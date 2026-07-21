package com.vladilima.vladmod.entity;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.entity.custom.StarPlatinum;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, VladMod.MOD_ID);

    public static final Supplier<EntityType<StarPlatinum>> STAR_PLATINUM =
            ENTITY_TYPES.register("star_platinum", () -> EntityType.Builder.of(StarPlatinum::new, MobCategory.MISC)
                    .sized(.75f, 1.85f).build("star_platinum"));

    public static void register(IEventBus eventBus) {
        VladMod.LOGGER.info("Registering Entities...");
        ENTITY_TYPES.register(eventBus);
    }
}
