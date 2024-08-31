package de.xyndra.backroomsutils;

import com.mojang.logging.LogUtils;
import de.xyndra.backroomsutils.util.GeneralUtilKt;
import de.xyndra.backroomsutils.util.Weightable;
import de.xyndra.backroomsutils.util.WeightedOptions;
import kotlin.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class BigRoomUtil {
    private static final List<BigRoom> bigRooms = new ArrayList<>();

    public static void addBigRoom(BigRoom bigRoom) {
        bigRooms.add(bigRoom);
    }

    // Min and max distance in blocks between big rooms (not cells!)
    // Be careful with center and edge points since this code only cares about what your functions are called with
    public static final int MIN_DISTANCE = 32;
    public static final int MAX_DISTANCE = 256;

    @SuppressWarnings("ConstantValue")
    public static @Nullable BigRoom getBigRoom(Level level, BlockPos pos) {
        assert MIN_DISTANCE < MAX_DISTANCE;
        assert MIN_DISTANCE > 0;
        int cellSize = MAX_DISTANCE / 2;
        int minDistance = MIN_DISTANCE / 2;
        assert cellSize % 8 == 0;
        assert minDistance % 8 == 0;

        if (pos.getX() % 8 != 0 || pos.getZ() % 8 != 0) {
            LogUtils.getLogger().warn("Weird position: {}", pos);
            return null;
        }

        // uniform random position in the cell based on seed and pos
        BlockPos cellPos = new BlockPos(
                pos.getX() - GeneralUtilKt.positiveModulo(pos.getX(), cellSize),
                0, pos.getZ() - GeneralUtilKt.positiveModulo(pos.getZ(), cellSize)
        );
        Random random = new Random(cellPos.asLong() + level.dimension().location().getPath().hashCode());
        // Make sure the random position is MIN_DISTANCE away from the walls and is divisible by 8
        int xOffset = (random.nextInt(cellSize - 2 * minDistance) + minDistance) & ~7;
        int zOffset = (random.nextInt(cellSize - 2 * minDistance) + minDistance) & ~7;
        BlockPos randomPos = cellPos.offset(xOffset, 0, zOffset);

        if (pos.equals(randomPos)) {
            return randomBigRoom(level, randomPos);
        }

        return null;
    }

    private static BigRoom randomBigRoom(Level level, BlockPos pos) {
        WeightedOptions<BigRoom, Pair<Level, BlockPos>> weightedOptions = new WeightedOptions<>();
        weightedOptions.fromJavaList(bigRooms, new Pair<>(level, pos));
        return weightedOptions.pickOne();
    }

    public static class BigRoom implements Weightable<Pair<Level, BlockPos>> {
        private final BiFunction<Level, BlockPos, Boolean> canSpawn;
        private final BiFunction<Level, BlockPos, Integer> getRarity;
        private final BiConsumer<Level, BlockPos> onSpawn;

        public BigRoom(BiFunction<Level, BlockPos, Boolean> canSpawn, BiFunction<Level, BlockPos, Integer> getRarity, BiConsumer<Level, BlockPos> onSpawn) {
            this.canSpawn = canSpawn;
            this.getRarity = getRarity;
            this.onSpawn = onSpawn;
        }

        public BigRoom(BiFunction<Level, BlockPos, Boolean> canSpawn, int rarity, BiConsumer<Level, BlockPos> onSpawn) {
            this.canSpawn = canSpawn;
            this.getRarity = (level, pos) -> rarity;
            this.onSpawn = onSpawn;
        }

        public BigRoom(int rarity, BiConsumer<Level, BlockPos> onSpawn) {
            this.canSpawn = (pos, level) -> true;
            this.getRarity = (pos, level) -> rarity;
            this.onSpawn = onSpawn;
        }

        public BigRoom(BiFunction<Level, BlockPos, Boolean> canSpawn, BiConsumer<Level, BlockPos> onSpawn) {
            this.canSpawn = canSpawn;
            this.getRarity = (level, pos) -> 1;
            this.onSpawn = onSpawn;
        }

        public BigRoom(BiConsumer<Level, BlockPos> onSpawn) {
            this.canSpawn = (pos, level) -> true;
            this.getRarity = (pos, level) -> 1;
            this.onSpawn = onSpawn;
        }

        public boolean canSpawn(Level level, BlockPos pos) {
            return canSpawn.apply(level, pos);
        }

        @Override
        public int getWeight(Pair<Level, BlockPos> input) {
            if (canSpawn(input.getFirst(), input.getSecond())) {
                return getRarity.apply(input.getFirst(), input.getSecond());
            }
            return 0;
        }

        public void spawn(Level level, BlockPos pos) {
            onSpawn.accept(level, pos);
        }
    }
}
