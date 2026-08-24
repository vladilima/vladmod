package com.vladilima.vladmod.darkworld.generation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;

import java.util.Random;

public class GenerationUtils {
    public static void setBlockAtChunkSection(LevelChunk chunk, BlockPos blockPos, BlockState blockState) {
        LevelChunkSection section = chunk.getSection(chunk.getSectionIndex(blockPos.getY()));
        int x = blockPos.getX() & 15;
        int y = blockPos.getY() & 15;
        int z = blockPos.getZ() & 15;
        section.setBlockState(
                x, y, z,
                blockState,
                false
        );
    }

    static Random rand = new Random();
    public static int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }
}
