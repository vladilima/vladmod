package com.vladilima.vladmod.registries;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.gui.textbox.TextboxMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModMenuTypes {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, VladMod.MOD_ID);

    public static final Supplier<MenuType<TextboxMenu>> TEXTBOX = MENU_TYPES.register("textbox", () -> new MenuType(TextboxMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static void register(IEventBus eventBus) {
        VladMod.LOGGER.info("Registering Menu Types...");
        MENU_TYPES.register(eventBus);
    }
}
