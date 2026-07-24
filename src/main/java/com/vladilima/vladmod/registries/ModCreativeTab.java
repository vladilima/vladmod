package com.vladilima.vladmod.registries;

import com.vladilima.vladmod.VladMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModCreativeTab {

    // Create a Deferred Register to hold CreativeModeTabs which will all be registered under the "vladmod" namespace
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, VladMod.MOD_ID);

    // Creates a creative tab with the id "vladmod:example_tab" for the example item, that is placed after the combat tab
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TABS.register("example_tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.vladmod")) //The language key for the title of your CreativeModeTab
            .withTabsBefore(CreativeModeTabs.COMBAT)
            .icon(() -> ModItems.EXP_BERRY.get().getDefaultInstance())
            .displayItems((parameters, output) -> {
                output.accept(ModItems.EXP_BERRY.get()); // Add the example item to the tab. For your own tabs, this method is preferred over the event
                output.accept(ModItems.NETHER_STAR_ARROW.get());
                output.accept(ModItems.STAND_ARROW.get());
            }).build());


//    // Add the example block item to the building blocks tab
//    private void addCreative(BuildCreativeModeTabContentsEvent event) {
//        if (event.getTabKey() == CreativeModeTabs.BUILDING_BLOCKS) {
//            event.accept(EXAMPLE_BLOCK_ITEM);
//        }
//    }

    public static void register(IEventBus eventBus) {
        VladMod.LOGGER.info("Registering Creative Tabs...");
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
