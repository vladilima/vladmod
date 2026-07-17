package com.vladilima.vladmod.entity.client;
// Made with Blockbench 5.1.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.vladilima.vladmod.VladMod;
import com.vladilima.vladmod.entity.custom.StarPlatinum;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class StarPlatinumModel<T extends StarPlatinum> extends HierarchicalModel<T> {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(VladMod.MOD_ID, "star_platinum"), "main");
	private final ModelPart root;
	private final ModelPart Waist;
	private final ModelPart head;
	private final ModelPart body;
	private final ModelPart rightArm;
	private final ModelPart leftArm;
	private final ModelPart rightLeg;
	private final ModelPart leftLeg;

	public StarPlatinumModel(ModelPart root) {
		this.root = root.getChild("root");
		this.Waist = this.root.getChild("Waist");
		this.head = this.Waist.getChild("head");
		this.body = this.Waist.getChild("body");
		this.rightArm = this.Waist.getChild("rightArm");
		this.leftArm = this.Waist.getChild("leftArm");
		this.rightLeg = this.Waist.getChild("rightLeg");
		this.leftLeg = this.Waist.getChild("leftLeg");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

		PartDefinition Waist = root.addOrReplaceChild("Waist", CubeListBuilder.create(), PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.0F, -0.4363F, 0.0F));

		PartDefinition head = Waist.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F))
				.texOffs(32, 0).addBox(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.5F)), PartPose.offsetAndRotation(0.0F, -12.0F, 0.0F, 0.0F, 0.4363F, 0.0F));

		PartDefinition body = Waist.addOrReplaceChild("body", CubeListBuilder.create().texOffs(16, 16).addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

		PartDefinition bodyLayer_r1 = body.addOrReplaceChild("Body Layer_r1", CubeListBuilder.create().texOffs(16, 32).addBox(-4.0F, -6.0F, -2.0F, 8.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(0.0F, 6.0F, 0.0F, 0.0F, -0.0436F, 0.0F));

		PartDefinition rightArm = Waist.addOrReplaceChild("rightArm", CubeListBuilder.create().texOffs(40, 16).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(40, 32).addBox(-2.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-5.0F, -10.0F, 0.0F, -0.2934F, -0.4623F, 0.1339F));

		PartDefinition leftArm = Waist.addOrReplaceChild("leftArm", CubeListBuilder.create().texOffs(32, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(48, 48).addBox(-1.0F, -2.0F, -2.0F, 3.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(5.1504F, -8.5F, 0.4769F, -1.293F, -0.0592F, 0.9642F));

		PartDefinition rightLeg = Waist.addOrReplaceChild("rightLeg", CubeListBuilder.create().texOffs(0, 16).addBox(-1.9F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 32).addBox(-1.9F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(-1.6993F, 1.0F, -2.0463F, 0.2618F, 0.0F, 0.0F));

		PartDefinition leftLeg = Waist.addOrReplaceChild("leftLeg", CubeListBuilder.create().texOffs(16, 48).addBox(-2.1F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.0F))
				.texOffs(0, 48).addBox(-2.1F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, new CubeDeformation(0.25F)), PartPose.offsetAndRotation(2.0F, 0.5F, -3.25F, 0.5236F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(StarPlatinum entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.Waist.getAllParts().forEach(ModelPart::resetPose);

		this.animate(entity.idleAnimationState, StarPlatinumAnimations.ANIM_IDLE, ageInTicks, 1f);
		this.animate(entity.punchAnimationState_1, StarPlatinumAnimations.ANIM_PUNCH, ageInTicks, 1f);
		this.animate(entity.punchAnimationState_2, StarPlatinumAnimations.ANIM_PUNCH_2, ageInTicks, 1f);
		this.animate(entity.punchAnimationState_3, StarPlatinumAnimations.ANIM_PUNCH_3, ageInTicks, 1f);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
		root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
	}

	@Override
	public ModelPart root() {
		return root;
	}
}