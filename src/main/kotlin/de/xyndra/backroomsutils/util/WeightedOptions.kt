package de.xyndra.backroomsutils.util

interface Weightable<T> {
    fun getWeight(input: T): Int
}

class WeightedOptions<T : Weightable<U>, U: Any> : HashMap<T, Int>() {
    fun fromJavaList(list: Collection<T>, input: U) {
        for (option in list) {
            assert(!this.containsKey(option)) { "Option $option already exists" }
            this[option] = option.getWeight(input)
        }
    }

    fun pickOne(): T {
        val totalWeight = this.values.sum()
        val random = (0 until totalWeight).random()
        var currentWeight = 0
        for ((option, weight) in this) {
            currentWeight += weight
            if (random < currentWeight) {
                return option
            }
        }
        throw IllegalStateException("No option found")
    }
}