package com.vladilima.vladmod.fountain;

import com.vladilima.vladmod.blocks.DarknessBlock;
import com.vladilima.vladmod.blocks.ModBlocks;
import com.vladilima.vladmod.blocks.entity.DarknessBlockEntity;
import net.minecraft.client.resources.sounds.Sound;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DarkFountain {
    private static final String ROOM_INFO = "room_info";
    private static final String TICKS_ALIVE = "ticks_alive";

    RoomScanner.ScanResult roomInfo;
    public final BlockPos FOUNTAIN_POS;
    private BlockPos currentBlock;
    List<BlockPos> roomBreaches = new ArrayList<>();

    private int ticksAlive = 0;

    DarkFountain (RoomScanner.ScanResult scan) {
        this.roomInfo = scan;
        this.FOUNTAIN_POS = scan.roomBlocks.getFirst();
        this.currentBlock = FOUNTAIN_POS;
    }

    DarkFountain (RoomScanner.ScanResult scan, int ticksAlive) {
        this.roomInfo = scan;
        this.FOUNTAIN_POS = scan.roomBlocks.getFirst();
        this.currentBlock = FOUNTAIN_POS;

        this.ticksAlive = ticksAlive;
    }

    private static int DARKNESS_SPREAD_DELAY = 5;
    private int ticksToSpread = 0;
    public void tick(Level level) {
        if (!level.isClientSide()) {
            if (this.ticksAlive == 0) {
                if (level.setBlockAndUpdate(currentBlock, ModBlocks.DARKNESS.get().defaultBlockState())) {
                    DarknessBlockEntity newDarkness = (DarknessBlockEntity) level.getBlockEntity(currentBlock);
                    if (newDarkness != null) {
                        newDarkness.fountain = this;
                    }
                };
            }

            if (ticksToSpread <= 0) {
                if (!isRoomFilled(level)) {
                    darknessSpread(level);
                }

                List<BlockPos> foundBreaches = getRoomBreaches(level);
                if (!foundBreaches.isEmpty()) {
                    for (BlockPos breachPos : foundBreaches) {
                        RoomScanner.ScanResult breachScan = RoomScanner.scan(level, breachPos, true, List.of());
                        if (breachScan != null && !breachScan.roomBlocks.isEmpty() && !breachScan.wallBlocks.isEmpty()) {
                            // Remove possible duplicates
                            breachScan.roomBlocks.removeAll(roomInfo.roomBlocks);
                            breachScan.wallBlocks.removeAll(roomInfo.wallBlocks);
                            breachScan.doorBlocks.removeAll(roomInfo.doorBlocks);

                            // Adds new scan
                            roomInfo.roomBlocks.addAll(breachScan.roomBlocks);
                            roomInfo.wallBlocks.addAll(breachScan.wallBlocks);
                            roomInfo.doorBlocks.addAll(breachScan.doorBlocks);

                            if (breachScan.highestYPos.getY() > this.roomInfo.highestYPos.getY()) {
                                this.roomInfo.highestYPos = breachScan.highestYPos;
                            }
                            if (breachScan.lowestYPos.getY() < this.roomInfo.lowestYPos.getY()) {
                                this.roomInfo.lowestYPos = breachScan.lowestYPos;
                            }

                            this.roomBreaches.remove(breachPos);
                        } else {
                            if (!this.roomBreaches.contains(breachPos)) {
                                this.roomBreaches.add(breachPos);
                            }
                        }
                    }
                }

                ticksToSpread = DARKNESS_SPREAD_DELAY;
            }

            ticksToSpread--;
            ticksAlive++;
        }
    }

    private boolean isRoomFilled(Level level) {
        boolean isFilled = true;
        for (BlockPos blockPos : roomInfo.roomBlocks){
            if (fillableBlock(level, blockPos)) {
                isFilled = false;
            } else if (isSolid(level, blockPos)) {
                RoomScanner.ScanResult roomScan = RoomScanner.scan(level, this.FOUNTAIN_POS, false, roomBreaches);
                if (roomScan != null && !roomScan.roomBlocks.isEmpty()) {
                    this.roomInfo = roomScan;
                } else {
                    this.roomInfo.roomBlocks.remove(blockPos);
                    if (level.getBlockState(blockPos).is(BlockTags.DOORS)) {
                        this.roomInfo.doorBlocks.add(blockPos);
                    } else {
                        this.roomInfo.wallBlocks.add(blockPos);
                    }
                }
            }
        }

        return isFilled;
    }

    private List<BlockPos> getRoomBreaches(Level level) {
        List<BlockPos> breachList = new ArrayList<>();

        for (BlockPos blockPos : roomInfo.wallBlocks){
            if (fillableBlock(level, blockPos)) {
                breachList.add(blockPos);
            }
        }

        for (BlockPos blockPos : roomInfo.doorBlocks){
            if (fillableBlock(level, blockPos)) {
                breachList.add(blockPos);
            }
        }

        // Remove non-breaches from breach list
        this.roomBreaches.removeIf(breachPos -> isSolid(level, breachPos));

        return breachList;
    }

    private void darknessSpread(Level level) {
        int spreadAmount = Integer.min(getSpreadAmount(level), 8);
        for (int i = 0; i < spreadAmount; i++) {
            while (!fillableBlock(level, currentBlock) || !isBlockReachable(level, currentBlock)) {
                currentBlock = getNextBlock(level);
            }

            if (roomInfo.roomBlocks.contains(currentBlock)) {
                if (level.setBlockAndUpdate(currentBlock, ModBlocks.DARKNESS.get().defaultBlockState())) {
                    DarknessBlockEntity newDarkness = (DarknessBlockEntity) level.getBlockEntity(currentBlock);
                    if (newDarkness != null) {
                        newDarkness.fountain = this;
                    }
                };

            } else {
                currentBlock = getNextBlock(level);
            }
        }
    }

    private int getSpreadAmount(Level level) {
        List<BlockPos> availableBlocks = roomInfo.roomBlocks.stream()
                .filter((blockPos -> fillableBlock(level, blockPos) && isBlockReachable(level, blockPos)))
                .toList();

        return Integer.max(availableBlocks.size() / 5, 1);
    }

    private boolean fillableBlock(Level level, BlockPos blockPos) {
        return level.getBlockState(blockPos) != ModBlocks.DARKNESS.get().defaultBlockState() && level.isEmptyBlock(blockPos);
    }

    private boolean isSolid(Level level, BlockPos blockPos) {
        return level.getBlockState(blockPos) != ModBlocks.DARKNESS.get().defaultBlockState() && !level.isEmptyBlock(blockPos);
    }

    private BlockPos getNextBlock(Level level) {
        if (fillableBlock(level, currentBlock.relative(Direction.UP)) && isBlockReachable(level, currentBlock.relative(Direction.UP))) {
            return currentBlock.relative(Direction.UP);
        } else {
            List<BlockPos> currentLayer = getYLayer(currentBlock.getY());
            if (!filledLayer(level, currentLayer)) {
                List<BlockPos> possibleBlocks = currentLayer.stream()
                        .filter((blockPos -> fillableBlock(level, blockPos) && isBlockReachable(level, blockPos)))
                        .toList();

                return possibleBlocks.isEmpty() ?
                        currentBlock.relative(Direction.DOWN) :
                        possibleBlocks.get(randInt(0, possibleBlocks.size() - 1));
            } else if (currentBlock.getY() < roomInfo.lowestYPos.getY() - 1) {
                return roomInfo.highestYPos; //roomInfo.highestYPos
            } else {
                return currentBlock.relative(Direction.DOWN);
            }
        }
    }

    private List<BlockPos> getYLayer(int yLayer) {
        List<BlockPos> blocksInYLayer = new ArrayList<>();
        for (BlockPos pos : roomInfo.roomBlocks) {
            if (pos.getY() == yLayer) {
                blocksInYLayer.add(pos);
            }
        }

        return blocksInYLayer;
    }

    private boolean filledLayer(Level level, List<BlockPos> yLayerBlocks) {
        for (BlockPos pos : yLayerBlocks) {
            if (fillableBlock(level, pos)) {
                return false;
            }
        }

        return true;
    }

    private boolean isBlockReachable(Level level, BlockPos targetPos){
        for (Direction direction : Direction.values()) {
            if (level.getBlockState(targetPos.relative(direction)) == ModBlocks.DARKNESS.get().defaultBlockState()) {
                return true;
            }
        }
        return false;
    }

    private BlockPos getClosestUnfilledInLayer(Level level, List<BlockPos> yLayerBlocks) {
        List<BlockPos> availableBlockList = yLayerBlocks.stream()
                .filter((blockPos -> fillableBlock(level, blockPos) && isBlockReachable(level, blockPos)))
                .sorted((a, b) -> (int) (a.distToCenterSqr(currentBlock.getCenter()) - b.distToCenterSqr(currentBlock.getCenter())))
                .limit(4)
                .toList();

        if (availableBlockList.isEmpty()) {
            return null;
        } else {
            return availableBlockList.get(randInt(0, availableBlockList.size() - 1));
        }
    }

    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        tag.put(ROOM_INFO, roomInfo.save());
        tag.putInt(TICKS_ALIVE, ticksAlive);

        return tag;
    }

    public static DarkFountain load(CompoundTag tag) {
        RoomScanner.ScanResult roomInfo =
                RoomScanner.ScanResult.load(tag.getCompound(ROOM_INFO));

        return new DarkFountain(roomInfo, tag.getInt(TICKS_ALIVE));
    }

    static Random rand = new Random();
    public static int randInt(int min, int max) {
        return rand.nextInt((max - min) + 1) + min;
    }

    public boolean hasBlock(BlockPos pos) {
        return roomInfo.roomBlocks.contains(pos);
    }
}
