package com.commodorethrawn.strawgolem.entity;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;

// Made using Blockbench 3.5.3 by the talented Fr3nderman
// Exported for Minecraft version 1.15
public class ModelStrawGolem extends EntityModel<EntityStrawGolem> implements ModelWithArms {
	private final ModelPart Head;
	private final ModelPart Body;
	private final ModelPart rightleg;
	private final ModelPart Leftleg;
	private final ModelPart Rightarm;
	private final ModelPart Leftarm;

	public boolean holdingItem;
	public boolean holdingBlock;

	public ModelStrawGolem() {
		holdingItem = false;
		holdingBlock = false;
		textureWidth = 48;
		textureHeight = 48;

		Head = new ModelPart(this);
		Head.setPivot(0.0F, 11.0F, 0.0F);
		Head.setTextureOffset(26, 24).addCuboid(-2.0F, -4.0F, -2.0F, 4.0F, 4.0F, 4.0F, 0.0F, false);
		Head.setTextureOffset(11, 32).addCuboid(-2.0F, -5.0F, -2.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);

		Body = new ModelPart(this);
		Body.setPivot(0.0F, 24.0F, 0.0F);
		Body.setTextureOffset(20, 32).addCuboid(-4.0F, -13.0F, -3.0F, 8.0F, 10.0F, 6.0F, 0.0F, false);

		rightleg = new ModelPart(this);
		rightleg.setPivot(-2.0F, 21.0F, 0.0F);
		rightleg.setTextureOffset(12, 43).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

		Leftleg = new ModelPart(this);
		Leftleg.setPivot(2.0F, 21.0F, 0.0F);
		Leftleg.setTextureOffset(12, 43).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

		Rightarm = new ModelPart(this);
		Rightarm.setPivot(-5.0F, 12.0F, 0.0F);
		Rightarm.setTextureOffset(4, 39).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, false);

		Leftarm = new ModelPart(this);
		Leftarm.setPivot(5.0F, 12.0F, 0.0F);
		Leftarm.setTextureOffset(4, 39).addCuboid(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, false);
	}

	@Override
	public void setAngles(EntityStrawGolem entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		this.Head.pivotY = netHeadYaw * 0.017453292F;
		this.Head.pivotX = headPitch * 0.017453292F;

		this.Body.pivotY = 0.0F;

		float auxLimbSwing = limbSwing * 5.0F * 0.6662F;

		float swingAmountArm = 1.7F * limbSwingAmount;
		float swingAmoungLeg = 2.5F * limbSwingAmount;

		this.Rightarm.pivotX = MathHelper.cos(auxLimbSwing + (float) Math.PI) * swingAmountArm;
		this.Leftarm.pivotX = MathHelper.cos(auxLimbSwing) * swingAmountArm;
		this.Rightarm.pivotZ = 0.0F;
		this.Leftarm.pivotZ = 0.0F;
		this.rightleg.pivotX = MathHelper.cos(auxLimbSwing) * swingAmoungLeg;
		this.Leftleg.pivotX = MathHelper.cos(auxLimbSwing + (float) Math.PI) * swingAmoungLeg;
		this.rightleg.pivotY = 0.0F;
		this.Leftleg.pivotY = 0.0F;
		this.rightleg.pivotZ = 0.0F;
		this.Leftleg.pivotZ = 0.0F;

		this.Rightarm.pivotY = 0.0F;
		this.Rightarm.pivotZ = 0.0F;

		this.Leftarm.pivotY = 0.0F;

		this.Rightarm.pivotY = 0.0F;

		this.Body.pivotX = 0.0F;

		// Arms idle movement
		if (holdingBlock) {
			this.Rightarm.pivotX = (float) Math.PI;
			this.Leftarm.pivotX = (float) Math.PI;
		} else if (holdingItem) {
			this.Rightarm.pivotX = (float) -(0.29D * Math.PI);
			this.Rightarm.pivotY = (float) -(0.12D * Math.PI);
			this.Rightarm.pivotZ = (float) (0.08D * Math.PI);
			this.Leftarm.pivotX = (float) -(0.29D * Math.PI);
			this.Leftarm.pivotY = (float) (0.12D * Math.PI);
			this.Leftarm.pivotZ = (float) -(0.08D * Math.PI);
		} else {
			this.Rightarm.pivotZ += MathHelper.cos(ageInTicks * 0.09F) * 0.06F + 0.06F;
			this.Leftarm.pivotZ -= MathHelper.cos(ageInTicks * 0.09F) * 0.06F + 0.06F;
			this.Rightarm.pivotX += MathHelper.sin(ageInTicks * 0.067F) * 0.06F;
			this.Leftarm.pivotX -= MathHelper.sin(ageInTicks * 0.067F) * 0.06F;
		}
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		Head.render(matrixStack, buffer, packedLight, packedOverlay);
		Body.render(matrixStack, buffer, packedLight, packedOverlay);
		rightleg.render(matrixStack, buffer, packedLight, packedOverlay);
		Leftleg.render(matrixStack, buffer, packedLight, packedOverlay);
		Rightarm.render(matrixStack, buffer, packedLight, packedOverlay);
		Leftarm.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	@Override
	public void setArmAngle(Arm arm, MatrixStack matrixStackIn) {
		if (holdingBlock) {
			matrixStackIn.translate(0.075F, -0.75F, 0.585F);
			matrixStackIn.multiply(Vector3f.NEGATIVE_X.getDegreesQuaternion(15.0F));
			matrixStackIn.scale(1.5F, 1.5F, 1.5F);
		} else {
			matrixStackIn.translate(0.05F, 1.3F, 0.23F);
			matrixStackIn.multiply(Vector3f.NEGATIVE_X.getDegreesQuaternion(90.0F));
		}
	}
}
