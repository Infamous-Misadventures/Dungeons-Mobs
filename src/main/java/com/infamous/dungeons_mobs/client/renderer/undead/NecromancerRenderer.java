package com.infamous.dungeons_mobs.client.renderer.undead;

import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.undead.NecromancerModel;
import com.infamous.dungeons_mobs.client.particle.ModParticleTypes;
import com.infamous.dungeons_mobs.client.renderer.armor.NecromancerArmorGearRenderer;
import com.infamous.dungeons_mobs.client.renderer.layer.GeoEyeLayer;
import com.infamous.dungeons_mobs.client.renderer.layer.PulsatingGlowLayer;
import com.infamous.dungeons_mobs.entities.undead.NecromancerEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.block.BlockState;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.model.BipedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.example.client.DefaultBipedBoneIdents;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

public class NecromancerRenderer extends ExtendedGeoEntityRenderer<NecromancerEntity> {
	
    @SuppressWarnings("unchecked")
	public NecromancerRenderer(EntityRendererManager renderManager) {
        super(renderManager, new NecromancerModel());
        this.addLayer(new PulsatingGlowLayer(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/skeleton/necromancer_eyes.png"), 0.2F, 1.0F, 0.5F));
    }

    @Override
    protected void applyRotations(NecromancerEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks,
                                  float rotationYaw, float partialTicks) {
        float scaleFactor = 1.3F;
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);

    }

    @Override
    public RenderType getRenderType(NecromancerEntity animatable, float partialTicks, MatrixStack stack,
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
    protected ResourceLocation getTextureForBone(String s, NecromancerEntity windcallerEntity) {
        return null;
    }

	@Override
	protected ItemStack getHeldItemForBone(String boneName, NecromancerEntity currentEntity) {
		switch (boneName) {
		case DefaultBipedBoneIdents.LEFT_HAND_BONE_IDENT:
			return currentEntity.isLeftHanded() ? mainHand : offHand;
		case DefaultBipedBoneIdents.RIGHT_HAND_BONE_IDENT:
			return currentEntity.isLeftHanded() ? offHand : mainHand;
		case DefaultBipedBoneIdents.POTION_BONE_IDENT:
			break;
		}
		return null;
	}

	@Override
	protected TransformType getCameraTransformForItemAtBone(ItemStack boneItem, String boneName) {
		switch (boneName) {
		case DefaultBipedBoneIdents.LEFT_HAND_BONE_IDENT:
			return TransformType.THIRD_PERSON_RIGHT_HAND;
		case DefaultBipedBoneIdents.RIGHT_HAND_BONE_IDENT:
			return TransformType.THIRD_PERSON_RIGHT_HAND;
		default:
			return TransformType.NONE;
		}
	}

	@Override
	protected void preRenderItem(MatrixStack stack, ItemStack item, String boneName, NecromancerEntity currentEntity, IBone bone) {
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
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, NecromancerEntity currentEntity, IBone bone) {

	}
    
	@Override
	protected BlockState getHeldBlockForBone(String boneName, NecromancerEntity currentEntity) {
		return null;
	}
	
	@Override
	protected void preRenderBlock(MatrixStack matrixStack, BlockState block, String boneName,
			NecromancerEntity currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(MatrixStack matrixStack, BlockState block, String boneName,
			NecromancerEntity currentEntity) {
		
	}

    @Nullable
    @Override
    protected ItemStack getArmorForBone(String boneName, NecromancerEntity currentEntity) {
        switch (boneName) {
            case "armorBipedLeftFoot":
            case "armorBipedRightFoot":
                return boots;
            case "armorBipedLeftLeg":
            case "armorBipedRightLeg":
                return leggings;
            case "armorBipedBody":
            case "armorBipedRightArm":
            case "armorBipedLeftArm":
                return chestplate;
            case "armorBipedHead":
                return helmet;
            default:
                return null;
        }
    }

    @Override
    protected EquipmentSlotType getEquipmentSlotForArmorBone(String boneName, NecromancerEntity currentEntity) {
        switch (boneName) {
            case "armorBipedLeftFoot":
            case "armorBipedRightFoot":
                return EquipmentSlotType.FEET;
            case "armorBipedLeftLeg":
            case "armorBipedRightLeg":
                return EquipmentSlotType.LEGS;
            case "armorBipedRightHand":
                return !currentEntity.isLeftHanded() ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
            case "armorBipedLeftHand":
                return currentEntity.isLeftHanded() ? EquipmentSlotType.MAINHAND : EquipmentSlotType.OFFHAND;
            case "armorBipedRightArm":
            case "armorBipedLeftArm":
            case "armorBipedBody":
                return EquipmentSlotType.CHEST;
            case "armorBipedHead":
                return EquipmentSlotType.HEAD;
            default:
                return null;
        }
    }

    @Override
    protected ModelRenderer getArmorPartForBone(String name, BipedModel<?> armorBipedModel) {
        switch (name) {
            case "armorBipedLeftFoot":
            case "armorBipedLeftLeg":
                return armorBipedModel.leftLeg;
            case "armorBipedRightFoot":
            case "armorBipedRightLeg":
                return armorBipedModel.rightLeg;
            case "armorBipedRightArm":
                return armorBipedModel.rightArm;
            case "armorBipedLeftArm":
                return armorBipedModel.leftArm;
            case "armorBipedBody":
                return armorBipedModel.body;
            case "armorBipedHead":
                return armorBipedModel.head;
            default:
                return null;
        }
    }

    protected void handleGeoArmorBoneVisibility(GeoArmorRenderer<? extends GeoArmorItem> geoArmorRenderer, ModelRenderer sourceLimb, BipedModel<?> armorModel, EquipmentSlotType slot) {
        super.handleGeoArmorBoneVisibility(geoArmorRenderer, sourceLimb, armorModel, slot);
        if(geoArmorRenderer instanceof NecromancerArmorGearRenderer) {
            IBone gbHood = geoArmorRenderer.getGeoModelProvider().getBone(((NecromancerArmorGearRenderer) geoArmorRenderer).hoodBodyBone);
            IBone gbLeggingsBody = geoArmorRenderer.getGeoModelProvider().getBone(((NecromancerArmorGearRenderer) geoArmorRenderer).leggingsBodyBone);
            gbHood.setHidden(true);
            gbLeggingsBody.setHidden(true);

            if (sourceLimb == armorModel.body) {
                gbHood.setHidden(false);
                return;
            }
            if (sourceLimb == armorModel.rightLeg) {
                gbLeggingsBody.setHidden(true);
                return;
            }
        }
    }
}