package us.timinc.mc.cobblemon.chaining.modules

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.spawning.context.SpawningContext
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import us.timinc.mc.cobblemon.chaining.config.HiddenBoostConfig
import kotlin.random.Random

class HiddenBooster(private val config: HiddenBoostConfig) : SpawnActionModifier("hiddenBooster") {
    override fun act(entity: PokemonEntity?, ctx: SpawningContext, props: PokemonProperties) {
        info("${props.species} spawned at ${ctx.position.toShortString()}")

        val innerEntity = entity ?: return
        val pokemon = innerEntity.pokemon
        val world: Level = ctx.world
        val species = pokemon.species.name.lowercase()

        val range = config.effectiveRange.toDouble()
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS") val nearbyPlayers = world.getNearbyPlayers(
            TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting(), null, AABB.ofSize(
                Vec3.atCenterOf(ctx.position), range, range, range
            )
        )
        info("nearby players: ${nearbyPlayers.size} ${
            nearbyPlayers.map {
                "${it.name.string}:${
                    config.getPoints(
                        it, species
                    )
                }"
            }
        }")
        val possibleMaxPlayer = nearbyPlayers.stream().max(Comparator.comparingInt { player: Player? ->
            config.getPoints(player!!, species)
        })
        if (possibleMaxPlayer.isEmpty) {
            info("conclusion: no nearby players")
            return
        }

        val maxPlayer = possibleMaxPlayer.get()
        val points = config.getPoints(maxPlayer, props.species!!)
        val chances = config.getThreshold(points)

        if (chances == null) {
            info("${maxPlayer.name.string} wins with $points points, has no chance")
            info("conclusion: winning player didn't get any hidden ability chance")
            return
        }

        info("${maxPlayer.name.string} wins with $points points, has a ${chances.first} out of ${chances.second}")

        val goodMarbles = chances.first
        val totalMarbles = chances.second
        val hiddenAbilityRoll = Random.nextInt(0, totalMarbles)
        val successfulRoll =hiddenAbilityRoll >= goodMarbles
        if (successfulRoll) {
            return
        }

        val tForm = pokemon.form
        val potentialAbilityMapping = tForm.abilities.mapping[Priority.LOW] ?: return

        val potentialAbility = potentialAbilityMapping[0]
        val newAbilityBuilder = potentialAbility.template.builder
        val newAbility = newAbilityBuilder.invoke(potentialAbility.template, false)
        newAbility.index = 0
        newAbility.priority = Priority.LOW
        pokemon.ability = newAbility
        info("conclusion: gave $species its hidden ability")
    }
}