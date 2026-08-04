package com.vladilima.vladmod.registries;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.blocks.entity.DarknessBlockEntity;
import com.vladilima.vladmod.blocks.great_door.GreatDoorCoreBlockEntity;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntityTicker.Type;
import team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntityType;

import java.util.function.Supplier;

import static team.lodestar.lodestone.modules.toolkit.blockentity.LodestoneBlockEntityTypeBuilder.create;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, VladMod.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DarknessBlockEntity>> DARKNESS_BLOCK_ENTITY =
            BLOCK_ENTITIES.register(
                    "darkness",
                    () -> BlockEntityType.Builder.of(
                            DarknessBlockEntity::new,
                            ModBlocks.DARKNESS.get()
                    ).build(null));

    public static final Supplier<LodestoneBlockEntityType<GreatDoorCoreBlockEntity>> GREAT_DOOR =
            BLOCK_ENTITIES.register("great_door", () -> create(GreatDoorCoreBlockEntity::new, ModBlocks.GREAT_DOOR).setTickerType(Type.BOTH).build());


    public static void register(IEventBus eventBus) {
        VladMod.LOGGER.info("Registering Block Entities...");
        BLOCK_ENTITIES.register(eventBus);
    }
}
