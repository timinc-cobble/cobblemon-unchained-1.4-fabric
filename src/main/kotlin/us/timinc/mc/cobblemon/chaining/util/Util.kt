package us.timinc.mc.cobblemon.chaining.util

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
}