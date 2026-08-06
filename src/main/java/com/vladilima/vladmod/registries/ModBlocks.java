package com.vladilima.vladmod.registries;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.blocks.DarknessBlock;
import com.vladilima.vladmod.blocks.ExpBerryBushBlock;
import com.vladilima.vladmod.blocks.great_door.GreatDoorComponentBlock;
import com.vladilima.vladmod.blocks.great_door.GreatDoorCoreBlock;
import com.vladilima.vladmod.blocks.great_door.GreatDoorStructure;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.lodestar.lodestone.modules.toolkit.block.BlockBlockItemHolder;
import team.lodestar.lodestone.modules.toolkit.item.LodestoneItemProperties;
import team.lodestar.lodestone.modules.toolkit.multiblock.MultiBlockItem;
import team.lodestar.lodestone.modules.toolkit.multiblock.MultiBlockStructure;

import java.util.function.BiFunction;
import java.util.function.Supplier;

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

    private static final BlockBehaviour.Properties GREAT_DOOR_PROPERTIES = BlockBehaviour.Properties.of().strength(-1, 3600000.0F).pushReaction(PushReaction.BLOCK).noTerrainParticles().noLootTable().sound(SoundType.EMPTY).noOcclusion();
    public static final BlockBlockItemHolder<Block, MultiBlockItem> GREAT_DOOR = registerMultiBlock(
            "great_door",
            () -> new GreatDoorCoreBlock<>(GREAT_DOOR_PROPERTIES),
            GreatDoorStructure.STRUCTURE);
    public static final DeferredHolder<Block, GreatDoorComponentBlock> GREAT_DOOR_COMPONENT = BLOCKS.register(
            "great_door_component",
            () -> new GreatDoorComponentBlock(GREAT_DOOR_PROPERTIES.lootFrom(GREAT_DOOR)));


    public static void register(IEventBus eventBus) {
        VladMod.LOGGER.info("Registering Blocks...");
        BLOCKS.register(eventBus);
    }

    public static <T extends Block> BlockBlockItemHolder<T, MultiBlockItem> registerMultiBlock(String name, Supplier<T> supplier, Supplier<? extends MultiBlockStructure> structure) {
        return registerBlock(name, name, supplier, (b, p) -> new MultiBlockItem(b, p, structure));
    }

    public static <T extends Block, K extends BlockItem> BlockBlockItemHolder<T, K> registerBlock(String blockName, String itemName, Supplier<T> blockSupplier, BiFunction<Block, LodestoneItemProperties, K> itemSupplier) {
        var block = BLOCKS.register(blockName, blockSupplier);
        var item = ModItems.registerModBlock(itemName, LodestoneItemProperties::new, p -> itemSupplier.apply(block.get(), p));
        return new BlockBlockItemHolder<>(block, item);
    }
}
