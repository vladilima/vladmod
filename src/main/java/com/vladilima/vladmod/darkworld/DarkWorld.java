package com.vladilima.vladmod.darkworld;

import com.vladilima.vladmod.fountain.RoomScanner;
import net.minecraft.core.BlockPos;
import net.minecraft.world.RandomSequence;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class DarkWorld {
    public void buildDarkWorld(Level level, RoomScanner.ScanResult roomInfo) {
        Level darkWorldLevel = Objects.requireNonNull(level.getServer()).getLevel(DimensionManager.DARK_WORLD);
        assert darkWorldLevel != null;

        // Create rough outline of room by tracing the floor blocks
        List<BlockPos> floorBlocks = getFloorOfRoom(roomInfo);
        for (BlockPos block : floorBlocks) {
            BlockPos relativeToFountain = block.subtract(roomInfo.originPos);
            BlockPos largePos = block.offset(relativeToFountain.multiply(8));
            AABB floor = new AABB(largePos).inflate(randInt(8, 24), 0, randInt(8, 24));
            BlockPos.betweenClosedStream(floor)
                    .forEach(blockPos -> darkWorldLevel.setBlockAndUpdate(blockPos, Blocks.STONE.defaultBlockState()));
        }

//        BoundingBox bBox = getFountainBB(roomInfo.wallBlocks);
//        AABB boundingBox = AABB.encapsulatingFullBlocks(bBox.bottomNorthWestPos, bBox.TopSouthEastPos.offset(-1, -1, -1));
//        BlockPos.betweenClosedStream(boundingBox)
//                .forEach(blockPos -> darkWorldLevel.setBlockAndUpdate(blockPos, Blocks.REDSTONE_BLOCK.defaultBlockState()));
//        for (double x = boundingBox.minX; x <= boundingBox.maxX; x++) {
//            for (double z = boundingBox.minZ; z <= boundingBox.maxZ; z++) {
//                darkWorldLevel.setBlockAndUpdate(BlockPos.containing(x, boundingBox.minY, z), Blocks.STONE.defaultBlockState());
//            }
//        }
    }

    private static List<BlockPos> getFloorOfRoom(RoomScanner.ScanResult roomInfo) {
        List<BlockPos> floorBlocks = new ArrayList<>();
        for (BlockPos pos : roomInfo.wallBlocks) {
            if (roomInfo.roomBlocks.contains(pos.above())) {
                floorBlocks.add(pos);
            }
        }

        return floorBlocks.stream().map(blockPos -> blockPos.atY(roomInfo.lowestYPos.getY())).toList();
    }

    static Random rand = new Random();
    public static int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

//    private BoundingBox getFountainBB(List<BlockPos> borderBlocks) {
//        List<BlockPos> sortedByY = borderBlocks.stream()
//                .sorted((a, b) -> (int) (a.getY() - b.getY())).toList();
//        BlockPos lowestYPos = sortedByY.getFirst();
//        BlockPos highestYPos = sortedByY.getLast();
//
//        List<BlockPos> sortedByX = borderBlocks.stream()
//                .sorted((a, b) -> (int) (a.getX() - b.getX())).toList();
//        BlockPos lowestXPos = sortedByX.getFirst();
//        BlockPos highestXPos = sortedByX.getLast();
//
//        List<BlockPos> sortedByZ = borderBlocks.stream()
//                .sorted((a, b) -> (int) (a.getZ() - b.getZ())).toList();
//        BlockPos lowestZPos = sortedByZ.getFirst();
//        BlockPos highestZPos = sortedByZ.getLast();
//
//        BlockPos bottomNorthWestPos = BlockPos.containing(lowestXPos.getX(), lowestYPos.getY(), lowestZPos.getZ());
//        BlockPos TopSouthEastPos = BlockPos.containing(highestXPos.getX(), highestYPos.getY(), highestZPos.getZ());
//
//        return new BoundingBox(bottomNorthWestPos, TopSouthEastPos);
//    }

//    private class BoundingBox {
//        BlockPos bottomNorthWestPos;
//        BlockPos TopSouthEastPos;
//
//        public BoundingBox(BlockPos bottomNorthWestPos, BlockPos topSouthEastPos) {
//            this.bottomNorthWestPos = bottomNorthWestPos;
//            TopSouthEastPos = topSouthEastPos;
//        }
//    }
}
