package us.timinc.mc.cobblemon.chaining.modules

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.pokemon.stats.Stat
import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.api.spawning.context.SpawningContext
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.IVs
import net.minecraft.world.entity.player.Player
import us.timinc.mc.cobblemon.chaining.config.IvBoostConfig
import us.timinc.mc.cobblemon.chaining.util.Util

class IvBooster(private val config: IvBoostConfig) : SpawnActionModifier("ivBooster") {
    override fun act(entity: PokemonEntity?, ctx: SpawningContext, props: PokemonProperties) {
        info("${props.species} spawned at ${ctx.position.toShortString()}", config.debug)

        val innerEntity = entity ?: return
        val species = innerEntity.pokemon.species.name.lowercase()

        val nearbyPlayers = getNearbyPlayers(ctx, config.effectiveRange.toDouble())
        info("nearby players: ${nearbyPlayers.size} ${
            nearbyPlayers.map {
                "${it.name.string}:${
                    config.getPoints(
                        it, species
                    )
                }"
            }
        }", config.debug)
        val possibleMaxPlayer = nearbyPlayers.stream().max(Comparator.comparingInt { player: Player? ->
            config.getPoints(player!!, species)
        })
        if (possibleMaxPlayer.isEmpty) {
            info("conclusion: no nearby players", config.debug)
            return
        }

        val maxPlayer = possibleMaxPlayer.get()
        val points = config.getPoints(maxPlayer, props.species!!)
        val perfectIvCount = config.getThreshold(points)

        info("${maxPlayer.name.string} wins with $points points, $perfectIvCount perfect IVs", config.debug)

        if (perfectIvCount <= 0) {
            info("conclusion: winning player didn't get any perfect IVs", config.debug)
            return
        }

        val perfectIvs: Set<Stat> = Util.pickRandomItems(Stats.BATTLE_ONLY, perfectIvCount)
        for (perfectIv in perfectIvs) {
            innerEntity.pokemon.ivs[perfectIv] = IVs.MAX_VALUE
        }
        info("conclusion: set ${perfectIvs.map { it.displayName.string }} to perfect", config.debug)
    }
}