package de.xyndra.backroomsutils;

import com.mojang.logging.LogUtils;
import de.xyndra.backroomsutils.generation.DirectionAdapter;
import de.xyndra.backroomsutils.generation.adapters.Level0Adapter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;

import java.util.Objects;

public class AdapterChooser {
    public static DirectionAdapter getAdapter(Level level, BlockPos pos) {


        if (getBiomeName(level, level.getBiome(pos).get()).equals("minecraft:the_end")) {
            return JavaAdapter.INSTANCE;
        } else {
            LogUtils.getLogger().error("Adapter not found for biome({})", getBiomeName(level, level.getBiome(pos).get()));
        }

        return Level0Adapter.INSTANCE;
    }

    public static String getBiomeName(Level level, Biome biome) {
        return Objects.requireNonNull(level.registryAccess().registryOrThrow(Registries.BIOME).getKey(biome)).toString();
    }
}
