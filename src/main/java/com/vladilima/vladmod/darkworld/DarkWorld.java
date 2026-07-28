package com.vladilima.vladmod.darkworld;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.fountain.RoomScanner;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DarkWorld {

    public static final int DARK_WORLD_SIZE = 16;

    public void buildDarkWorld(Level level, RoomScanner.ScanResult roomInfo) {
        Level darkWorldLevel = Objects.requireNonNull(level.getServer()).getLevel(DimensionManager.DARK_WORLD);
        assert darkWorldLevel != null;

        List<BlockPos> darkWorldFloor = new ArrayList<>(Collections.emptyList());

        // Create rough outline of dark world by tracing the floor blocks
        List<BlockPos> roomFloorBlocks = getFloorOfRoom(roomInfo);
        for (BlockPos block : roomFloorBlocks) {
            BlockPos relativeToFountain = block.subtract(roomInfo.originPos);
            BlockPos largePos = block.offset(relativeToFountain.multiply(DARK_WORLD_SIZE)).atY(1);

            AABB floor = new AABB(largePos).inflate(randInt(DARK_WORLD_SIZE, DARK_WORLD_SIZE * 3), 0, randInt(DARK_WORLD_SIZE, DARK_WORLD_SIZE * 3));
            floor = floor.setMaxY(1.5);

            BlockPos.betweenClosedStream(floor)
                    .forEach(blockPos -> darkWorldFloor.add(blockPos.immutable()));
        }

        Optional<BoundingBox> boundingBox = BoundingBox.encapsulatingPositions(darkWorldFloor);
        if (boundingBox.isPresent()) {
            VladMod.LOGGER.debug("Started Placing Blocks.");
            AtomicInteger i = new AtomicInteger();

//            AABB darkWorldAreaBox = AABB.of(boundingBox.get());
//            darkWorldAreaBox = darkWorldAreaBox.inflate(.5, -.5, .5);
//            BlockPos.betweenClosedStream(darkWorldAreaBox)
//                    .filter(blockPos -> !darkWorldFloor.contains(blockPos))
//                    .forEach(blockPos -> {
//                        LevelChunk levelChunk = darkWorldLevel.getChunkAt(blockPos);
//                        for (int y = 1; y < darkWorldLevel.getMaxBuildHeight(); y++) {
//                            i.addAndGet(1);
//                            levelChunk.setBlockState(blockPos.atY(y), Blocks.IRON_BLOCK.defaultBlockState(), false);
//                        }
//                    });

            List<BlockPos> darkWorldFloorFiltered = darkWorldFloor.stream().distinct().toList();

            darkWorldFloorFiltered.forEach(blockPos -> {
                LevelChunk levelChunk = darkWorldLevel.getChunkAt(blockPos);
                for (int y = 1; y < darkWorldLevel.getMaxBuildHeight(); y++) {
                    LevelChunkSection section = levelChunk.getSection(levelChunk.getSectionIndex(y));
                    i.addAndGet(1);
                    setBlockChunkSection(
                            section,
                            blockPos,
                            y <= 63 ? Blocks.IRON_BLOCK.defaultBlockState() : Blocks.AIR.defaultBlockState()
                    );
                }
            });

            VladMod.LOGGER.debug("Finished Placing Blocks. Placed {} Blocks.", i);
        } else {
            VladMod.LOGGER.error("Failed to get Dark World Bounding Box.");
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

    private static void setBlockChunkSection(LevelChunkSection chunkSection, BlockPos blockPos, BlockState blockState) {
        int x = blockPos.getX() & 15;
        int y = blockPos.getX() & 15;
        int z = blockPos.getZ() & 15;
        chunkSection.setBlockState(
                x, y, z,
                blockState,
                false
        );
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
