package de.xyndra.backroomsutils.generation.adapters

import de.xyndra.backroomsutils.generation.Direction
import de.xyndra.backroomsutils.generation.DirectionAdapter
import net.minecraft.Util
import net.minecraft.core.BlockPos
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.util.RandomSource
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.Mirror
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager

object Level0Adapter : DirectionAdapter {
    override fun place(pLevel: Level, startX: Int, startY: Int, startZ: Int, direction: Direction) {
        val pos = BlockPos(startX, startY, startZ)

        super.place(pLevel, startX, startY, startZ, direction)

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
        assert(pLevel is ServerLevel)
        template.placeInWorld(pLevel as ServerLevel, structurePos, structurePos, settings, RandomSource.create(Util.getMillis()), 2)
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
        return super.getType(blockPos, pLevel)
    }
}