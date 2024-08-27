package de.xyndra.backroomsutils;

import com.mojang.logging.LogUtils;
import de.xyndra.backroomsutils.generation.Direction;
import de.xyndra.backroomsutils.generation.DirectionAdapter;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class JavaAdapter implements DirectionAdapter {
    static JavaAdapter INSTANCE = new JavaAdapter();

    @Override
    public void place(@NotNull Level pLevel, int startX, int startY, int startZ, @NotNull Direction direction) {
        DefaultImpls.place(this, pLevel, startX, startY, startZ, direction);
        BlockPos pos = new BlockPos(startX, startY, startZ);

        StructureTemplateManager manager = pLevel.getServer() != null ? pLevel.getServer().getStructureManager() : null;
        if (manager == null) {
            return;
        }

        Optional<StructureTemplate> optionalTemplate = switch (direction) {
            case NONE -> manager.get(new ResourceLocation("backroomsutils:level0_none"));
            case NORTH, EAST, SOUTH, WEST -> manager.get(new ResourceLocation("backroomsutils:level0_one"));
            case TWO_X, TWO_Z -> manager.get(new ResourceLocation("backroomsutils:level0_two"));
            case THREE_NORTH, THREE_EAST, THREE_SOUTH, THREE_WEST ->
                    manager.get(new ResourceLocation("backroomsutils:level0_three"));
            case FULL -> manager.get(new ResourceLocation("backroomsutils:level0_all"));
        };

        if (optionalTemplate.isEmpty()) {
            LogUtils.getLogger().error("Template not found({})", direction);
            return;
        }

        StructureTemplate template = optionalTemplate.get();

        StructurePlaceSettings settings = switch (direction) {
            case FULL, NONE, NORTH, TWO_X, THREE_NORTH ->
                    new StructurePlaceSettings().setMirror(Mirror.NONE).setRotation(Rotation.NONE).setIgnoreEntities(true);
            case EAST, TWO_Z, THREE_EAST ->
                    new StructurePlaceSettings().setMirror(Mirror.NONE).setRotation(Rotation.CLOCKWISE_90).setIgnoreEntities(true);
            case SOUTH, THREE_SOUTH ->
                    new StructurePlaceSettings().setMirror(Mirror.NONE).setRotation(Rotation.CLOCKWISE_180).setIgnoreEntities(true);
            case WEST, THREE_WEST ->
                    new StructurePlaceSettings().setMirror(Mirror.NONE).setRotation(Rotation.COUNTERCLOCKWISE_90).setIgnoreEntities(true);
        };

        BlockPos structurePos = switch (settings.getRotation()) {
            case CLOCKWISE_90 -> pos.offset(7, 1, 0);
            case CLOCKWISE_180 -> pos.offset(7, 1, 7);
            case COUNTERCLOCKWISE_90 -> pos.offset(0, 1, 7);
            default -> pos.offset(0, 1, 0);
        };

        if (pLevel instanceof ServerLevel) {
            template.placeInWorld((ServerLevel) pLevel, structurePos, structurePos, settings, RandomSource.create(Util.getMillis()), 2);
        }
    }

    @Override
    public @Nullable Direction getType(@NotNull BlockPos blockPos, @NotNull Level pLevel) {
        return DefaultImpls.getType(this, blockPos, pLevel);
    }

    @Override
    public void erase(@NotNull Level pLevel, int startX, int startY, int startZ, @NotNull Direction direction) {
        DefaultImpls.erase(this, pLevel, startX, startY, startZ, direction);
    }
}
