package com.vladilima.vladmod.darkworld;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.fountain.RoomScanner;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;
import java.util.Optional;

public record DarkWorldTheme(String generator, float requiredWeight, List<BlockTags> blockTags) {
    public static final ResourceKey<Registry<DarkWorldTheme>> REGISTRY_KEY = ResourceKey.createRegistryKey(
            ResourceLocation.fromNamespaceAndPath(
                    VladMod.MOD_ID, "dark_world/theme"
            ));

    public static final Codec<DarkWorldTheme> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("generator").forGetter(DarkWorldTheme::generator),
            Codec.FLOAT.fieldOf("required_weight").forGetter(DarkWorldTheme::requiredWeight),
            BlockTags.CODEC.listOf().fieldOf("block_tags").forGetter(DarkWorldTheme::blockTags)
    ).apply(instance, DarkWorldTheme::new));

    @Override
    public String toString() {
        return "DarkWorldTheme{" +
                "requiredWeight=" + requiredWeight +
                ", generator='" + generator + '\'' +
                '}';
    }

    public record BlockTags(ResourceLocation tag, float weight) {
        public static final Codec<BlockTags> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                ResourceLocation.CODEC.fieldOf("tag").forGetter(BlockTags::tag),
                Codec.FLOAT.fieldOf("weight").forGetter(BlockTags::weight)
        ).apply(instance, BlockTags::new));
    }

    public static DarkWorldTheme calculateTheme(Level level, RoomScanner.ScanResult roomInfo){
        Registry<DarkWorldTheme> darkWorldThemeRegistry = level.registryAccess().registryOrThrow(DarkWorldTheme.REGISTRY_KEY);
        final DarkWorldTheme DEFAULT = darkWorldThemeRegistry.getOptional(ResourceLocation.fromNamespaceAndPath(VladMod.MOD_ID, "cliffs")).orElseThrow();

        float chosenWeight = 0f;
        Optional<DarkWorldTheme> chosenTheme = Optional.empty();
        for (DarkWorldTheme theme : darkWorldThemeRegistry.stream().toList()) {
            float currentWeight = 0f;
            for (BlockPos wallBlock : roomInfo.wallBlocks) {
                Optional<Float> blockWeight = theme.blockTags().stream()
                        .filter((bt) -> {
                            TagKey<Block> tag = TagKey.create(Registries.BLOCK, bt.tag());
                            return level.getBlockState(wallBlock).is(tag);
                        })
                        .map(bt -> bt.weight).max((bt1, bt2) -> (int) (bt1 - bt2));

                currentWeight += blockWeight.orElse(0f);
            }

            if (currentWeight >= theme.requiredWeight && currentWeight > chosenWeight) {
                chosenWeight = currentWeight;
                chosenTheme = Optional.of(theme);
            }
        }

        VladMod.LOGGER.info("Chosen Theme: {}", chosenTheme.orElse(null));
        return chosenTheme.orElse(DEFAULT);
    }
}
