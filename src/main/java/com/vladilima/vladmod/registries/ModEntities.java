package com.vladilima.vladmod.registries;

import com.vladilima.vladmod.VladMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, VladMod.MOD_ID);

    public static void register(IEventBus eventBus) {
        VladMod.LOGGER.info("Registering Entities...");
        ENTITY_TYPES.register(eventBus);
    }
}
