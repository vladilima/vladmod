package com.vladilima.vladmod.darkworld;

import com.vladilima.vladmod.VladMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;

public class DimensionManager {

    public static final ResourceLocation DARK_WORLD_ID = ResourceLocation.fromNamespaceAndPath(VladMod.MOD_ID, "dark_world");

    public static final ResourceKey<Level> DARK_WORLD = ResourceKey.create(Registries.DIMENSION, DARK_WORLD_ID);
    protected static final ResourceKey<LevelStem> DARK_WORLD_DIMENSION = ResourceKey.create(Registries.LEVEL_STEM, DARK_WORLD_ID);
    protected static final ResourceKey<Biome> DARK_WORLD_BIOME = ResourceKey.create(Registries.BIOME, DARK_WORLD_ID);
    public static final ResourceKey<DimensionType> DARK_WORLD_DIMENSION_TYPE = ResourceKey.create(Registries.DIMENSION_TYPE, DARK_WORLD_ID);

}
