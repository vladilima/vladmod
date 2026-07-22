package com.vladilima.vladmod.darkworld;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.vladilima.vladmod.VladMod;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class DarkWorldTheme {
    public static final ResourceKey<Registry<DarkWorldTheme>> REGISTRY_KEY = ResourceKey.createRegistryKey(
            ResourceLocation.fromNamespaceAndPath(
                    VladMod.MOD_ID, "dark_world/theme"
            ));

    public DarkWorldTheme(String theme) {
        this.theme = theme;
    }

    public String theme;
    public String theme() {
        return theme;
    };

    public static final Codec<DarkWorldTheme> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("theme").forGetter(DarkWorldTheme::theme)
    ).apply(instance, DarkWorldTheme::new));
}
