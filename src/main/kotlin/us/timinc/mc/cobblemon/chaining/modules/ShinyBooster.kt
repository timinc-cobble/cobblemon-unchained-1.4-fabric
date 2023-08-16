package us.timinc.mc.cobblemon.chaining.modules

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.spawning.context.SpawningContext
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.world.entity.player.Player
import us.timinc.mc.cobblemon.chaining.config.ShinyBoostConfig
import us.timinc.mc.cobblemon.chaining.util.Util
import kotlin.random.Random

class ShinyBooster(private val config: ShinyBoostConfig) : SpawnActionModifier("shinyBooster") {
    override fun act(entity: PokemonEntity, ctx: SpawningContext) {
        val pokemon = entity.pokemon
        val species = entity.species.get().lowercase().split(":").last()

        info("$species spawned at ${ctx.position.toShortString()}", config.debug)

        if (!Util.matchesList(pokemon, config.whitelist, config.blacklist)) {
            info("$species is blocked by the blacklist", config.debug)
            return
        }

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
        val points = config.getPoints(maxPlayer, species)
        val shinyChances = config.getThreshold(points) + 1
        val shinyRate: Int = Cobblemon.config.shinyRate.toInt()

        if (shinyChances <= 1) {
            info("conclusion: winning player didn't get any shiny boost, leaving Cobblemon's decision", config.debug)
            return
        }

        val shinyRoll = Random.nextInt(shinyRate)
        val successfulRoll = shinyRoll < shinyChances

        info(
            "${maxPlayer.name.string} wins with $points points, $shinyChances shiny chances out of $shinyRate total chances, rolls a $shinyRoll, ${if (successfulRoll) "wins" else "loses"}",
            config.debug
        )
        entity.pokemon.shiny = successfulRoll
        info("conclusion: set shiny to $successfulRoll", config.debug)
    }
}