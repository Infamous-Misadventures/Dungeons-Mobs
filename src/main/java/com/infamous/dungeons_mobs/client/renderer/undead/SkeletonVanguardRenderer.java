package com.infamous.dungeons_mobs.client.renderer.undead;

import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.client.models.undead.SkeletonVanguardModel;
import com.infamous.dungeons_mobs.entities.undead.SkeletonVanguardEntity;
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
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.example.client.DefaultBipedBoneIdents;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoCube;
import software.bernie.geckolib3.renderers.geo.ExtendedGeoEntityRenderer;
public class SkeletonVanguardRenderer extends ExtendedGeoEntityRenderer<SkeletonVanguardEntity> {
    public SkeletonVanguardRenderer(EntityRendererManager renderManager) {
        super(renderManager, new SkeletonVanguardModel());
    }

    @Override
    protected void applyRotations(SkeletonVanguardEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks,
                                  float rotationYaw, float partialTicks) {
        float scaleFactor = 1.0F;
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);

    }

    @Override
    public RenderType getRenderType(SkeletonVanguardEntity animatable, float partialTicks, MatrixStack stack,
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
    protected ResourceLocation getTextureForBone(String s, SkeletonVanguardEntity windcallerEntity) {
        return null;
    }

	@Override
	protected ItemStack getHeldItemForBone(String boneName, SkeletonVanguardEntity currentEntity) {
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
	protected void preRenderItem(MatrixStack stack, ItemStack item, String boneName, SkeletonVanguardEntity currentEntity, IBone bone) {
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
	protected void postRenderItem(MatrixStack matrixStack, ItemStack item, String boneName, SkeletonVanguardEntity currentEntity, IBone bone) {

	}
    
	@Override
	protected BlockState getHeldBlockForBone(String boneName, SkeletonVanguardEntity currentEntity) {
		return null;
	}
	
	@Override
	protected void preRenderBlock(MatrixStack matrixStack, BlockState block, String boneName,
			SkeletonVanguardEntity currentEntity) {
		
	}

	@Override
	protected void postRenderBlock(MatrixStack matrixStack, BlockState block, String boneName,
			SkeletonVanguardEntity currentEntity) {
		
	}

    @Nullable
    @Override
    protected ItemStack getArmorForBone(String boneName, SkeletonVanguardEntity currentEntity) {
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
            case "armorIllagerRightArm":
            case "armorIllagerLeftArm":
                return chestplate;
            case "armorBipedHead":
                return helmet;
            default:
                return null;
        }
    }

    @Override
    protected EquipmentSlotType getEquipmentSlotForArmorBone(String boneName, SkeletonVanguardEntity currentEntity) {
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
            case "armorIllagerRightArm":
            case "armorIllagerLeftArm":
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
            case "armorIllagerRightArm":
                return armorBipedModel.rightArm;
            case "armorBipedLeftArm":
            case "armorIllagerLeftArm":
                return armorBipedModel.leftArm;
            case "armorBipedBody":
                return armorBipedModel.body;
            case "armorBipedHead":
                return armorBipedModel.head;
            default:
                return null;
        }
    }

    @Override
    protected void prepareArmorPositionAndScale(GeoBone bone, ObjectList<ModelRenderer.ModelBox> cubeList, ModelRenderer sourceLimb, MatrixStack stack, boolean geoArmor, boolean modMatrixRot) {
        GeoCube firstCube = (GeoCube)bone.childCubes.get(0);
        ModelRenderer.ModelBox armorCube = (ModelRenderer.ModelBox)cubeList.get(0);
        float targetSizeX = firstCube.size.x();
        float targetSizeY = firstCube.size.y();
        float targetSizeZ = firstCube.size.z();
        float sourceSizeX = Math.abs(armorCube.maxX - armorCube.minX);
        float sourceSizeY = Math.abs(armorCube.maxY - armorCube.minY);
        float sourceSizeZ = Math.abs(armorCube.maxZ - armorCube.minZ);
        float scaleX = targetSizeX / sourceSizeX;
        float scaleY = targetSizeY / sourceSizeY;
        float scaleZ = targetSizeZ / sourceSizeZ;
        sourceLimb.setPos(-(bone.getPivotX() - (bone.getPivotX() * scaleX - bone.getPivotX()) / scaleX), -(bone.getPivotY() - (bone.getPivotY() * scaleY - bone.getPivotY()) / scaleY), bone.getPivotZ() - (bone.getPivotZ() * scaleZ - bone.getPivotZ()) / scaleZ);
        if (!geoArmor) {
            sourceLimb.xRot = -bone.getRotationX();
            sourceLimb.yRot = -bone.getRotationY();
            sourceLimb.zRot = bone.getRotationZ();
        } else {
            float xRot = -bone.getRotationX();
            float yRot = -bone.getRotationY();
            float zRot = bone.getRotationZ();


            for(GeoBone tmpBone = bone.parent; tmpBone != null; tmpBone = tmpBone.parent) {
                if(bone.getName().equals("armorBipedHead") && tmpBone.getName().equals("bipedBody")) {
                   break;
                }
                xRot -= tmpBone.getRotationX();
                yRot -= tmpBone.getRotationY();
                zRot += tmpBone.getRotationZ();
            }

            if (modMatrixRot) {
                xRot = (float)Math.toRadians((double)xRot);
                yRot = (float)Math.toRadians((double)yRot);
                zRot = (float)Math.toRadians((double)zRot);
                stack.mulPose(new Quaternion(0.0F, 0.0F, zRot, false));
                stack.mulPose(new Quaternion(0.0F, yRot, 0.0F, false));
                stack.mulPose(new Quaternion(xRot, 0.0F, 0.0F, false));
            } else {
                sourceLimb.xRot = xRot;
                sourceLimb.yRot = yRot;
                sourceLimb.zRot = zRot;
            }
        }

        stack.scale(scaleX, scaleY, scaleZ);
    }
}