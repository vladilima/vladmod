package com.vladilima.vladmod.darkworld.generators;

import com.vladilima.vladmod.fountain.DarkFountain;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

import java.util.List;

public abstract class Generator {
    public abstract GenerationInfo empty(Level level, List<BlockPos> generationArea, DarkFountain darkFountain);

    public abstract GenerationInfo surface(Level level, GenerationInfo generationInfo);

    public abstract GenerationInfo features(Level level, GenerationInfo generationInfo);

    public abstract void entities(Level level, GenerationInfo generationInfo);


    public class GenerationInfo {
        List<BlockPos> generationArea;
        List<BlockPos> surfaceBlocks;
        public List<BlockPos> greatDoors;
        DarkFountain fountain;

        public GenerationInfo(List<BlockPos> generationArea, DarkFountain fountain) {
            this.generationArea = generationArea;
            this.fountain = fountain;
        }
    }
}
