package com.vladilima.vladmod.blocks.entity;

import com.mojang.datafixers.types.Type;
import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.blocks.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.HitResult;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, VladMod.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DarknessBlockEntity>> DARKNESS_BLOCK_ENTITY =
            BLOCK_ENTITIES.register(
                    "darkness",
                    () -> BlockEntityType.Builder.of(
                            DarknessBlockEntity::new,
                            ModBlocks.DARKNESS.get()
                    ).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
