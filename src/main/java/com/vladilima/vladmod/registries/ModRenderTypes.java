package com.vladilima.vladmod.registries;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;

import java.util.function.Supplier;

public class ModRenderTypes {
    private static RenderType DARKNESS_BLOCK_RENDER_TYPE;

    public static RenderType darknessBlock(Supplier<ShaderInstance> shaderSupplier) {
        if (DARKNESS_BLOCK_RENDER_TYPE == null) {
            DARKNESS_BLOCK_RENDER_TYPE = RenderType.create(
                    "darkness_block",
                    DefaultVertexFormat.POSITION_COLOR,
                    VertexFormat.Mode.QUADS,
                    1536,
                    false,
                    true,
                    RenderType.CompositeState.builder()
                            .setShaderState(new RenderStateShard.ShaderStateShard(() -> shaderSupplier.get()))
                            .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                            .setCullState(RenderStateShard.NO_CULL)
                            .createCompositeState(false)
            );
        }
        return DARKNESS_BLOCK_RENDER_TYPE;
    }
}
