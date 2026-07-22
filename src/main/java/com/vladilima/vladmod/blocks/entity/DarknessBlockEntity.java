package com.vladilima.vladmod.blocks.entity;

import com.vladilima.vladmod.registries.ModBlockEntities;
import com.vladilima.vladmod.registries.ModBlocks;
import com.vladilima.vladmod.fountain.DarkFountain;
import com.vladilima.vladmod.registries.ModParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Random;

public class DarknessBlockEntity extends BlockEntity {
    public DarkFountain fountain;

    public DarknessBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.DARKNESS_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    static List<Direction> directionList = List.of(Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST);
    public static void tick(Level level, BlockPos pos, BlockState state, DarknessBlockEntity blockEntity) {
        if (level.isClientSide()) {
            if (blockEntity.particleTick <= 0) {
                for (Direction direction : directionList) {
                    BlockPos relativeBlock = pos.relative(direction);
                    if (isParticulableBlock(level, relativeBlock)) {
                        Vec3 pPos = pos.getCenter();
                        Vec3i dirVector = direction.getNormal();
                        level.addParticle(
                                ModParticles.DARKNESS_PARTICLES.get(),
                                pPos.x() + (double) dirVector.getX() / 2,
                                pPos.y() + (double) dirVector.getY() / 2,
                                pPos.z() + (double) dirVector.getZ() / 2,
                                dirVector.getX(),
                                dirVector.getY(),
                                dirVector.getZ()
                        );

                    }
                }

                blockEntity.particleTick = 1;
            } else {
                blockEntity.particleTick -= 1;
            }
        } else {
            if (blockEntity.fountain != null && randInt(1, 20) == 20) {
                if (!blockEntity.fountain.hasBlock(pos)) {
                    level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                }
            }
        }
    }

    int particleTick = 0;
    private static boolean isParticulableBlock(Level level, BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);
        if (blockState.getBlock() instanceof DoorBlock) {
            return blockState.getValue(DoorBlock.OPEN);
        }

        return (level.isEmptyBlock(blockPos) &&
                !(level.getBlockState(blockPos) == ModBlocks.DARKNESS.get().defaultBlockState()));
    }

    static Random rand = new Random();
    public static int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }
}
