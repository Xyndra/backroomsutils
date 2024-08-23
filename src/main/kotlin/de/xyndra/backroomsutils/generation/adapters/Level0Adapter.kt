package de.xyndra.backroomsutils.generation.adapters

import com.mojang.logging.LogUtils
import de.xyndra.backroomsutils.generation.Direction
import de.xyndra.backroomsutils.generation.DirectionAdapter
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager

object Level0Adapter : DirectionAdapter {
    override fun place(pLevel: WorldGenLevel, startX: Int, startY: Int, startZ: Int, direction: Direction) {
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

        val manager: StructureTemplateManager = pLevel.server?.structureManager ?: return
        val template: StructureTemplate = when (direction) {
            Direction.NONE -> manager.get(ResourceLocation("backroomsutils:level0_none")).get()
            Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST -> manager.get(ResourceLocation("backroomsutils:level0_one_debug")).get()
            Direction.TWO_X, Direction.TWO_Z -> manager.get(ResourceLocation("backroomsutils:level0_two_debug")).get()
            Direction.THREE_NORTH, Direction.THREE_EAST, Direction.THREE_SOUTH, Direction.THREE_WEST -> manager.get(ResourceLocation("backroomsutils:level0_three_debug")).get()
            Direction.FULL -> manager.get(ResourceLocation("backroomsutils:level0_all_debug")).get()
        }
        val settings = when (direction) {
            Direction.FULL, Direction.NONE, Direction.NORTH, Direction.TWO_X, Direction.THREE_NORTH -> (StructurePlaceSettings()).setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(true)
            Direction.EAST, Direction.TWO_Z, Direction.THREE_EAST -> (StructurePlaceSettings()).setMirror(Mirror.NONE).setRotation(Rotation.CLOCKWISE_90).setIgnoreEntities(true)
            Direction.SOUTH, Direction.THREE_SOUTH -> (StructurePlaceSettings()).setMirror(Mirror.NONE).setRotation(Rotation.CLOCKWISE_180).setIgnoreEntities(true)
            Direction.WEST, Direction.THREE_WEST -> (StructurePlaceSettings()).setMirror(Mirror.NONE).setRotation(Rotation.COUNTERCLOCKWISE_90).setIgnoreEntities(true)
        }

        val structurePos = when (settings.rotation) {
            Rotation.NONE, null -> pos.offset(0, 1, 0)
            Rotation.CLOCKWISE_90 -> pos.offset(7, 1, 0)
            Rotation.CLOCKWISE_180 -> pos.offset(7, 1, 7)
            Rotation.COUNTERCLOCKWISE_90 -> pos.offset(0, 1, 7)
        }
        template.placeInWorld(pLevel, structurePos, structurePos, settings, RandomSource.create(Util.getMillis()), 2)
    }

    override fun getType(blockPos: BlockPos, pLevel: Level): Direction? {
        val block = pLevel.getBlockState(blockPos).block
        if (block != Blocks.AIR) {
            for (height in 9..63) {
                val pos = blockPos.above(height)
                if (pLevel.getBlockState(pos).block == Blocks.AIR) {
                    pLevel.setBlock(pos, block.defaultBlockState(), 2)
                    break
                }
            }
        }
        return when (block) {
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
            Blocks.AIR -> {
                LogUtils.getLogger().warn("Block at $blockPos is air")
                return null
            }
            else -> null
        }
    }
}