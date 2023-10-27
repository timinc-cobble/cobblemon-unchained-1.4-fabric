package us.timinc.mc.cobblemon.chaining.util

import com.cobblemon.mod.common.pokemon.Pokemon

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
}