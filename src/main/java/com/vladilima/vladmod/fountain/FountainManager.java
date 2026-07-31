package com.vladilima.vladmod.fountain;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.registries.ModAttachmentTypes;
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
            List<DarkFountain> darkFountains = getFountains(level);

            for (DarkFountain fountain : darkFountains) {
                if (scanResult.roomBlocks.contains(fountain.fountainPos)) {
                    VladMod.LOGGER.error("Attempted to create a Dark Fountain inside a room that already contains a dark fountain.");
                    return;
                }
            }

            darkFountains.add(new DarkFountain(scanResult));
            overworldLevel.setData(ModAttachmentTypes.DARK_FOUNTAINS, darkFountains);
        } else {
            VladMod.LOGGER.error("Invalid space for a Dark Fountain.");
        }
    }

    public static void nullFountain(Level level, DarkFountain fountain) {
        List<DarkFountain> darkFountains = getFountains(level);
        int fountainIndex = darkFountains.indexOf(fountain);
        darkFountains.set(fountainIndex, null);

        setFountains(level, darkFountains);
    }

    public static void removeNull(Level level) {
        List<DarkFountain> darkFountains = getFountains(level);
        darkFountains.remove(null);

        setFountains(level, darkFountains);
    }

    public static List<DarkFountain> getFountains(Level level) {
        if (level.getServer() == null) {
            return List.of();
        }
        Level overworldLevel = level.getServer().getLevel(Level.OVERWORLD);

        return new ArrayList<>(List.copyOf(overworldLevel.getData(ModAttachmentTypes.DARK_FOUNTAINS)));
    }

    public static void setFountains(Level level, List<DarkFountain> newFountainList) {
        if (level.getServer() == null) {
            return;
        }
        Level overworldLevel = level.getServer().getLevel(Level.OVERWORLD);
        assert overworldLevel != null;

        overworldLevel.setData(ModAttachmentTypes.DARK_FOUNTAINS, newFountainList);
    }
}
