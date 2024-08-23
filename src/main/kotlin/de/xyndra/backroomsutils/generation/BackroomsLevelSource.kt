package de.xyndra.backroomsutils.generation

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.BlockPos
import net.minecraft.core.Holder
import net.minecraft.resources.RegistryOps
import net.minecraft.server.level.WorldGenRegion
import net.minecraft.world.level.LevelHeightAccessor
import net.minecraft.world.level.NoiseColumn
import net.minecraft.world.level.StructureManager
import net.minecraft.world.level.WorldGenLevel
import net.minecraft.world.level.biome.Biome
import net.minecraft.world.level.biome.BiomeManager
import net.minecraft.world.level.biome.Biomes
import net.minecraft.world.level.biome.FixedBiomeSource
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
import java.util.function.Function

// TODO: Disable mob spawning
class BackroomsLevelSource(p_255723_: Holder.Reference<Biome?>) : ChunkGenerator(FixedBiomeSource(p_255723_)) {
    override fun codec(): Codec<out ChunkGenerator?> {
        return CODEC
    }

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

        // TODO: Detect dimension
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
            RecordCodecBuilder.create { p_255576_: RecordCodecBuilder.Instance<BackroomsLevelSource?> ->
                p_255576_.group(
                    RegistryOps.retrieveElement(
                        Biomes.PLAINS
                    )
                ).apply(
                    p_255576_,
                    p_255576_.stable(
                        Function { p_255723_: Holder.Reference<Biome?> ->
                            BackroomsLevelSource(
                                p_255723_
                            )
                        })
                )
            }
        private val AIR: BlockState = Blocks.AIR.defaultBlockState()
        private val BARRIER: BlockState = Blocks.BARRIER.defaultBlockState()
        private val DIRT: BlockState = Blocks.DIRT.defaultBlockState()
    }
}
