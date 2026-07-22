package com.vladilima.vladmod.darkworld;

import com.vladilima.vladmod.fountain.RoomScanner;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.Objects;

public class DarkWorld {
    public static void buildDarkWorld(Level level, RoomScanner.ScanResult roomInfo) {
        Level darkWorldLevel = Objects.requireNonNull(level.getServer()).getLevel(DimensionManager.DARK_WORLD);
        assert darkWorldLevel != null;

        for (BlockPos pos : roomInfo.wallBlocks) {
            darkWorldLevel.setBlockAndUpdate(pos, level.getBlockState(pos));
        }
    }
}
