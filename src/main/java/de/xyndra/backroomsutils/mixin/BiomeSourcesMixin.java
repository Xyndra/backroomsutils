package de.xyndra.backroomsutils.mixin;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import de.xyndra.backroomsutils.BackroomsBiomeSource;
import net.minecraft.core.Registry;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.BiomeSources;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(BiomeSources.class)
public class BiomeSourcesMixin {
    @Inject(method = "bootstrap", at = @At("HEAD"))
    private static void bootstrap(Registry<Codec<? extends BiomeSource>> pRegistry, CallbackInfoReturnable<Codec<? extends BiomeSource>> cir) {
        LogUtils.getLogger().info("Registering backrooms biome source");
        Registry.register(pRegistry, "backrooms_biomes", BackroomsBiomeSource.CODEC);
    }
}
