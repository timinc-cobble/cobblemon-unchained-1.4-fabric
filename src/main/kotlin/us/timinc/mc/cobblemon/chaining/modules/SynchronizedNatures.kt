package us.timinc.mc.cobblemon.chaining.modules

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.spawning.context.SpawningContext
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Nature
import net.minecraft.server.level.ServerPlayer
import us.timinc.mc.cobblemon.chaining.config.SynchronizedNaturesConfig
import us.timinc.mc.cobblemon.chaining.util.Util
import kotlin.random.Random
import kotlin.random.Random.Default.nextDouble
import kotlin.random.Random.Default.nextInt

class SynchronizedNatures(private val config: SynchronizedNaturesConfig) : SpawnActionModifier("synchronizedNatures") {
    override fun act(entity: PokemonEntity, ctx: SpawningContext) {
        val pokemon = entity.pokemon
        val species = pokemon.species.name.lowercase()

        info("$species spawned at ${ctx.position.toShortString()}", config.debug)

        val roll = nextDouble()
        info("Rolled a $roll out of ${config.percentChance}", config.debug)
        if (roll >= config.percentChance) {
            return
        }

        if (!Util.matchesList(pokemon, config.whitelist, config.blacklist)) {
            info("$species is blocked by the blacklist", config.debug)
            return
        }

        val nearbyPlayers = getNearbyPlayers(ctx, config.effectiveRange.toDouble())
        info("nearby players: ${nearbyPlayers.size} ${nearbyPlayers.map { it.name.string }}", config.debug)
        if (nearbyPlayers.isEmpty()) {
            info("conclusion: no nearby players", config.debug)
            return
        }

        val nearbyPlayersWithSynchronize = nearbyPlayers.filter { getSynchronizedNature(it) != null }
        info("nearby players with synchronize: ${nearbyPlayersWithSynchronize.size} ${
            nearbyPlayersWithSynchronize.map { it.name.string }
        }:${nearbyPlayersWithSynchronize.map { config.getPoints(it, species) }}", config.debug
        )
        if (nearbyPlayersWithSynchronize.isEmpty()) {
            info("conclusion: no nearby players with synchronize", config.debug)
            return
        }

        val totalMarbles = nearbyPlayersWithSynchronize.sumOf { config.getPoints(it, species) }
        var pickedMarble = nextInt(totalMarbles)
        info("out of $totalMarbles total marbles, picked marble #$pickedMarble", config.debug)
        val pickedPlayer = nearbyPlayersWithSynchronize.find {
            pickedMarble -= config.getPoints(it, species)
            pickedMarble <= 0
        }!!

        getSynchronizedNature(pickedPlayer)?.let {
            entity.pokemon.nature = it
            info("conclusion: setting $species nature to ${it.displayName}", config.debug)
        }
    }

    private fun getSynchronizedNature(player: ServerPlayer): Nature? {
        val playerPartyStore = Cobblemon.storage.getParty(player)
        if (config.mustBeFirst) {
            val firstPartyMember = playerPartyStore.firstOrNull()
            if (firstPartyMember?.ability?.name != "synchronize") {
                return null
            }
            return firstPartyMember.nature
        }

        return playerPartyStore.find { it.ability.name == "synchronize" }?.nature
    }
}