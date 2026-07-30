package com.vladilima.vladmod.darkworld;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.darkworld.generators.Generator;
import com.vladilima.vladmod.fountain.FountainManager;
import com.vladilima.vladmod.fountain.RoomScanner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class DarkWorld {
    public final BoundingBox boundingBox;

    public DarkWorld(BoundingBox boundingBox) {
        this.boundingBox = boundingBox;
    }

    public static final int DARK_WORLD_SIZE = 16;

    public static DarkWorld buildDarkWorld(Level level, RoomScanner.ScanResult roomInfo) {
        Level darkWorldLevel = Objects.requireNonNull(level.getServer()).getLevel(DimensionManager.DARK_WORLD);
        assert darkWorldLevel != null;

        List<BlockPos> darkWorldFloorBlocks = new ArrayList<>(Collections.emptyList());
        // Create Shape of Dark World by expanding floor of LW Room
        for (BlockPos block : getFloorOfLWRoom(roomInfo)) {
            BlockPos relativeToFountain = block.subtract(roomInfo.originPos).multiply(DARK_WORLD_SIZE).atY(1);

            AABB floor = AABB.ofSize(
                    relativeToFountain.getCenter(),
                    GenerationUtils.randInt(DARK_WORLD_SIZE, (int) (DARK_WORLD_SIZE * 1.5)),
                    .5,
                    GenerationUtils.randInt(DARK_WORLD_SIZE, (int) (DARK_WORLD_SIZE * 1.5))
            );
            BlockPos.betweenClosedStream(floor)
                    .forEach(blockPos -> darkWorldFloorBlocks.add(blockPos.immutable()));
        }

        // Dark World Area Blocks Relative to Fountain
        List<BlockPos> darkWorldAreaRelative = darkWorldFloorBlocks.stream().distinct().toList();

        Optional<BoundingBox> boundingBox = BoundingBox.encapsulatingPositions(darkWorldAreaRelative);
        BoundingBox finalBoundingBox = getNewDarkWorldLocation(level, boundingBox.orElseThrow());
        Registry<DarkWorldTheme> darkWorldThemeRegistry = level.registryAccess().registryOrThrow(DarkWorldTheme.REGISTRY_KEY);

        // Placeholder Theme + Generator Get (REPLACE WITH THEME CALCULATION)
        DarkWorldTheme theme = darkWorldThemeRegistry.getOptional(ResourceLocation.fromNamespaceAndPath(VladMod.MOD_ID, "cliffs")).orElseThrow();
        Generator generator = getGeneratorFromTheme(theme).orElseThrow();

        // Definitive In-Game Dark World Area Blocks
        List<BlockPos> darkWorldArea = darkWorldAreaRelative.stream()
                .map((blockPos -> blockPos.offset(finalBoundingBox.getCenter()).atY(1))).toList();

        generator.empty(darkWorldLevel, darkWorldArea);
        generator.surface(darkWorldLevel, darkWorldArea);

        return new DarkWorld(finalBoundingBox);
    }

    private static BoundingBox getNewDarkWorldLocation(Level level, BoundingBox newDWBoundingBox) {
        while (!isLocationValid(level, newDWBoundingBox)) {
            newDWBoundingBox = newDWBoundingBox.moved(64, 0, 64);
        }
        return newDWBoundingBox;
    }

    private static boolean isLocationValid(Level level, BoundingBox newDWBoundingBox) {
        List<DarkWorld> darkWorlds = FountainManager.getFountains(level).stream()
                .filter((fountain) -> fountain.darkWorld != null)
                .map((fountain) -> fountain.darkWorld).toList();
        for (DarkWorld darkWorld : darkWorlds) {
            if (darkWorld.boundingBox.intersects(newDWBoundingBox.inflatedBy(128, 0, 128))) {
                return false;
            };
        }
        return true;
    }

    private static List<BlockPos> getFloorOfLWRoom(RoomScanner.ScanResult roomInfo) {
        List<BlockPos> floorBlocks = new ArrayList<>();
        for (BlockPos pos : roomInfo.wallBlocks) {
            if (roomInfo.roomBlocks.contains(pos.above())) {
                floorBlocks.add(pos);
            }
        }

        return floorBlocks.stream().map(blockPos -> blockPos.atY(roomInfo.lowestYPos.getY())).toList();
    }

    private static Optional<Generator> getGeneratorFromTheme(DarkWorldTheme theme) {
        return Optional.ofNullable(DarkWorldGenerators.GENERATORS.get(theme.generator()));
    }

    private static void debugEmptyDWArea(Level darkWorldLevel, List<BlockPos> darkWorldArea) {
        VladMod.LOGGER.debug("Started Placing Blocks.");
        AtomicInteger i = new AtomicInteger();

        darkWorldArea.forEach(blockPos -> {
            LevelChunk levelChunk = darkWorldLevel.getChunkAt(blockPos);
            for (int y = 1; y < darkWorldLevel.getMaxBuildHeight(); y++) {
                i.addAndGet(1);
                GenerationUtils.setBlockAtChunkSection(
                        levelChunk,
                        blockPos.atY(y),
                        y <= 63 ? Blocks.IRON_BLOCK.defaultBlockState() : Blocks.AIR.defaultBlockState()
                );
            }
        });

        VladMod.LOGGER.debug("Finished Placing Blocks. Placed {} Blocks.", i);
    }
}
