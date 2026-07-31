package com.vladilima.vladmod.fountain;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.vladilima.vladmod.registries.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;

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
        blockQueue.add(creationPos);

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

        return new ScanResult(roomBlocks, wallBlocks, doorBlocks, creationPos, level.dimension());
    }

    public static class ScanResult {
        public List<BlockPos> roomBlocks;
        public List<BlockPos> wallBlocks;
        public List<BlockPos> doorBlocks;

        public BlockPos originPos;
        public ResourceKey<Level> dimension;

        public BlockPos lowestYPos;
        public BlockPos highestYPos;

        public ScanResult(List<BlockPos> roomBlocks, List<BlockPos> wallBlocks, List<BlockPos> doorBlocks, BlockPos creationPos, ResourceKey<Level> dimension) {
            this.roomBlocks = roomBlocks;
            this.wallBlocks = wallBlocks;
            this.doorBlocks = doorBlocks;

            this.originPos = creationPos;
            this.dimension = dimension;

            if (roomBlocks != null && !roomBlocks.isEmpty()) {
                List<BlockPos> sortedByY = roomBlocks.stream().sorted((a, b) -> (int) (a.getY() - b.getY())).toList();
                this.lowestYPos = sortedByY.getFirst();
                this.highestYPos = sortedByY.getLast();
            }
        }

        public static final Codec<ScanResult> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                BlockPos.CODEC.listOf().fieldOf("roomBlocks").forGetter(ScanResult::roomBlocks),
                BlockPos.CODEC.listOf().fieldOf("wallBlocks").forGetter(ScanResult::wallBlocks),
                BlockPos.CODEC.listOf().fieldOf("doorBlocks").forGetter(ScanResult::doorBlocks),
                BlockPos.CODEC.fieldOf("originPos").forGetter(ScanResult::originPos),
                Level.RESOURCE_KEY_CODEC.fieldOf("dimension").forGetter(ScanResult::dimension)
        ).apply(instance, ScanResult::new));

        public List<BlockPos> roomBlocks() {
            return roomBlocks;
        }

        public List<BlockPos> wallBlocks() {
            return wallBlocks;
        }

        public List<BlockPos> doorBlocks() {
            return doorBlocks;
        }

        public BlockPos originPos() {
            return originPos;
        }

        public ResourceKey<Level> dimension() {
            return dimension;
        }
    }
}
