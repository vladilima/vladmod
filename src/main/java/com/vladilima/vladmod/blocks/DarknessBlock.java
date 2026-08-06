package com.vladilima.vladmod.blocks;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.blocks.entity.DarknessBlockEntity;
import com.vladilima.vladmod.blocks.great_door.GreatDoorCoreBlockEntity;
import com.vladilima.vladmod.registries.ModBlockEntities;
import com.vladilima.vladmod.darkworld.DimensionManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.vladilima.vladmod.darkworld.DarkWorld.DARK_WORLD_SIZE;

public class DarknessBlock extends Block implements EntityBlock {
    public DarknessBlock(Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new DarknessBlockEntity(pos, state);
    }

    // We use a second method here due to generic conversions
    // If extending `BaseEntityBlock`, this method is also available there as a protected static method
    private static <E extends BlockEntity, A extends BlockEntity> @Nullable BlockEntityTicker<A> createTickerHelper(
            BlockEntityType<A> type, BlockEntityType<E> checkedType, BlockEntityTicker<? super E> ticker
    ) {
        return checkedType == type ? (BlockEntityTicker<A>) ticker : null;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntity) {
        // You can return different tickers here, depending on whatever factors you want. A common use case would be
        // to return different tickers on the client or server, only tick one side to begin with,
        // or only return a ticker for some blockstates (e.g. when using a "my machine is working" blockstate property).
        return createTickerHelper(blockEntity, ModBlockEntities.DARKNESS_BLOCK_ENTITY.get(), DarknessBlockEntity::tick);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!level.isClientSide()) {
            DarknessBlockEntity blockEntity = (DarknessBlockEntity) level.getBlockEntity(pos);

            if (blockEntity.fountain != null && blockEntity.fountain.darkWorld != null) {
                ServerLevel darkWorldServerLevel = Objects.requireNonNull(level.getServer()).getLevel(DimensionManager.DARK_WORLD);
                assert darkWorldServerLevel != null;

                for (Direction dir : Direction.Plane.HORIZONTAL) {
                    if (level.getBlockState(pos.relative(dir)).is(BlockTags.DOORS)) {
                        BlockPos doorPos =
                                level.getBlockState(pos.relative(dir)).getValue(DoorBlock.HALF) == DoubleBlockHalf.LOWER ?
                                        pos.relative(dir) :
                                        pos.relative(dir).below();

                        GreatDoorCoreBlockEntity greatDoor = blockEntity.fountain.darkWorld.greatDoors.stream()
                                .map(gDoorPos -> (GreatDoorCoreBlockEntity) darkWorldServerLevel.getBlockEntity(gDoorPos))
                                .filter(greatDoorBE ->
                                        greatDoorBE.lightDoorPos.equals(doorPos) &&
                                                greatDoorBE.lightDoorDim.equals(level.dimension()))
                                .findAny().orElse(null);

                        if (greatDoor != null) {
                            DimensionTransition dimTransition = new DimensionTransition(darkWorldServerLevel,
                                    greatDoor.getBlockPos().getBottomCenter(), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot(),
                                    DimensionTransition.DO_NOTHING
                            );

                            greatDoor.entitiesDebounced.put(entity, GreatDoorCoreBlockEntity.DEBOUNCE_DURATION);
                            entity.changeDimension(dimTransition);
                        } else {
                            VladMod.LOGGER.error("Found no Great Door");
                        }
                    }
                }
            }
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected boolean skipRendering(BlockState state, BlockState adjacentState, Direction direction) {
        return adjacentState.getBlock() instanceof DarknessBlock || super.skipRendering(state, adjacentState, direction);
    }
}
