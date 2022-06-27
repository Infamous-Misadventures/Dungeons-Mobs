package com.infamous.dungeons_mobs.client.renderer.illager;

import com.infamous.dungeons_mobs.client.models.illager.MageModel;
import com.infamous.dungeons_mobs.entities.illagers.MageCloneEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class MageCloneRenderer extends GeoEntityRenderer<MageCloneEntity> {
	
	   private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();
	   public MageCloneEntity thisMage;
	
	public MageCloneRenderer(EntityRendererManager renderManager) {
		super(renderManager, new MageModel());
		//this.addLayer(new GeoEyeLayer<>(this, new ResourceLocation(DungeonsMobs.MODID, "textures/entity/enchanter/enchanter_eyes.png")));
		//this.addLayer(new GeoHeldItemLayer<>(this, 0.0, 0.0, 0.5));
	}
	
	protected void applyRotations(MageCloneEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks,
			float rotationYaw, float partialTicks) {
        float scaleFactor = 0.9375F;
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
		super.applyRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
	}

	@Override
	public RenderType getRenderType(MageCloneEntity animatable, float partialTicks, MatrixStack stack,
			IRenderTypeBuffer renderTypeBuffer, IVertexBuilder vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation) {
		return RenderType.entityTranslucent(getTextureLocation(animatable));
	}
	@Override
	public void render(MageCloneEntity entity, float entityYaw, float partialTicks, MatrixStack stack,
			IRenderTypeBuffer bufferIn, int packedLightIn) {

		thisMage = entity;
		
	      if (this.getArmorResource(entity, entity.getItemBySlot(EquipmentSlotType.CHEST), EquipmentSlotType.CHEST, null) != null) {
	  		Minecraft.getInstance().textureManager.bind(this.getArmorResource(entity, entity.getItemBySlot(EquipmentSlotType.CHEST), EquipmentSlotType.CHEST, null));
	      } 
		
	      if (this.getArmorResource(entity, entity.getItemBySlot(EquipmentSlotType.HEAD), EquipmentSlotType.HEAD, null) != null) {
	  		Minecraft.getInstance().textureManager.bind(this.getArmorResource(entity, entity.getItemBySlot(EquipmentSlotType.HEAD), EquipmentSlotType.HEAD, null));
	      } 
	      
	      if (this.getArmorResource(entity, entity.getItemBySlot(EquipmentSlotType.LEGS), EquipmentSlotType.LEGS, null) != null) {
	  		Minecraft.getInstance().textureManager.bind(this.getArmorResource(entity, entity.getItemBySlot(EquipmentSlotType.LEGS), EquipmentSlotType.LEGS, null));
	      } 
	      
	      if (this.getArmorResource(entity, entity.getItemBySlot(EquipmentSlotType.FEET), EquipmentSlotType.FEET, null) != null) {
	  		Minecraft.getInstance().textureManager.bind(this.getArmorResource(entity, entity.getItemBySlot(EquipmentSlotType.FEET), EquipmentSlotType.FEET, null));
	      }
	       
			super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
	      
	}
	
	@Override
	public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
		bufferIn = rtb.getBuffer(RenderType.entityTranslucent(new ResourceLocation(DungeonsMobs.MODID, "textures/entity/illager/mage.png")));
		
	    if (bone.getName().equals("armourLeftArm")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
	        bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));    
		      if (this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.CHEST), EquipmentSlotType.CHEST, null) != null) {
			        bufferIn = rtb.getBuffer(RenderType.entityTranslucent((this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.CHEST), EquipmentSlotType.CHEST, null))));
			      } 
	    }	
	    
	    if (bone.getName().equals("armourRightArm")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
	        bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
		      if (this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.CHEST), EquipmentSlotType.CHEST, null) != null) {
			        bufferIn = rtb.getBuffer(RenderType.entityTranslucent((this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.CHEST), EquipmentSlotType.CHEST, null))));
			      } 

	    }	
	    
	    if (bone.getName().equals("armourBody")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
	        bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
		      if (this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.CHEST), EquipmentSlotType.CHEST, null) != null) {
			        bufferIn = rtb.getBuffer(RenderType.entityTranslucent((this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.CHEST), EquipmentSlotType.CHEST, null))));
			      } 

	    }	
	    
	    if (bone.getName().equals("innerArmourBody")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
	        bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
		      if (this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.LEGS), EquipmentSlotType.LEGS, null) != null) {
			        bufferIn = rtb.getBuffer(RenderType.entityTranslucent((this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.LEGS), EquipmentSlotType.LEGS, null))));
			      } 

	    }	
	    
	    if (bone.getName().equals("innerArmourLeftLeg")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
	        bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
		      if (this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.LEGS), EquipmentSlotType.LEGS, null) != null) {
			        bufferIn = rtb.getBuffer(RenderType.entityTranslucent((this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.LEGS), EquipmentSlotType.LEGS, null))));
			      } 

	    }	
	    
	    if (bone.getName().equals("innerArmourRightLeg")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
	        bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
		      if (this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.LEGS), EquipmentSlotType.LEGS, null) != null) {
			        bufferIn = rtb.getBuffer(RenderType.entityTranslucent((this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.LEGS), EquipmentSlotType.LEGS, null))));
			      } 

	    }	
	    
	    if (bone.getName().equals("armourLeftLeg")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
	        bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
		      if (this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.FEET), EquipmentSlotType.FEET, null) != null) {
			        bufferIn = rtb.getBuffer(RenderType.entityTranslucent((this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.FEET), EquipmentSlotType.FEET, null))));
			      } 

	    }	
	    
	    if (bone.getName().equals("armourRightLeg")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
	        bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
		      if (this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.FEET), EquipmentSlotType.FEET, null) != null) {
			        bufferIn = rtb.getBuffer(RenderType.entityTranslucent((this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.FEET), EquipmentSlotType.FEET, null))));
			      } 

	    }	
	    
	    if (bone.getName().equals("armourHead")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
	        bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
		      if (this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.HEAD), EquipmentSlotType.HEAD, null) != null) {
			        bufferIn = rtb.getBuffer(RenderType.entityTranslucent((this.getArmorResource(thisMage, thisMage.getItemBySlot(EquipmentSlotType.HEAD), EquipmentSlotType.HEAD, null))));
			      } 

	    }	
		
	    if (bone.getName().equals("leftHand")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
	        stack.pushPose();
	        //You'll need to play around with these to get item to render in the correct orientation
	        stack.mulPose(Vector3f.XP.rotationDegrees(15)); 
	        stack.mulPose(Vector3f.YP.rotationDegrees(0)); 
	        stack.mulPose(Vector3f.ZP.rotationDegrees(0));
	        //You'll need to play around with this to render the item in the correct spot.
	        stack.translate(-0.4D, 0.6D, -0.7D); 
	        //Sets the scaling of the item.
	        stack.scale(1.0f, 1.0f, 1.0f); 
	        // Change mainHand to predefined Itemstack and TransformType to what transform you would want to use.
	        Minecraft.getInstance().getItemRenderer().renderStatic(offHand, TransformType.THIRD_PERSON_LEFT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
	        stack.popPose();

	    }		
		
		if (bone.getName().equals("rightHand")) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
	        stack.pushPose();
	        //You'll need to play around with these to get item to render in the correct orientation
	        stack.mulPose(Vector3f.XP.rotationDegrees(-75)); 
	        stack.mulPose(Vector3f.YP.rotationDegrees(0)); 
	        stack.mulPose(Vector3f.ZP.rotationDegrees(0));
	        //You'll need to play around with this to render the item in the correct spot.
	        stack.translate(0.4D, 0.4D, 0.8D); 
	        //Sets the scaling of the item.
	        stack.scale(1.0f, 1.0f, 1.0f); 
	        // Change mainHand to predefined Itemstack and TransformType to what transform you would want to use.
	        Minecraft.getInstance().getItemRenderer().renderStatic(mainHand, TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, this.rtb);
	        stack.popPose();

	    }
		
	    if (bone.getName().equals("headWear") && !(thisMage.getItemBySlot(EquipmentSlotType.HEAD).getItem() instanceof ArmorItem)) { // rArmRuff is the name of the bone you to set the item to attach too. Please see Note
	        stack.pushPose();
	        //You'll need to play around with these to get item to render in the correct orientation
	        stack.mulPose(Vector3f.XP.rotationDegrees(0)); 
	        stack.mulPose(Vector3f.YP.rotationDegrees(0)); 
	        stack.mulPose(Vector3f.ZP.rotationDegrees(0));
	        //You'll need to play around with this to render the item in the correct spot.
	        stack.translate(0.0D, 1.75D, 0.0D); 
	        //Sets the scaling of the item.
	        stack.scale(0.6F, 0.6F, 0.6F); 
	        // Change mainHand to predefined Itemstack and TransformType to what transform you would want to use.
	        Minecraft.getInstance().getItemRenderer().renderStatic(helmet, TransformType.HEAD, packedLightIn, packedOverlayIn, stack, this.rtb);
	        stack.popPose();
	        bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
	    }
	    
    

	    
	    super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
	}
	
	   /**
	    * More generic ForgeHook version of the above function, it allows for Items to have more control over what texture they provide.
	    *
	    * @param entity Entity wearing the armor
	    * @param stack ItemStack for the armor
	    * @param slot Slot ID that the item is in
	    * @param type Subtype, can be null or "overlay"
	    * @return ResourceLocation pointing at the armor's texture
	    */
	   public ResourceLocation getArmorResource(net.minecraft.entity.Entity entity, ItemStack stack, EquipmentSlotType slot, @Nullable String type) {
		   if (stack.getItem() instanceof ArmorItem) {
	      ArmorItem item = (ArmorItem)stack.getItem();
	      String texture = item.getMaterial().getName();
	      String domain = "dungeons_mobs";
	      int idx = texture.indexOf(':');
	      if (idx != -1) {
	         domain = texture.substring(0, idx);
	         texture = texture.substring(idx + 1);
	      }
	      
	      String s1 = null;
	      if (slot == EquipmentSlotType.HEAD) {
	    	  s1 = String.format("%s:textures/geo_armor/geo_%s_helmet.png", domain, texture, type == null ? "" : String.format("_%s", type)); 
	      } else if (slot == EquipmentSlotType.CHEST) {
	    	  s1 = String.format("%s:textures/geo_armor/geo_%s_chestplate.png", domain, texture, type == null ? "" : String.format("_%s", type));  	    	  
	      } else if (slot == EquipmentSlotType.LEGS) {
	    	  s1 = String.format("%s:textures/geo_armor/geo_%s_leggings.png", domain, texture, type == null ? "" : String.format("_%s", type));  	    	  
	      } else if (slot == EquipmentSlotType.FEET) {
	    	  s1 = String.format("%s:textures/geo_armor/geo_%s_boots.png", domain, texture, type == null ? "" : String.format("_%s", type));  	    	  
	      }

	      s1 = net.minecraftforge.client.ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
	      ResourceLocation resourcelocation = ARMOR_LOCATION_CACHE.get(s1);

	      if (resourcelocation == null) {
	         resourcelocation = new ResourceLocation(s1);
	         ARMOR_LOCATION_CACHE.put(s1, resourcelocation);
	      }

	      return resourcelocation;
		   } else {

			   return null;

		   }
	   }
	   
		public Integer getUniqueID(MageEntity animatable) {
			return animatable.getId();
	}
}
