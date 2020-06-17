package com.commodorethrawn.strawgolem.entity;

import com.commodorethrawn.strawgolem.Strawgolem;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class RenderStrawGolem extends MobEntityRenderer<EntityStrawGolem, ModelStrawGolem> {

    private static final Identifier TEXTURE = new Identifier(Strawgolem.MODID, "textures/entity/straw_golem.png");


    public RenderStrawGolem(EntityRenderDispatcher rendermanagerIn) {
        super(rendermanagerIn, new ModelStrawGolem(), 0.5f);
        this.addFeature(new HeldItemFeatureRenderer<>(this));
    }

    @Override
    public void render(EntityStrawGolem entityIn, float entityYaw, float partialTicks, MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn) {
        ModelStrawGolem golem = this.getModel();
        golem.holdingItem = !entityIn.isHandEmpty();
        golem.holdingBlock = entityIn.holdingBlockCrop();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }


    @Override
    public Identifier getTexture(EntityStrawGolem entity) {
        return TEXTURE;
    }
}
