package com.vladilima.vladmod.event;

import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.darkworld.DarkWorldClientData;
import com.vladilima.vladmod.fountain.render.DarkFountainModel;
import com.vladilima.vladmod.registries.ModRenderTypes;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Quaternionf;

@EventBusSubscriber(modid = VladMod.MOD_ID, value = Dist.CLIENT)
public class ClientEvents {

    private static final ByteBufferBuilder FOUNTAIN_BUFFER = new ByteBufferBuilder(65536);
    private static final float COS45 = 0.70710678f;

    private static DarkFountainModel fountainModel;
    private static DarkFountainModel getFountainModel() {
        if (fountainModel == null)
            fountainModel = new DarkFountainModel(Minecraft.getInstance().getEntityModels().bakeLayer(DarkFountainModel.LAYER_LOCATION));
        return fountainModel;
    }

    @SubscribeEvent
    public static void renderLevel(RenderLevelStageEvent event) {
        if (event.getStage().equals(RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS)) {
            ClientLevel level = Minecraft.getInstance().level;
            if (level == null)
                return;


            if (DarkWorldClientData.fountainPos != null) {
                renderDarkFountain(
                        event.getPoseStack(),
                        MultiBufferSource.immediate(FOUNTAIN_BUFFER),
                        event.getPartialTick(),
                        event.getCamera().getPosition(),
                        DarkWorldClientData.fountainPos
                );
            }
        }
    }


    private static float currentFrame = 0;
    private static void renderDarkFountain(PoseStack poseStack, MultiBufferSource.BufferSource buffer, DeltaTracker deltaTracker, Vec3 cameraPos, BlockPos fountainPos) {
        currentFrame += deltaTracker.getRealtimeDeltaTicks() / 10;
        if (currentFrame >= 6) {
            currentFrame = 0;
        }

        ResourceLocation fountainTexture =
                ResourceLocation.fromNamespaceAndPath(VladMod.MOD_ID, String.format("textures/misc/dark_fountain/%s.png", (int) currentFrame));

        poseStack.pushPose();
        poseStack.translate(
                fountainPos.getX() - cameraPos.x,
                fountainPos.getY() - cameraPos.y,
                fountainPos.getZ() - cameraPos.z
        );

        Vec2 fountain2dPos = new Vec2(fountainPos.getX(), fountainPos.getZ());
        Vec2 camera2dPos = new Vec2((float) cameraPos.x, (float) cameraPos.z);

        double distance2d = Mth.sqrt(fountain2dPos.distanceToSqr(camera2dPos));
        double referenceDistance = 128;
        float distanceScale = (float) (distance2d / referenceDistance);
        distanceScale = Math.max(distanceScale, 1f);
        poseStack.scale(distanceScale, distanceScale, distanceScale);

        float yPosOffset = 15;
        for (int i = 0; i < 12; i++) {
            poseStack.pushPose();
            poseStack.translate(3.5f, yPosOffset * i, .5f);
            renderFountainSection(poseStack, buffer, fountainTexture);
            poseStack.rotateAround(Axis.YP.rotationDegrees(90), 0, 0, 0);
            poseStack.translate(3f, 0f, -3f);
            renderFountainSection(poseStack, buffer, fountainTexture);
            poseStack.popPose();
        }

        poseStack.popPose();
        buffer.endBatch();
    }

    private static void renderFountainSection(PoseStack poseStack, MultiBufferSource.BufferSource buffer, ResourceLocation texture) {
        poseStack.pushPose();
        poseStack.scale(1, -1, 1);
        getFountainModel().renderToBuffer(
                poseStack,
                buffer.getBuffer(ModRenderTypes.fountain(texture)),
                LightTexture.FULL_BRIGHT,
                OverlayTexture.NO_OVERLAY,
                0xFFFFFF);
        poseStack.popPose();
    }
}
