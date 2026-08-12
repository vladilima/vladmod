package com.vladilima.vladmod.datagen;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.registries.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, VladMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.EXP_BERRY.get());
        basicItem(ModItems.NETHER_STAR_ARROW.get());
        basicItem(ModItems.STAND_ARROW.get());

        basicItem(ModItems.WOBBLY_THING.get());
    }
}
