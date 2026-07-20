package com.vladilima.vladmod.fountain;

import com.vladilima.vladmod.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.*;

public class RoomScanner {
    public static ScanResult scan(Level level, BlockPos creationPos, boolean ignoreDarkness, List<BlockPos> ignoreList) {
        if (level.canSeeSky(creationPos)) {
            return null; // Empty scan (invalid room)
        }

        List<BlockPos> roomBlocks = new ArrayList<>();
        List<BlockPos> wallBlocks = new ArrayList<>();
        List<BlockPos> doorBlocks = new ArrayList<>();

        LinkedList<BlockPos> blockQueue = new LinkedList<>();
        if (ignoreDarkness) {
            blockQueue.add(creationPos);
        } else {
            blockQueue.add(findStartPosition(level, creationPos));
        }

        BlockPos checkingBlock;

        while (!blockQueue.isEmpty()) {
            checkingBlock = blockQueue.removeFirst();

            if (level.canSeeSky(checkingBlock)) {
                return null; // Empty scan (invalid room)
            }

            if (!roomBlocks.contains(checkingBlock) || !wallBlocks.contains(checkingBlock)) {
                if (level.isInWorldBounds(checkingBlock) && level.isEmptyBlock(checkingBlock)) {
                    roomBlocks.add(checkingBlock);

                    for (Direction direction : Direction.values()){
                        BlockPos neighborPos = checkingBlock.relative(direction);
                        if (!roomBlocks.contains(neighborPos) && !wallBlocks.contains(neighborPos) && !blockQueue.contains(neighborPos) && !ignoreList.contains(neighborPos)) {
                            if (ignoreDarkness) {
                                if (level.getBlockState(neighborPos) != ModBlocks.DARKNESS.get().defaultBlockState()) {
                                    blockQueue.add(neighborPos);
                                }
                            } else {
                                blockQueue.add(neighborPos);
                            }
                        }
                    }
                } else {
                    if (level.getBlockState(checkingBlock).is(BlockTags.DOORS)) {
                        doorBlocks.add(checkingBlock);
                    } else {
                        wallBlocks.add(checkingBlock);
                    }
                }
            }
        }

        return new ScanResult(roomBlocks, wallBlocks, doorBlocks);
    }

    private static BlockPos findStartPosition(Level level, BlockPos creationPos) {
        BlockPos startPos = creationPos;
        BlockPos blockAbove = startPos.relative(Direction.UP);
        while (level.isEmptyBlock(blockAbove)) {
            startPos = blockAbove;
            blockAbove = startPos.relative(Direction.UP);
        }
        return startPos;
    }

    public static class ScanResult {
        private static final String ROOM_BLOCKS = "room_blocks";
        private static final String WALL_BLOCKS = "wall_blocks";
        private static final String DOOR_BLOCKS = "door_blocks";

        public List<BlockPos> roomBlocks;
        public List<BlockPos> wallBlocks;
        public List<BlockPos> doorBlocks;

        public BlockPos lowestYPos;
        public BlockPos highestYPos;

        public ScanResult(List<BlockPos> roomBlocks, List<BlockPos> wallBlocks, List<BlockPos> doorBlocks) {
            this.roomBlocks = roomBlocks;
            this.wallBlocks = wallBlocks;
            this.doorBlocks = doorBlocks;

            if (roomBlocks != null && !roomBlocks.isEmpty()) {
                List<BlockPos> sortedByY = roomBlocks.stream().sorted((a, b) -> (int) (a.getY() - b.getY())).toList();
                this.lowestYPos = sortedByY.getFirst();
                this.highestYPos = sortedByY.getLast();
            }
        }



        public CompoundTag save() {
            CompoundTag tag = new CompoundTag();

            ListTag roomBlocks = new ListTag();
            for (BlockPos pos : this.roomBlocks) {
                roomBlocks.add(NbtUtils.writeBlockPos(pos));
            }
            tag.put(ROOM_BLOCKS, roomBlocks);

            ListTag wallBlocks = new ListTag();
            for (BlockPos pos : this.wallBlocks) {
                wallBlocks.add(NbtUtils.writeBlockPos(pos));
            }
            tag.put(WALL_BLOCKS, wallBlocks);

            ListTag doorBlocks = new ListTag();
            for (BlockPos pos : this.doorBlocks) {
                doorBlocks.add(NbtUtils.writeBlockPos(pos));
            }
            tag.put(DOOR_BLOCKS, doorBlocks);

            return tag;
        }

        public static ScanResult load(CompoundTag tag) {
            List<BlockPos> roomBlocks = new ArrayList<>();
            for (Tag t : tag.getList(ROOM_BLOCKS, ListTag.TAG_COMPOUND)) {
                roomBlocks.add(NbtUtils.readBlockPos((CompoundTag) t, ROOM_BLOCKS).get());
            }

            List<BlockPos> wallBlocks = new ArrayList<>();
            for (Tag t : tag.getList(WALL_BLOCKS, ListTag.TAG_COMPOUND)) {
                wallBlocks.add(NbtUtils.readBlockPos((CompoundTag) t, WALL_BLOCKS).get());
            }

            List<BlockPos> doorBlocks = new ArrayList<>();
            for (Tag t : tag.getList(DOOR_BLOCKS, ListTag.TAG_COMPOUND)) {
                doorBlocks.add(NbtUtils.readBlockPos((CompoundTag) t, DOOR_BLOCKS).get());
            }

            return new ScanResult(roomBlocks, wallBlocks, doorBlocks);
        }
    }
}
