package com.vladilima.vladmod;

import com.vladilima.vladmod.registries.ModEntities;
import com.vladilima.vladmod.entity.client.StarPlatinumModel;
import com.vladilima.vladmod.entity.custom.StarPlatinum;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = VladMod.MOD_ID)
public class ModEventBusEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(StarPlatinumModel.LAYER_LOCATION, StarPlatinumModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.STAR_PLATINUM.get(), StarPlatinum.createAttributes().build());
    }
}
