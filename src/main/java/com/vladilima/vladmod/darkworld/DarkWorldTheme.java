package com.vladilima.vladmod.darkworld;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.vladilima.vladmod.VladMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record DarkWorldTheme(float requiredWeight, List<BlockTags> blockTags) {
    public static final ResourceKey<Registry<DarkWorldTheme>> REGISTRY_KEY = ResourceKey.createRegistryKey(
            ResourceLocation.fromNamespaceAndPath(
                    VladMod.MOD_ID, "dark_world/theme"
            ));

    public static final Codec<DarkWorldTheme> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.FLOAT.fieldOf("required_weight").forGetter(DarkWorldTheme::requiredWeight),
            BlockTags.CODEC.listOf().fieldOf("block_tags").forGetter(DarkWorldTheme::blockTags)
    ).apply(instance, DarkWorldTheme::new));

    public record BlockTags(ResourceLocation tag, float weight) {
        public static final Codec<BlockTags> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("tag").forGetter(BlockTags::tag),
                Codec.FLOAT.fieldOf("weight").forGetter(BlockTags::weight)
        ).apply(instance, BlockTags::new));
    }
}
