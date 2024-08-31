package de.xyndra.backroomsutils;

import com.mojang.logging.LogUtils;
import de.xyndra.backroomsutils.generation.BackroomsLevelSource;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;

public class GenerationCallbacks {
    // Call this from the main startup method
    // Note: These will be called in the order they are registered, so the later ones might override the earlier ones
    // If you replace the block at the blockpos, it will not generate the block at the blockpos
    public static void register() {
        BackroomsLevelSource.Companion.addCallback("backroomsutils:level_0", GenerationCallbacks::sampleCallback);
        // BackroomsLevelSource.Companion.addCallback("backroomsutils:level_0", GenerationCallbacks::biomeCallback);
    }

    private static void biomeCallback(WorldGenLevel worldGenLevel, BlockPos blockPos) {
        LogUtils.getLogger().info("Biome at blockpos: {}", AdapterChooser.getBiomeName(worldGenLevel.getLevel(), worldGenLevel.getBiome(blockPos).get()));
    }

    private static void sampleCallback(WorldGenLevel level, BlockPos pos) {
        if (pos.getX() == 256 && pos.getZ() == 256) {
            // Replace the block at the blockpos
            level.setBlock(pos, Blocks.DIAMOND_BLOCK.defaultBlockState(), 2);
        }
    }


}
