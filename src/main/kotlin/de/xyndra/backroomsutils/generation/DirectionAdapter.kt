package de.xyndra.backroomsutils.generation

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks

interface DirectionAdapter {
    fun place(pLevel: Level, startX: Int, startY: Int, startZ: Int, direction: Direction) {
        val pos = BlockPos(startX, startY, startZ)

        when (direction) {
            Direction.NONE -> pLevel.setBlock(pos, Blocks.BEDROCK.defaultBlockState(), 2)
            Direction.NORTH -> pLevel.setBlock(pos, Blocks.LIME_WOOL.defaultBlockState(), 2)
            Direction.EAST -> pLevel.setBlock(pos, Blocks.BLUE_WOOL.defaultBlockState(), 2)
            Direction.SOUTH -> pLevel.setBlock(pos, Blocks.YELLOW_WOOL.defaultBlockState(), 2)
            Direction.WEST -> pLevel.setBlock(pos, Blocks.RED_WOOL.defaultBlockState(), 2)
            Direction.TWO_X -> pLevel.setBlock(pos, Blocks.ORANGE_CONCRETE.defaultBlockState(), 2)
            Direction.TWO_Z -> pLevel.setBlock(pos, Blocks.PURPLE_CONCRETE.defaultBlockState(), 2)
            Direction.THREE_NORTH -> pLevel.setBlock(pos, Blocks.PINK_STAINED_GLASS.defaultBlockState(), 2)
            Direction.THREE_EAST -> pLevel.setBlock(pos, Blocks.BLACK_STAINED_GLASS.defaultBlockState(), 2)
            Direction.THREE_SOUTH -> pLevel.setBlock(pos, Blocks.MAGENTA_STAINED_GLASS.defaultBlockState(), 2)
            Direction.THREE_WEST -> pLevel.setBlock(pos, Blocks.CYAN_STAINED_GLASS.defaultBlockState(), 2)
            Direction.FULL -> pLevel.setBlock(pos, Blocks.TARGET.defaultBlockState(), 2)
        }
    }

    fun getType(blockPos: BlockPos, pLevel: Level): Direction? {
        return when (val block = pLevel.getBlockState(blockPos).block) {
            Blocks.BEDROCK -> Direction.NONE
            Blocks.LIME_WOOL -> Direction.NORTH
            Blocks.BLUE_WOOL -> Direction.EAST
            Blocks.YELLOW_WOOL -> Direction.SOUTH
            Blocks.RED_WOOL -> Direction.WEST
            Blocks.ORANGE_CONCRETE -> Direction.TWO_X
            Blocks.PURPLE_CONCRETE -> Direction.TWO_Z
            Blocks.PINK_STAINED_GLASS -> Direction.THREE_NORTH
            Blocks.BLACK_STAINED_GLASS -> Direction.THREE_EAST
            Blocks.MAGENTA_STAINED_GLASS -> Direction.THREE_SOUTH
            Blocks.CYAN_STAINED_GLASS -> Direction.THREE_WEST
            Blocks.TARGET -> Direction.FULL
            Blocks.DIRT -> null
            else -> {
                // LogUtils.getLogger().info("Unknown block: $block")
                null
            }
        }
    }

    fun erase(pLevel: Level, startX: Int, startY: Int, startZ: Int, direction: Direction) {
        for (x in 0..8) {
            for (y in 0..8) {
                for (z in 0..8) {
                    val pos = BlockPos(startX + x, startY + y, startZ + z)
                    pLevel.setBlock(pos, Blocks.AIR.defaultBlockState(), 2)
                }
            }
        }
    }
}