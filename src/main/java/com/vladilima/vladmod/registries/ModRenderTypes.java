package com.vladilima.vladmod.registries;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class ModRenderTypes extends RenderType {
    private static RenderType DARKNESS_BLOCK_RENDER_TYPE;

    public ModRenderTypes(String name, VertexFormat format, VertexFormat.Mode mode, int bufferSize, boolean affectsCrumbling, boolean sortOnUpload, Runnable setupState, Runnable clearState) {
        super(name, format, mode, bufferSize, affectsCrumbling, sortOnUpload, setupState, clearState);
    }

    public static RenderType darknessBlock(Supplier<ShaderInstance> shaderSupplier) {
        if (DARKNESS_BLOCK_RENDER_TYPE == null) {
            DARKNESS_BLOCK_RENDER_TYPE = RenderType.create(
                    "darkness_block",
                    DefaultVertexFormat.POSITION_COLOR,
                    VertexFormat.Mode.QUADS,
                    256,
                    false,
                    true,
                    RenderType.CompositeState.builder()
                            .setShaderState(new RenderStateShard.ShaderStateShard(shaderSupplier))
                            .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
                            .createCompositeState(false)
            );
        }
        return DARKNESS_BLOCK_RENDER_TYPE;
    }

    public static RenderType fountain(ResourceLocation rl) {
        return create("fountain", DefaultVertexFormat.POSITION_TEX, VertexFormat.Mode.QUADS, 256,
                false, false,
                RenderType.CompositeState.builder()
                        .setShaderState(RenderStateShard.POSITION_TEX_SHADER)
                        .setTextureState(new TextureStateShard(rl, false, false))
                        .setTransparencyState(TRANSLUCENT_TRANSPARENCY)
                        .setCullState(CULL)
                        .createCompositeState(true));
    }
}
