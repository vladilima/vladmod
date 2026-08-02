package com.vladilima.vladmod.fountain;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.darkworld.GenerationUtils;
import com.vladilima.vladmod.registries.ModBlocks;
import com.vladilima.vladmod.blocks.entity.DarknessBlockEntity;
import com.vladilima.vladmod.darkworld.DarkWorld;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DarkFountain {
    RoomScanner.ScanResult roomInfo;
    public final ResourceKey<Level> fountainDimension;
    public final BlockPos fountainPos;

    private BlockPos currentBlock;
    List<BlockPos> roomBreaches = new ArrayList<>();

    public boolean isFilled = false;
    private int ticksAlive = 0;

    public DarkWorld darkWorld;

    DarkFountain (RoomScanner.ScanResult scan) {
        this.roomInfo = scan;
        this.fountainPos = scan.originPos;
        this.fountainDimension = scan.dimension;

        this.currentBlock = fountainPos;
    }

    // Load Dark Fountain
    public DarkFountain (RoomScanner.ScanResult roomInfo, BlockPos currentBlock, int ticksAlive, boolean isFilled, Optional<DarkWorld> darkWorld) {
        this.roomInfo = roomInfo;
        this.fountainPos = roomInfo.originPos;
        this.fountainDimension = roomInfo.dimension;

        this.currentBlock = currentBlock;
        this.ticksAlive = ticksAlive;
        this.isFilled = isFilled;
        this.darkWorld = darkWorld.orElse(null);
    }

    private static final int DARKNESS_SPREAD_DELAY = 5;
    private int ticksToSpread = 0;
    public void tick(Level level) {
        if (this.ticksAlive == 0) {
            setInitialPosition(level);
        }

        if (ticksToSpread <= 0) {
            if (!isRoomFilled(level)) {
                darknessSpread(level);
            } else if (!this.isFilled) { // Room filled for the first time
                System.out.println("Filled Fountain Room");
                this.isFilled = true;
                this.darkWorld = DarkWorld.buildDarkWorld(level, roomInfo);
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

    private void setInitialPosition(Level level) {
        BlockPos startPos = this.fountainPos;
        BlockPos blockAbove = startPos.relative(Direction.UP);
        while (level.isEmptyBlock(blockAbove)) {
            startPos = blockAbove;
            blockAbove = startPos.relative(Direction.UP);
        }

        // Place initial darkness block
        createDarkness(level, startPos);
    }

    private boolean isRoomFilled(Level level) {
        boolean allFillable = true;
        for (BlockPos blockPos : roomInfo.roomBlocks){
            if (fillableBlock(level, blockPos)) {
                allFillable = false;
            } else if (isSolid(level, blockPos)) {
                RoomScanner.ScanResult roomScan = RoomScanner.scan(level, this.fountainPos, false, roomBreaches);
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

        return allFillable;
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
            int attempts = 0; // Attempts at finding reachable block
            while (!fillableBlock(level, currentBlock) || !isBlockReachable(level, currentBlock)) {
                currentBlock = getNextBlock(level);
                attempts++;
                if (attempts >= 50) {
                    FountainManager.nullFountain(this);
                    VladMod.LOGGER.error("Exceeded Darkness Spread attempts, deleting Fountain.");
                    return;
                }
            }

            if (roomInfo.roomBlocks.contains(currentBlock)) {
                createDarkness(level, currentBlock);
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
                        possibleBlocks.get(GenerationUtils.randInt(0, possibleBlocks.size() - 1));
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

    private void createDarkness(Level level, BlockPos targetPos) {
        if (level.setBlockAndUpdate(targetPos, ModBlocks.DARKNESS.get().defaultBlockState())) {
            DarknessBlockEntity newDarkness = (DarknessBlockEntity) level.getBlockEntity(targetPos);
            if (newDarkness != null) {
                newDarkness.fountain = this;
            }
        }
    }

    // Sets the Fountain parameter for Darkness blocks within room
    public static void loadDarkness(MinecraftServer server, DarkFountain fountain) {
        Level level = server.getLevel(fountain.fountainDimension);

        for (BlockPos blockPos : fountain.roomInfo.roomBlocks) {
            if (level.getBlockState(blockPos) == ModBlocks.DARKNESS.get().defaultBlockState()) {
                DarknessBlockEntity existingDarkness = (DarknessBlockEntity) level.getBlockEntity(blockPos);
                if (existingDarkness != null) {
                    existingDarkness.fountain = fountain;
                }
            }
        }
    }

    public RoomScanner.ScanResult roomInfo() {
        return roomInfo;
    }

    public BlockPos currentBlock() {
        return currentBlock;
    }

    public int ticksAlive() {
        return ticksAlive;
    }

    public boolean isFilled() {
        return isFilled;
    }

    public boolean hasBlock(BlockPos pos) {
        return roomInfo.roomBlocks.contains(pos);
    }
}
