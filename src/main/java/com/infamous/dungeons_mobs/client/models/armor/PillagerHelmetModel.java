package com.infamous.dungeons_mobs.client.models.armor;// Made with Blockbench 3.6.6
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PillagerHelmetModel<T extends LivingEntity> extends HumanoidModel<T> {
    private final LivingEntity livingEntity;
    private final ModelPart helmet;

    public PillagerHelmetModel(ModelPart modelPart, LivingEntity livingEntity) {
        super(modelPart);
        this.helmet = modelPart.getChild("head");
        this.livingEntity = livingEntity;
    }

    public static LayerDefinition createHelmetLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();
        PartDefinition helmet = partdefinition.addOrReplaceChild("head", CubeListBuilder.create().texOffs(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 6.0F, 8.0F), PartPose.offset(0.0F, -24.0F, 0.0F));
        helmet.addOrReplaceChild("flap1", CubeListBuilder.create().texOffs(46, 27).addBox(-4.0F, -10.0F, -4.0F, 8.0F, 6.0F, 8.0F), PartPose.offset(-1.0F, 0.0F, -4.0F));
        helmet.addOrReplaceChild("flap2", CubeListBuilder.create().texOffs(46, 27).addBox(-5.0F, -3.0F, 0.0F, 1.0F, 3.0F, 8.0F), PartPose.offset(-1.0F, 0.0F, -4.0F));
        helmet.addOrReplaceChild("flap3", CubeListBuilder.create().texOffs(45, 30).addBox(-8.0F, -3.0F, 0.0F, 8.0F, 3.0F, 1.0F), PartPose.offset(4.0F, -3.0F, 7.0F));
        partdefinition.addOrReplaceChild("hat", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_arm", CubeListBuilder.create(), PartPose.offset(-5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_arm", CubeListBuilder.create(), PartPose.offset(5.0F, 2.0F, 0.0F));
        partdefinition.addOrReplaceChild("right_leg", CubeListBuilder.create(), PartPose.offset(-1.9F, 12.0F, 0.0F));
        partdefinition.addOrReplaceChild("left_leg", CubeListBuilder.create(), PartPose.offset(1.9F, 12.0F, 0.0F));
        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        matrixStackIn.pushPose();
        this.helmet.copyFrom(this.head);
        if (livingEntity.isBaby()) {
            matrixStackIn.scale(0.8F, 0.8F, 0.8F);
            this.helmet.setPos(0.0F, 15.0F, 0.0F);
        }
        this.helmet.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
        matrixStackIn.popPose();
    }

}