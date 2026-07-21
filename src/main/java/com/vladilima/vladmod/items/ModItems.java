package com.vladilima.vladmod.items;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.blocks.ModBlocks;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(VladMod.MOD_ID);

    public static final DeferredItem<Item> EXP_BERRY = ITEMS.register("exp_berry",
            () -> new ExpBerryItem(ModBlocks.EXP_BERRY_BUSH.get(),
                    new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationModifier(0.5F).alwaysEdible().fast().build())));

    public static final DeferredItem<Item> NETHER_STAR_ARROW = ITEMS.register("nether_star_arrow",
            () -> new NetherStarArrowItem(new Item.Properties()));

    public static final DeferredItem<Item> STAND_ARROW = ITEMS.register("stand_arrow",
            () -> new StandArrowItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<Item> FOUNTAIN_MAKER = ITEMS.register("fountain_maker",
            () -> new FountainMakerItem(new Item.Properties().stacksTo(1)));


    public static void register(IEventBus eventBus) {
        VladMod.LOGGER.info("Registering Items...");
        ITEMS.register(eventBus);
    }
}
