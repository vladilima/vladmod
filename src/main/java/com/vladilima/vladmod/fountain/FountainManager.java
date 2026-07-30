package com.vladilima.vladmod.fountain;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.attachments.DarkFountainsAttachment;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FountainManager {

    public static void makeFountain(Level level, BlockPos startingPos) {
        Level overworldLevel = Objects.requireNonNull(level.getServer()).getLevel(Level.OVERWORLD);

        RoomScanner.ScanResult scanResult = RoomScanner.scan(level, startingPos, false, List.of());
        if (scanResult != null && !scanResult.roomBlocks.isEmpty()) {
            ArrayList<DarkFountain> darkFountains = overworldLevel.getData(DarkFountainsAttachment.TYPE);

            for (DarkFountain fountain : darkFountains) {
                if (scanResult.roomBlocks.contains(fountain.FOUNTAIN_POS)) {
                    VladMod.LOGGER.error("Attempted to create a Dark Fountain inside a room that already contains a dark fountain.");
                    return;
                }
            }

            darkFountains.add(new DarkFountain(scanResult));
            overworldLevel.setData(DarkFountainsAttachment.TYPE, darkFountains);
        } else {
            VladMod.LOGGER.error("Invalid space for a Dark Fountain.");
        }
    }

    public static void removeFountain(Level level, DarkFountain fountain) {
        Level overworldLevel = Objects.requireNonNull(level.getServer()).getLevel(Level.OVERWORLD);

        ArrayList<DarkFountain> darkFountains = overworldLevel.getData(DarkFountainsAttachment.TYPE);
        int fountainIndex = darkFountains.indexOf(fountain);
        darkFountains.set(fountainIndex, null);

        overworldLevel.setData(DarkFountainsAttachment.TYPE, darkFountains);
    }

    public static List<DarkFountain> getFountains(Level level) {
        Level overworldLevel = Objects.requireNonNull(level.getServer()).getLevel(Level.OVERWORLD);

        return overworldLevel.getData(DarkFountainsAttachment.TYPE);
    }
}
