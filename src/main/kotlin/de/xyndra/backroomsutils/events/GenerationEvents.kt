package de.xyndra.backroomsutils.events

import de.xyndra.backroomsutils.AdapterChooser
import de.xyndra.backroomsutils.BackroomsUtils
import de.xyndra.backroomsutils.generation.Direction
import de.xyndra.backroomsutils.generation.DirectionAdapter
import de.xyndra.backroomsutils.util.CircularIterator
import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.block.Blocks
import net.minecraftforge.event.TickEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod

@Mod.EventBusSubscriber(modid = BackroomsUtils.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
class GenerationEvents {
    private val lock = Any()
    @SubscribeEvent
    fun levelTick(event: TickEvent.LevelTickEvent) {
        if (event.side.isClient) return
        val level =  event.level as ServerLevel
        if (event.phase != TickEvent.Phase.END) return
        synchronized(lock) {
            for (player: ServerPlayer in level.players()) {
                // TODO: Figure out how to get the render distance of the player
                val renderDistance = 10
                val playerPos = player.onPos
                val startPos = BlockPos(playerPos.x - playerPos.x % 8, 0, playerPos.z - playerPos.z % 8)
                for ((x, z) in CircularIterator(renderDistance * 2)) {
                    val pos = startPos.offset(x * 8, 0, z * 8)
                    if (level.getBlockState(pos).block == Blocks.DIRT) {
                        val adapter: DirectionAdapter = AdapterChooser.getAdapter(level, pos)
                        val direction = Direction.waveFunctionCollapseWrapper(adapter, level, pos.x, pos.y, pos.z)
                        adapter.place(
                            level,
                            pos.x,
                            pos.y,
                            pos.z,
                            direction
                        )
                        break
                    }
                }
            }
        }
    }
}