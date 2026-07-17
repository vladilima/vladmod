package com.vladilima.vladmod.fountain;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.attachments.DarkFountainsAttachment;
import com.vladilima.vladmod.blocks.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Objects;

public class FountainManager {

    public static void makeFountain(Level level, BlockPos startingPos) {
        RoomScanner.ScanResult scanResult = RoomScanner.scan(level, startingPos);
        if (scanResult != null && !scanResult.roomBlocks.isEmpty()) {
            ArrayList<DarkFountain> darkFountains = level.getData(DarkFountainsAttachment.TYPE);

            for (DarkFountain fountain : darkFountains) {
                if (scanResult.roomBlocks.contains(fountain.FOUNTAIN_POS)) {
                    VladMod.LOGGER.error("Attempted to create a Dark Fountain inside a room that already contains a dark fountain.");
                    return;
                }
            }

            darkFountains.add(new DarkFountain(scanResult));
            level.setData(DarkFountainsAttachment.TYPE, darkFountains);
        } else {
            VladMod.LOGGER.error("Invalid space for a Dark Fountain.");
        }
    }

    public static void removeFountain(Level level, DarkFountain fountain) {
        ArrayList<DarkFountain> darkFountains = level.getData(DarkFountainsAttachment.TYPE);
        int fountainIndex = darkFountains.indexOf(fountain);
        darkFountains.set(fountainIndex, null);

        level.setData(DarkFountainsAttachment.TYPE, darkFountains);
    }
}
