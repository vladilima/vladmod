package com.vladilima.vladmod.fountain;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.registries.ModAttachmentTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public class FountainManager {

    public static ArrayList<DarkFountain> darkFountains;

    public static void makeFountain(Level level, BlockPos startingPos) {
        RoomScanner.ScanResult scanResult = RoomScanner.scan(level, startingPos, false, List.of());
        if (scanResult != null && !scanResult.roomBlocks.isEmpty()) {
            for (DarkFountain fountain : darkFountains) {
                if (scanResult.roomBlocks.contains(fountain.fountainPos)) {
                    VladMod.LOGGER.error("Attempted to create a Dark Fountain inside a room that already contains a dark fountain.");
                    return;
                }
            }

            darkFountains.add(new DarkFountain(scanResult));
        } else {
            VladMod.LOGGER.error("Invalid space for a Dark Fountain.");
        }
    }

    public static void nullFountain(DarkFountain fountain) {
        int fountainIndex = darkFountains.indexOf(fountain);
        darkFountains.set(fountainIndex, null);
    }

    public static void save(MinecraftServer server) {
        Level overworldLevel = server.getLevel(Level.OVERWORLD);
        assert overworldLevel != null;

        overworldLevel.setData(ModAttachmentTypes.DARK_FOUNTAINS, darkFountains);
        VladMod.LOGGER.info("Saved Dark Fountains: " + overworldLevel.getData(ModAttachmentTypes.DARK_FOUNTAINS));
    }

    public static void load(MinecraftServer server) {
        Level overworldLevel = server.getLevel(Level.OVERWORLD);
        assert overworldLevel != null;

        darkFountains = new ArrayList<>(overworldLevel.getData(ModAttachmentTypes.DARK_FOUNTAINS));
        VladMod.LOGGER.info("Loaded Dark Fountains: " + FountainManager.darkFountains);
    }
}
