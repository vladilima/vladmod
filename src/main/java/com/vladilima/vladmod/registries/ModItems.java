package com.vladilima.vladmod.registries;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.items.FountainMakerItem;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.lodestar.lodestone.modules.toolkit.item.LodestoneItemProperties;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(VladMod.MOD_ID);

    public static final DeferredItem<Item> FOUNTAIN_MAKER = ITEMS.register("fountain_maker",
            () -> new FountainMakerItem(new Item.Properties().stacksTo(1)));

    public static final DeferredItem<BlockItem> WOBBLY_THING = ITEMS.registerSimpleBlockItem(ModBlocks.WOBBLY_THING);


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
