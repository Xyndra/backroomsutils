package de.xyndra.backroomsutils;

import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import static de.xyndra.backroomsutils.AdapterChooser.getBiomeName;

// Note: for safety, spawn your own hallways to guarantee that the player enter the room
public class BigRooms {
    public static void register() {
        BigRoomUtil.addBigRoom(new BigRoomUtil.BigRoom(BigRooms::spawnCrossRoom));
        BigRoomUtil.addBigRoom(new BigRoomUtil.BigRoom((level, pos) -> !getBiomeName(level, level.getBiome(pos).get()).equals("minecraft:the_end"), BigRooms::spawnGoldCrossRoom));
        BigRoomUtil.addBigRoom(new BigRoomUtil.BigRoom(3, BigRooms::spawnOtherCrossRoom));
    }

    private static void spawnCrossRoom(Level worldGenLevel, BlockPos blockPos) {
        // Replace the block at the blockpos
        LogUtils.getLogger().info("Spawning cross room at blockpos: {}", blockPos);
        worldGenLevel.setBlock(blockPos, Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
        worldGenLevel.setBlock(blockPos.offset(8, 0, 0), Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
        worldGenLevel.setBlock(blockPos.offset(-8, 0, 0), Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
        worldGenLevel.setBlock(blockPos.offset(0, 0, 8), Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
        worldGenLevel.setBlock(blockPos.offset(0, 0, -8), Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
    }

    private static void spawnGoldCrossRoom(Level worldGenLevel, BlockPos blockPos) {
        // Replace the block at the blockpos
        LogUtils.getLogger().info("Spawning gold cross room at blockpos: {}", blockPos);
        worldGenLevel.setBlock(blockPos, Blocks.GOLD_BLOCK.defaultBlockState(), 2);
        worldGenLevel.setBlock(blockPos.offset(8, 0, 0), Blocks.GOLD_BLOCK.defaultBlockState(), 2);
        worldGenLevel.setBlock(blockPos.offset(-8, 0, 0), Blocks.GOLD_BLOCK.defaultBlockState(), 2);
        worldGenLevel.setBlock(blockPos.offset(0, 0, 8), Blocks.GOLD_BLOCK.defaultBlockState(), 2);
        worldGenLevel.setBlock(blockPos.offset(0, 0, -8), Blocks.GOLD_BLOCK.defaultBlockState(), 2);
    }

    private static void spawnOtherCrossRoom(Level worldGenLevel, BlockPos blockPos) {
        // Replace the block at the blockpos
        LogUtils.getLogger().info("Spawning other cross room at blockpos: {}", blockPos);
        worldGenLevel.setBlock(blockPos, Blocks.EMERALD_BLOCK.defaultBlockState(), 2);
        worldGenLevel.setBlock(blockPos.offset(8, 0, 8), Blocks.EMERALD_BLOCK.defaultBlockState(), 2);
        worldGenLevel.setBlock(blockPos.offset(-8, 0, -8), Blocks.EMERALD_BLOCK.defaultBlockState(), 2);
        worldGenLevel.setBlock(blockPos.offset(8, 0, -8), Blocks.EMERALD_BLOCK.defaultBlockState(), 2);
        worldGenLevel.setBlock(blockPos.offset(-8, 0, 8), Blocks.EMERALD_BLOCK.defaultBlockState(), 2);
    }
}
