package com.vladilima.vladmod.datagen;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.blocks.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, VladMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.BEE_GROWABLES)
                .add(ModBlocks.EXP_BERRY_BUSH.get());

        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.EXP_BERRY_BUSH.get());

        tag(BlockTags.SWORD_EFFICIENT)
                .add(ModBlocks.EXP_BERRY_BUSH.get());
    }
}
