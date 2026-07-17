package com.vladilima.vladmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.entity.custom.StarPlatinum;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class StarPlatinumRenderer extends LivingEntityRenderer<StarPlatinum, StarPlatinumModel<StarPlatinum>> {

    public StarPlatinumRenderer(EntityRendererProvider.Context context) {
        super(context, new StarPlatinumModel<>(context.bakeLayer(StarPlatinumModel.LAYER_LOCATION)), .5f);
    }

    @Override
    public ResourceLocation getTextureLocation(StarPlatinum entity) {
        return ResourceLocation.fromNamespaceAndPath(VladMod.MOD_ID, "textures/entity/stand/star_platinum.png");
    }



    @Override
    public void render(StarPlatinum p_entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(p_entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    protected boolean shouldShowName(StarPlatinum entity) {
        return false;
    }
}

