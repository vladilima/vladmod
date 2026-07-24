package com.vladilima.vladmod.registries;

import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.blocks.entity.renderer.DarknessBlockEntityRenderer;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

public class ModBlockEntityRenderers {
    public static void register(EntityRenderersEvent.RegisterRenderers event) {
        VladMod.LOGGER.info("Registering Block Entity Renderers (Clientside)...");
        event.registerBlockEntityRenderer(ModBlockEntities.DARKNESS_BLOCK_ENTITY.get(), DarknessBlockEntityRenderer::new);
    }
}
