package us.timinc.mc.cobblemon.chaining.util

import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.entity.player.PlayerEntity
import us.timinc.mc.cobblemon.counter.api.CaptureApi
import us.timinc.mc.cobblemon.counter.api.KoApi

object Util {
    fun <T> pickRandomItems(inputSet: Set<T>, numToPick: Int): Set<T> {
        if (numToPick <= 0 || numToPick > inputSet.size) {
            throw IllegalArgumentException("Invalid number of items to pick")
        }

        val inputList = inputSet.toMutableList()
        val pickedItems = mutableSetOf<T>()

        repeat(numToPick) {
            val randomIndex = (0 until inputList.size).random()
            val randomItem = inputList.removeAt(randomIndex)
            pickedItems.add(randomItem)
        }

        return pickedItems
    }

    private fun pokemonHasLabel(pokemon: Pokemon, label: String): Boolean {
        return pokemon.hasLabels(label) || pokemon.species.name.lowercase() == label
    }

    fun matchesList(pokemon: Pokemon, whitelist: Set<String>, blacklist: Set<String>): Boolean {
        return when {
            whitelist.isNotEmpty() && blacklist.isNotEmpty() -> whitelist.any {
                pokemonHasLabel(
                    pokemon, it
                )
            } || blacklist.none { pokemonHasLabel(pokemon, it) }

            whitelist.isNotEmpty() -> whitelist.any { pokemonHasLabel(pokemon, it) }
            blacklist.isNotEmpty() -> blacklist.none { pokemonHasLabel(pokemon, it) }
            else -> true
        }
    }

    fun getPlayerScore(
        player: PlayerEntity,
        species: String,
        koStreakPoints: Int,
        koCountPoints: Int,
        captureStreakPoints: Int,
        captureCountPoints: Int
    ): Int {
        val koStreakData = KoApi.getStreak(player)
        val koStreak = if (koStreakData.first == species) koStreakData.second else 0
        val koCount = KoApi.getCount(player, species)
        val captureStreakData = CaptureApi.getStreak(player)
        val captureStreak = if (captureStreakData.first == species) captureStreakData.second else 0
        val captureCount = CaptureApi.getCount(player, species)
        return (koStreak * koStreakPoints) + (koCount * koCountPoints) + (captureStreak * captureStreakPoints) + (captureCount * captureCountPoints)
    }
}