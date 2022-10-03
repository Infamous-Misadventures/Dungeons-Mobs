package com.infamous.dungeons_mobs.client.renderer.blaze;

import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.blaze.WildfireModel;
import com.infamous.dungeons_mobs.client.renderer.layers.PulsatingGlowLayer;
import com.infamous.dungeons_mobs.entities.blaze.WildfireEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.example.client.DefaultBipedBoneIdents;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;
public class WildfireRenderer extends ExtendedGeoEntityRenderer<WildfireEntity> {
    public WildfireRenderer(EntityRendererManager renderManager) {
        super(renderManager, new WildfireModel());
        this.addLayer(new PulsatingGlowLayer<>(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/blaze/wildfire.png"), 0.1F, 1.0F, 0.25F));
    }
    
    @Override
    protected int getBlockLightLevel(WildfireEntity p_225624_1_, BlockPos p_225624_2_) {
    	return 15;
    }

    @Override
    protected void applyRotations(WildfireEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks,
                                  float rotationYaw, float partialTicks) {
        float scaleFactor = 1.25F;
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);

    }

    @Override
    public RenderType getRenderType(WildfireEntity animatable, float partialTicks, MatrixStack stack,
                                    IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
                                    ResourceLocation textureLocation) {
        return RenderType.entityTranslucent(getTextureLocation(animatable));
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if(this.isArmorBone(bone)) {
            bone.setCubesHidden(true);
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    protected boolean isArmorBone(GeoBone bone) {
        return bone.getName().startsWith("armor");
    }

    @Nullable
    @Override
    protected ResourceLocation getTextureForBone(String s, WildfireEntity windcallerEntity) {
        return null;
    }

	@Override
	protected ItemStack getHeldItemForBone(String boneName, WildfireEntity currentEntity) {
		return null;
	}

	@Override
	protected TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {		
		return TransformType.NONE;
	}

	@Override
	protected void preRenderItem(MatrixStack stack, ItemStack item, String boneName, WildfireEntity currentEntity, IBone bone) {
		if(item == this.mainHand || item == this.offHand) {
			stack.scale(1.1F, 1.1F, 1.1F);
			stack.mulPose(Vector3f.XP.rotationDegrees(-90.0F));
			boolean shieldFlag = item.getItem() instanceof ShieldItem;
			if(item == this.mainHand) {
				if(shieldFlag) {
					stack.translate(0.0, 0.125, -0.25);
				} else {
					
				}
			} else {
				if(shieldFlag) {
					stack.translate(-0.15, 0.125, 0.05);
					stack.mulPose(Vector3f.YP.rotationDegrees(90));
				} else {
					
				}
					
				
			}
		}
	}

	@Override
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, WildfireEntity currentEntity, IBone bone) {

	}
    
	@Override
	protected BlockState getHeldBlockForBone(String boneName, WildfireEntity currentEntity) {
		return null;
	}
	
	@Override
	protected void preRenderBlock(MatrixStack matrixStack, BlockState block, String boneName,
			WildfireEntity currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(MatrixStack matrixStack, BlockState block, String boneName,
			WildfireEntity currentEntity) {
		
	}

    @Nullable
    @Override
    protected ItemStack getArmorForBone(String boneName, WildfireEntity currentEntity) {
        switch (boneName) {
            case "armorHead":
                return helmet;
            default:
                return null;
        }
    }

    @Override
    protected EquipmentSlotType getEquipmentSlotForArmorBone(String boneName, WildfireEntity currentEntity) {
        switch (boneName) {
            case "armorHead":
                return EquipmentSlotType.HEAD;
            default:
                return null;
        }
    }

    @Override
    protected ModelRenderer getArmorPartForBone(String name, BipedModel<?> armorBipedModel) {
        switch (name) {
            case "armorHead":
                return armorBipedModel.head;
            default:
                return null;
        }
    }
}