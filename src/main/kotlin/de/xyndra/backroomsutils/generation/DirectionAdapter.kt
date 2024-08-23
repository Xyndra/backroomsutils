package de.xyndra.backroomsutils.generation

import net.minecraft.core.BlockPos
import net.minecraft.world.level.Level
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Blocks

interface DirectionAdapter {
    fun place(pLevel: WorldGenLevel, startX: Int, startY: Int, startZ: Int, direction: Direction)
    fun getType(blockPos: BlockPos, pLevel: Level): Direction?
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