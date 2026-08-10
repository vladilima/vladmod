package com.vladilima.vladmod.blocks.great_door;

import com.vladilima.vladmod.registries.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.portal.DimensionTransition;
import net.minecraft.world.phys.AABB;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntityType;
import team.lodestar.lodestone.modules.toolkit.multiblock.MultiBlockCoreEntity;
import team.lodestar.lodestone.modules.toolkit.multiblock.MultiBlockStructure;

import javax.annotation.Nullable;
import java.util.List;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.OPEN;

public class GreatDoorCoreBlockEntity extends MultiBlockCoreEntity {
    public GreatDoorCoreBlockEntity(LodestoneBlockEntityType<?> type, MultiBlockStructure structure, BlockPos pos, BlockState state) {
        super(type, structure, pos, state);
    }

    public GreatDoorCoreBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.GREAT_DOOR.get(), GreatDoorStructure.STRUCTURE.get(), pos, state);
    }

    @Override
    public void setupMultiblock(BlockPos pos) {
        GreatDoorStructure structure = (GreatDoorStructure) getStructure();
        structure.rotatedPieces(this.getBlockState().getValue(BlockStateProperties.HORIZONTAL_FACING).getAxis()).forEach(p -> {
            Vec3i offset = p.offset;
            getComponentPositions().add(pos.offset(offset));
        });
    }

    @Override
    public void serverTick(ServerLevel level) {
        // Transports entities to Dark World
        AABB searchArea = AABB.encapsulatingFullBlocks(getComponentPositions().getFirst(), getComponentPositions().getLast());
        List<Entity> entitiesInside = level.getEntities(null, searchArea);
        for (Entity entity : entitiesInside) {
            if (isOpen(level.getBlockState(getBlockPos()))) {
                teleportOutOfDarkWorld(level, entity);
            }
        }

        // Sync to LW Door
        ServerLevel fountainLevel = level.getServer().getLevel(lightDoorDim);
        BlockState doorBlockState = fountainLevel.getBlockState(lightDoorPos);
        if (doorBlockState.is(BlockTags.DOORS)) {
            if (isOpen(getBlockState()) != doorBlockState.getValue(OPEN)) {
                useDoor(null);
            }
        } else if (isOpen(getBlockState())) {
            useDoor(null);
        }
    }

    public BlockPos lightDoorPos;
    public ResourceKey<Level> lightDoorDim;
    private void teleportOutOfDarkWorld(ServerLevel level, Entity entity) {
        ServerLevel fountainLevel = level.getServer().getLevel(lightDoorDim);

        DimensionTransition dimTransition = new DimensionTransition(fountainLevel,
                lightDoorPos.getBottomCenter(), entity.getDeltaMovement(), entity.getYRot(), entity.getXRot(),
                DimensionTransition.DO_NOTHING
        );

        entity.changeDimension(dimTransition);
    }

    @Override
    public InteractionResult onUseWithoutItem(Player pPlayer) {
        if (!level.isClientSide()) {

            ServerLevel fountainLevel = level.getServer().getLevel(lightDoorDim);
            BlockState doorBlockState = fountainLevel.getBlockState(lightDoorPos);
            if (doorBlockState.is(BlockTags.DOORS)) {
                DoorBlock doorBlock = (DoorBlock) doorBlockState.getBlock();
                if (doorBlock.type().canOpenByHand()) {
                    useDoor(pPlayer);
                    doorBlock.setOpen(
                            pPlayer,
                            fountainLevel,
                            doorBlockState,
                            lightDoorPos,
                            isOpen(level.getBlockState(getBlockPos()))
                    );
                }
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    public void useDoor(@Nullable Player player) {
        toggleOpenProperty(player, getBlockPos());
        for (BlockPos component : getComponentPositions()) {
            toggleOpenProperty(player, component);
        }
    }

    private void toggleOpenProperty(@Nullable Player pPlayer, BlockPos pos) {
        BlockState state = level.getBlockState(pos).cycle(BlockStateProperties.OPEN);
        level.setBlock(pos, state, 10);
        level.gameEvent(pPlayer, this.isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
    }

    private boolean isOpen(BlockState state) {
        return state.getValue(BlockStateProperties.OPEN);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("lightDoorPos", NbtUtils.writeBlockPos(lightDoorPos));
        tag.put("lightDoorDim", Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, lightDoorDim).getOrThrow());
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);

        lightDoorPos = NbtUtils.readBlockPos(tag, "lightDoorPos").orElseThrow();
        lightDoorDim = Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, tag.get("lightDoorDim")).getOrThrow();
    }
}
