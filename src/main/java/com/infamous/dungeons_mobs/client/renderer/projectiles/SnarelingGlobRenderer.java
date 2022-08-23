package com.infamous.dungeons_mobs.client.renderer.projectiles;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.projectile.SnarelingGlobModel;
import com.infamous.dungeons_mobs.entities.projectiles.SnarelingGlobEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SnarelingGlobRenderer extends EntityRenderer<SnarelingGlobEntity> {
   private static final ResourceLocation LLAMA_SPIT_LOCATION = new ResourceLocation(DungeonsMobs.MODID, "textures/entity/snareling_glob.png");
   private final SnarelingGlobModel<SnarelingGlobEntity> model = new SnarelingGlobModel<>();

   public SnarelingGlobRenderer(EntityRendererManager p_i47202_1_) {
      super(p_i47202_1_);
   }
   
   protected int getBlockLightLevel(SnarelingGlobEntity p_225624_1_, BlockPos p_225624_2_) {
	      return 10;
	   }

   public void render(SnarelingGlobEntity p_225623_1_, float p_225623_2_, float p_225623_3_, MatrixStack p_225623_4_, IRenderTypeBuffer p_225623_5_, int p_225623_6_) {
      p_225623_4_.pushPose();
      p_225623_4_.translate(0.0D, (double)0.15F, 0.0D);
      p_225623_4_.mulPose(Vector3f.YP.rotationDegrees(MathHelper.lerp(p_225623_3_, p_225623_1_.yRotO, p_225623_1_.yRot)));
      p_225623_4_.mulPose(Vector3f.ZP.rotationDegrees(MathHelper.lerp(p_225623_3_, p_225623_1_.xRotO, p_225623_1_.xRot)));
      this.model.setupAnim(p_225623_1_, p_225623_3_, 0.0F, -0.1F, 0.0F, 0.0F);
      IVertexBuilder ivertexbuilder = p_225623_5_.getBuffer(this.model.renderType(LLAMA_SPIT_LOCATION));
      this.model.renderToBuffer(p_225623_4_, ivertexbuilder, p_225623_6_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
      p_225623_4_.popPose();
      super.render(p_225623_1_, p_225623_2_, p_225623_3_, p_225623_4_, p_225623_5_, p_225623_6_);
   }

   public ResourceLocation getTextureLocation(SnarelingGlobEntity p_110775_1_) {
      return LLAMA_SPIT_LOCATION;
   }
}
