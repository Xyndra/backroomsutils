package de.xyndra.backroomsutils.generation

import com.mojang.logging.LogUtils
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.server.level.WorldGenRegion
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.NoiseColumn
import net.minecraft.world.level.StructureManager
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.biome.BiomeManager
import net.minecraft.world.level.biome.BiomeSource
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.chunk.ChunkAccess
import net.minecraft.world.level.chunk.ChunkGenerator
import net.minecraft.world.level.levelgen.GenerationStep.Carving
import net.minecraft.world.level.levelgen.Heightmap
import net.minecraft.world.level.levelgen.RandomState
import net.minecraft.world.level.levelgen.blending.Blender
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executor
import java.util.function.BiConsumer
import java.util.function.Function

// TODO: Disable mob spawning
class BackroomsLevelSource(customBiomeSource: BiomeSource) : ChunkGenerator(customBiomeSource) {

    override fun buildSurface(
        pLevel: WorldGenRegion,
        pStructureManager: StructureManager,
        pRandom: RandomState,
        pChunk: ChunkAccess
    ) {
    }

    override fun applyBiomeDecoration(pLevel: WorldGenLevel, pChunk: ChunkAccess, pStructureManager: StructureManager) {
        val chunkpos = pChunk.pos
        val i = chunkpos.x * 16
        val j = chunkpos.z * 16

        pLevel.setBlock(BlockPos(i, 0, j), DIRT, 2)
        pLevel.setBlock(BlockPos(i + 8, 0, j), DIRT, 2)
        pLevel.setBlock(BlockPos(i, 0, j + 8), DIRT, 2)
        pLevel.setBlock(BlockPos(i + 8, 0, j + 8), DIRT, 2)

        // callbacks based on dimension
        if (Callbacks.containsKey(pLevel.level.dimension().location().toString())) {
            Callbacks[pLevel.level.dimension().location().toString()]!!.forEach { callback ->
                callback.accept(pLevel, BlockPos(i, 0, j))
                callback.accept(pLevel, BlockPos(i + 8, 0, j))
                callback.accept(pLevel, BlockPos(i, 0, j + 8))
                callback.accept(pLevel, BlockPos(i + 8, 0, j + 8))
            }
        } else {
            LogUtils.getLogger().warn("No callbacks found for dimension ${pLevel.level.dimension().location()}")
        }
    }

    override fun fillFromNoise(
        pExecutor: Executor,
        pBlender: Blender,
        pRandom: RandomState,
        pStructureManager: StructureManager,
        pChunk: ChunkAccess
    ): CompletableFuture<ChunkAccess> {
        return CompletableFuture.completedFuture(pChunk)
    }

    override fun getBaseHeight(
        pX: Int,
        pZ: Int,
        pType: Heightmap.Types,
        pLevel: LevelHeightAccessor,
        pRandom: RandomState
    ): Int {
        return 0
    }

    override fun getBaseColumn(pX: Int, pZ: Int, pHeight: LevelHeightAccessor, pRandom: RandomState): NoiseColumn {
        return NoiseColumn(0, arrayOfNulls(0))
    }

    override fun addDebugScreenInfo(pInfo: List<String>, pRandom: RandomState, pPos: BlockPos) {
    }

    override fun codec(): Codec<BackroomsLevelSource?> {
        return CODEC
    }

    override fun applyCarvers(
        pLevel: WorldGenRegion,
        pSeed: Long,
        pRandom: RandomState,
        pBiomeManager: BiomeManager,
        pStructureManager: StructureManager,
        pChunk: ChunkAccess,
        pStep: Carving
    ) {
    }

    override fun spawnOriginalMobs(pLevel: WorldGenRegion) {}

    override fun getMinY(): Int {
        return 0
    }

    override fun getGenDepth(): Int {
        return 64
    }

    override fun getSeaLevel(): Int {
        return 1
    }

    companion object {
        @JvmField
        val CODEC: Codec<BackroomsLevelSource?> =
            RecordCodecBuilder.create { levelSourceInstance: RecordCodecBuilder.Instance<BackroomsLevelSource> ->
                return@create levelSourceInstance.group(
                    BiomeSource.CODEC.fieldOf("biome_source").forGetter(BackroomsLevelSource::biomeSource)
                ).apply(
                    levelSourceInstance,
                    levelSourceInstance.stable(Function { biomeSource: BiomeSource -> BackroomsLevelSource(biomeSource) })
                )
            }
        private val DIRT: BlockState = Blocks.DIRT.defaultBlockState()
        private var Callbacks: Map<String, MutableList<BiConsumer<WorldGenLevel, BlockPos>>> = emptyMap()
        fun addCallback(dimension: String, callback: BiConsumer<WorldGenLevel, BlockPos>) {
            if (Callbacks.containsKey(dimension)) {
                Callbacks[dimension]!!.add(callback)
            } else {
                Callbacks = Callbacks + (dimension to mutableListOf(callback))
            }
        }
    }
}
