package com.vladilima.vladmod.blocks.great_door;

import com.vladilima.vladmod.registries.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntityType;
import team.lodestar.lodestone.modules.toolkit.multiblock.MultiBlockCoreEntity;
import team.lodestar.lodestone.modules.toolkit.multiblock.MultiBlockStructure;

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
    public InteractionResult onUseWithoutItem(Player pPlayer) {
        toggleOpenProperty(pPlayer, getBlockPos());
        for (BlockPos component : getComponentPositions()) {
            toggleOpenProperty(pPlayer, component);
        }
        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    private void toggleOpenProperty(Player pPlayer, BlockPos pos) {
        BlockState state = level.getBlockState(pos).cycle(BlockStateProperties.OPEN);
        level.setBlock(pos, state, 10);
        level.gameEvent(pPlayer, this.isOpen(state) ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, pos);
    }

    private boolean isOpen(BlockState state) {
        return state.getValue(BlockStateProperties.OPEN);
    }
}
