package com.vladilima.vladmod.blocks;

import com.vladilima.vladmod.VladMod;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(VladMod.MOD_ID);

    public static final DeferredBlock<Block> EXP_BERRY_BUSH = BLOCKS.registerBlock(
            "exp_berry_bush",
                ExpBerryBushBlock::new,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .randomTicks()
                        .noCollission()
                        .sound(SoundType.SWEET_BERRY_BUSH)
                        .pushReaction(PushReaction.DESTROY)
    );

    public static final DeferredBlock<Block> DARKNESS = BLOCKS.registerBlock(
            "darkness",
            DarknessBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_BLACK)
                    .noOcclusion()
                    .noCollission()
                    .noLootTable()
                    .noTerrainParticles()
                    .air()
                    .replaceable()
    );

    public static void register(IEventBus eventBus) {
        VladMod.LOGGER.info("Registering Blocks...");
        BLOCKS.register(eventBus);
    }
}
