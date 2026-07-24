package com.vladilima.vladmod.registries;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.vladilima.vladmod.VladMod;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterShadersEvent;

import java.io.IOException;

@EventBusSubscriber(modid = VladMod.MOD_ID, value = Dist.CLIENT)
public class ModShaders {
    private static ShaderInstance darknessBlockShader;

    @SubscribeEvent
    static void onRegisterShaders(RegisterShadersEvent event) throws IOException {
        ResourceProvider resourceProvider = event.getResourceProvider();

        try {
            darknessBlockShader = new ShaderInstance(
                    resourceProvider,
                    ResourceLocation.fromNamespaceAndPath(VladMod.MOD_ID, "darkness_block"),
                    DefaultVertexFormat.POSITION_COLOR
            );
        } catch (Exception e) {
            System.err.println("[VladMod] ERROR: Failed to load darkness_block shader!");
            e.printStackTrace();
        }

        if (darknessBlockShader != null) {
            event.registerShader(darknessBlockShader, shader -> darknessBlockShader = shader);
        }
    }

    public static ShaderInstance getDarknessBlockShader() {
        return darknessBlockShader;
    }
}
