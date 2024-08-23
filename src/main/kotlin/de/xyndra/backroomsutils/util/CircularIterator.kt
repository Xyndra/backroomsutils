package de.xyndra.backroomsutils.util

import kotlin.math.sqrt

// A circular iterator taking a radius of a square and starting from the center, iterating outwards
// If the width and height are odd, the iterator will start at the bottom right center and make sure to catch the
// other centers as well. The minimum radius is 1.

// Example:
// for ((x, y) in CircularIterator(5)) {
//     println("($x, $y)")
// }
// Output:
// (0, 0)
// (1, 0)
// (1, 1)
// (0, 1)
// (-1, 1)
// ...

class CircularIterator(private var radius: Int) : Iterator<Pair<Int, Int>> {
    private var x = 0
    private var y = 0
    private var currentDirection = Direction.LEFT
    private var toStep = 1
    private var currentSteps = 0

    init {
        if (radius < 1) {
            throw IllegalArgumentException("Radius must be at least 2")
        }

    }

    override fun hasNext(): Boolean {
        return sqrt((x * x + y * y).toDouble()) <= radius
    }

    override fun next(): Pair<Int, Int> {
        val result = Pair(x, y)
        if (currentSteps < toStep) {
            currentSteps++
            when (currentDirection) {
                Direction.RIGHT -> x++
                Direction.UP -> y--
                Direction.DOWN -> y++
                Direction.LEFT -> x--
            }
        } else {
            currentSteps = 0
            currentDirection = currentDirection.next()
            if (currentDirection == Direction.RIGHT || currentDirection == Direction.LEFT) {
                toStep++
            }
        }

        return result
    }

    private enum class Direction {
        RIGHT, DOWN, LEFT, UP;

        fun next(): Direction {
            return when (this) {
                RIGHT -> DOWN
                DOWN -> LEFT
                LEFT -> UP
                UP -> RIGHT
            }
        }
    }
}