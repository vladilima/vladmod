package com.vladilima.vladmod.datagen;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.blocks.ExpBerryBushBlock;
import com.vladilima.vladmod.registries.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, VladMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        makeBush(((SweetBerryBushBlock) ModBlocks.EXP_BERRY_BUSH.get()), "exp_berry_bush_stage", "exp_berry_bush_stage");

        simpleBlock(ModBlocks.DARKNESS.get());
    }


    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    private void makeBush(SweetBerryBushBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> states(state, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] states(BlockState state, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().cross(modelName + state.getValue(ExpBerryBushBlock.AGE),
                ResourceLocation.fromNamespaceAndPath(VladMod.MOD_ID, "block/" + textureName + state.getValue(ExpBerryBushBlock.AGE))).renderType("cutout"));

        return models;
    }
}
