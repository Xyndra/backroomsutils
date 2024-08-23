package de.xyndra.backroomsutils.generation

import com.mojang.logging.LogUtils
import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level

// NOTE!!!: MOJANG SAYS NORTH IS Z-, THIS CODE SAYS NORTH IS X+

enum class CardinalDirection {
    NORTH,
    EAST,
    SOUTH,
    WEST
}

enum class Direction {
     NONE,
    // These are dead ends
    // XXXXXXXXXXXX
    // ########XXXX
    // ########XXXX
    // XXXXXXXXXXXX
    NORTH,
    EAST,
    SOUTH,
    WEST,
    // These are always symmetrical
    // ############
    // ############
    TWO_X,
    TWO_Z,
    // The direction describes the direction of the sideways tile (the one marked with @)
    // ############
    // ############
    //     @@@
    //     @@@
    THREE_NORTH,
    THREE_EAST,
    THREE_SOUTH,
    THREE_WEST,
    // This means that it is open to all directions
    //     ###
    //     ###
    // ############
    // ############
    //     ###
    //     ###
    FULL;

    fun openTo(direction: CardinalDirection): Boolean {
        if (this == FULL) return true
        return when (direction) {
            CardinalDirection.NORTH -> this == NORTH || this == TWO_X || this == THREE_NORTH || this == THREE_WEST || this == THREE_EAST
            CardinalDirection.EAST -> this == EAST || this == TWO_Z || this == THREE_EAST || this == THREE_NORTH || this == THREE_SOUTH
            CardinalDirection.SOUTH -> this == SOUTH || this == TWO_X || this == THREE_SOUTH || this == THREE_EAST || this == THREE_WEST
            CardinalDirection.WEST -> this == WEST || this == TWO_Z || this == THREE_WEST || this == THREE_SOUTH || this == THREE_NORTH
        }
    }

    companion object {
        private val bad = listOf(
            NORTH, EAST, SOUTH, WEST, FULL
        )

        private fun waveFunctionCollapse(north: Direction?, east: Direction?, south: Direction?, west: Direction?): Direction {
            val options = entries.toMutableList()
            if (north != null && north.openTo(CardinalDirection.SOUTH)) options.removeIf { !it.openTo(CardinalDirection.NORTH) }
            else if (north != null) options.removeIf { it.openTo(CardinalDirection.NORTH) }
            if (east != null && east.openTo(CardinalDirection.WEST)) options.removeIf { !it.openTo(CardinalDirection.EAST) }
            else if (east != null) options.removeIf { it.openTo(CardinalDirection.EAST) }
            if (south != null && south.openTo(CardinalDirection.NORTH)) options.removeIf { !it.openTo(CardinalDirection.SOUTH) }
            else if (south != null) options.removeIf { it.openTo(CardinalDirection.SOUTH) }
            if (west != null && west.openTo(CardinalDirection.EAST)) options.removeIf { !it.openTo(CardinalDirection.WEST) }
            else if (west != null) options.removeIf { it.openTo(CardinalDirection.WEST) }
            // try to avoid dead ends if possible
            if (options.size > 1) {
                options.remove(NONE)
            }
            if (options.any { it !in bad }) {
                options.removeIf { it in bad }
            }

            if (options.isEmpty()) {
                LogUtils.getLogger().error("No options left: $north, $east, $south, $west")
                return NONE
            }

            return options.random()
        }

        private fun getDirection(blockPos: BlockPos, adapter: DirectionAdapter, pLevel: Level): Direction? {
            return adapter.getType(blockPos, pLevel)
        }

        fun waveFunctionCollapseWrapper(adapter: DirectionAdapter, pLevel: Level, newX: Int, newY: Int, newZ: Int): Direction {
            val north = getDirection(BlockPos(newX + 8, newY, newZ), adapter, pLevel)
            val east = getDirection(BlockPos(newX, newY, newZ + 8), adapter, pLevel)
            val south = getDirection(BlockPos(newX - 8, newY, newZ), adapter, pLevel)
            val west = getDirection(BlockPos(newX, newY, newZ - 8), adapter, pLevel)

            val direction = waveFunctionCollapse(north, east, south, west)
            // hotStore[BlockPos(newX, newY, newZ)] = direction

            return direction
        }
    }
}