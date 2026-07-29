package com.vladilima.vladmod.darkworld.generators;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.darkworld.GenerationUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;

public class CliffsGenerator extends Generator {
    private final int SURFACE_LEVEL = 8;

    @Override
    public void empty(Level level, List<BlockPos> generationArea) {
        VladMod.LOGGER.debug("Began generation of empty DW Location.");

        generationArea.forEach(blockPos -> {
            LevelChunk levelChunk = level.getChunkAt(blockPos);
            for (int y = 1; y < level.getMaxBuildHeight(); y++) {
                LevelChunkSection section = levelChunk.getSection(levelChunk.getSectionIndex(y));
                GenerationUtils.setBlockChunkSection(
                        section,
                        blockPos.atY(y),
                        y <= SURFACE_LEVEL ? Blocks.BLACK_CONCRETE.defaultBlockState() : Blocks.AIR.defaultBlockState()
                );
            }
        });

        VladMod.LOGGER.debug("Finished DW hollowing-out.");
    }

    @Override
    public void surface(Level level, List<BlockPos> generationArea) {
        VladMod.LOGGER.debug("Began generation of surface of Dark World.");

        List<AABB> currentPaths = new ArrayList<>();

        int pathAmount = getPathAmount(generationArea);
        for (int i = 0; i < pathAmount; i++) {
//            int pathSegmentAmount = GenerationUtils.randInt(5, 15);
            int pathSegmentAmount = GenerationUtils.randInt(10, 20);
            BlockPos previousPoint = null;
            for (int j = 0; j < pathSegmentAmount; j++) {
                BlockPos newPoint = newPoint(level, generationArea, previousPoint);
                if (previousPoint != null) {
                    currentPaths.add(newPathSegment(previousPoint, newPoint));
                }

                previousPoint = newPoint;
            }
        }

        for (AABB pathBox : currentPaths) {
            BlockPos.betweenClosedStream(pathBox).forEach(blockPos -> {
                LevelChunk levelChunk = level.getChunkAt(blockPos);
                LevelChunkSection section = levelChunk.getSection(levelChunk.getSectionIndex(blockPos.getY()));
                GenerationUtils.setBlockChunkSection(
                        section,
                        blockPos,
                        Blocks.BLUE_TERRACOTTA.defaultBlockState()
                );
            });
        }

        VladMod.LOGGER.debug("Finished DW surface Generation.");
    }

    // Decides amount of path structures the dark world will have based on size
    private static int getPathAmount(List<BlockPos> generationArea) {
        return (int) (Math.sqrt(generationArea.size()) / 15);
    }

    private BlockPos newPoint(Level level, List<BlockPos> generationArea, BlockPos previousPoint) {
        BlockPos startingPos = previousPoint != null ?
                previousPoint :
                generationArea.get(GenerationUtils.randInt(0, generationArea.size())).atY(GenerationUtils.randInt(SURFACE_LEVEL + 5, 63));

        Direction pathDir = Direction.Plane.HORIZONTAL.getRandomDirection(level.random);
        int segmentSize = GenerationUtils.randInt(8, 24);
        BlockPos newPoint = startingPos.relative(pathDir, segmentSize);
        while (!isPointValid(generationArea, newPoint)) {
            pathDir = Direction.Plane.HORIZONTAL.getRandomDirection(level.random);
            segmentSize = GenerationUtils.randInt(8, 24);
            newPoint = startingPos.relative(pathDir, segmentSize);
        }
        return newPoint;
    }

    private boolean isPointValid(List<BlockPos> generationArea, BlockPos newPoint) {
        return generationArea.contains(newPoint.atY(1));
    }

    private AABB newPathSegment(BlockPos start, BlockPos target) {
        AABB pathBox = AABB.encapsulatingFullBlocks(start, target);
        pathBox = pathBox.setMinY(SURFACE_LEVEL + 1);

        Direction dir = Direction.fromDelta(
                start.getX() - target.getX(),
                start.getY() - target.getY(),
                start.getZ() - target.getZ()
        ).getClockWise();

        return pathBox.inflate(Math.abs(dir.getNormal().getX()) * 1.5, 0, Math.abs(dir.getNormal().getZ()) * 1.5).move(.5,0,.5);
    }

    @Override
    public void features(Level level, List<BlockPos> generationArea) {
        VladMod.LOGGER.debug("Began populating Dark World with Features.");
    }

    @Override
    public void entities(Level level, List<BlockPos> generationArea) {
        VladMod.LOGGER.debug("Began populating Dark World with Entities.");
    }
}
