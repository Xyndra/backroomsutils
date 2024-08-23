package de.xyndra.backroomsutils

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraftforge.common.ForgeConfigSpec
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.config.ModConfigEvent
import net.minecraftforge.registries.ForgeRegistries
import java.util.stream.Collectors

@EventBusSubscriber(modid = BackroomsUtils.MODID, bus = EventBusSubscriber.Bus.MOD)
object Config {
    private val BUILDER = ForgeConfigSpec.Builder()

    private val LOG_DIRT_BLOCK: ForgeConfigSpec.BooleanValue = BUILDER
        .comment("Whether to log the dirt block on common setup")
        .define("logDirtBlock", true)

    private val MAGIC_NUMBER: ForgeConfigSpec.IntValue = BUILDER
        .comment("A magic number")
        .defineInRange("magicNumber", 42, 0, Int.MAX_VALUE)

    val MAGIC_NUMBER_INTRODUCTION: ForgeConfigSpec.ConfigValue<String> = BUILDER
        .comment("What you want the introduction message to be for the magic number")
        .define("magicNumberIntroduction", "The magic number is... ")

    // a list of strings that are treated as resource locations for items
    private val ITEM_STRINGS: ForgeConfigSpec.ConfigValue<List<String>> = BUILDER
        .comment("A list of items to log on common setup.")
        .defineListAllowEmpty(
            "items", listOf("minecraft:iron_ingot")
        ) { obj: Any -> validateItemName(obj) }

    @JvmField
    val SPEC: ForgeConfigSpec = BUILDER.build()

    @JvmField
    var logDirtBlock: Boolean = false
    @JvmField
    var magicNumber: Int = 0
    @JvmField
    var magicNumberIntroduction: String? = null
    @JvmField
    var items: Set<Item?>? = null

    private fun validateItemName(obj: Any): Boolean {
        return obj is String && ForgeRegistries.ITEMS.containsKey(ResourceLocation(obj))
    }


    @SubscribeEvent
    fun onLoad(event: ModConfigEvent?) {
        logDirtBlock = LOG_DIRT_BLOCK.get()
        magicNumber = MAGIC_NUMBER.get()
        magicNumberIntroduction = MAGIC_NUMBER_INTRODUCTION.get()

        // convert the list of strings into a set of items
        items = ITEM_STRINGS.get().stream()
            .map { itemName: String? -> ForgeRegistries.ITEMS.getValue(ResourceLocation(itemName)) }
            .collect(Collectors.toSet())
    }
}