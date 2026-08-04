package com.vladilima.vladmod.registries;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.items.ExpBerryItem;
import com.vladilima.vladmod.items.FountainMakerItem;
import com.vladilima.vladmod.items.NetherStarArrowItem;
import com.vladilima.vladmod.items.StandArrowItem;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.lodestar.lodestone.modules.toolkit.item.LodestoneItemProperties;

import java.util.function.Function;
import java.util.function.Supplier;

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

    public static <T extends Item> DeferredItem<T> registerModBlock(String name, Supplier<LodestoneItemProperties> propertySupplier, Function<LodestoneItemProperties, T> function) {
        return ITEMS.register(name, () -> {
            var properties = propertySupplier.get();
            return function.apply(properties);
        });
    }
}
