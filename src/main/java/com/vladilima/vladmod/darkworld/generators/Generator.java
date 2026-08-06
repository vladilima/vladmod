package com.vladilima.vladmod.darkworld.generators;

import com.vladilima.vladmod.fountain.DarkFountain;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class Generator {
    public abstract void empty(Level level, List<BlockPos> generationArea);

    public abstract void surface(Level level, List<BlockPos> generationArea);

    public abstract List<BlockPos> features(Level level, List<BlockPos> generationArea, DarkFountain fountainInfo);

    public abstract void entities(Level level, List<BlockPos> generationArea);
}
