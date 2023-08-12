package us.timinc.mc.cobblemon.chaining.modules

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.spawning.context.SpawningContext
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.Level
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import us.timinc.mc.cobblemon.chaining.config.ShinyBoostConfig
import kotlin.random.Random

class ShinyBooster(private val config: ShinyBoostConfig) : SpawnActionModifier("shinyBooster") {
    override fun act(entity: PokemonEntity?, ctx: SpawningContext, props: PokemonProperties) {
        if (props.shiny != null) {
            return
        }
        info("${props.species} spawned at ${ctx.position.toShortString()}")

        val world: Level = ctx.world
        val species = props.species ?: return

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
        val shinyChances = config.getThreshold(points) + 1
        val shinyRate: Int = Cobblemon.config.shinyRate.toInt()

        info("${maxPlayer.name.string} wins with $points points, $shinyChances shiny chances out of $shinyRate total chances")

        if (shinyChances <= 1) {
            info("conclusion: winning player didn't get any shiny boost")
            return
        }

        val shinyRoll = Random.nextInt(shinyRate)
        val shinyResult = shinyRoll < shinyChances
        props.shiny = shinyResult
        info("conclusion: set shiny to $shinyResult")
    }
}