package com.infamous.dungeons_mobs.client.renderer.piglin;

import com.infamous.dungeons_mobs.DungeonsMobs;
import com.infamous.dungeons_mobs.client.models.FungusSackModel;
import com.infamous.dungeons_mobs.client.models.geom.ModModelLayers;
import com.infamous.dungeons_mobs.client.renderer.layers.FungusSackLayer;
import com.infamous.dungeons_mobs.interfaces.ISmartCrossbowUser;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.PiglinRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class CustomPiglinRenderer extends PiglinRenderer {
    public static final Map<EntityType<?>, Map<String, ResourceLocation>> RESOURCE_LOCATION_MAP = new HashMap<>();

    public CustomPiglinRenderer(EntityRendererProvider.Context rendererContext, ModelLayerLocation p_174345_, ModelLayerLocation p_174346_, ModelLayerLocation p_174347_, boolean isZombified, boolean isFungusThrower) {
        super(rendererContext, p_174345_, p_174346_, p_174347_, isZombified);
        if (isFungusThrower) {
            this.addLayer(new FungusSackLayer<>(this, new FungusSackModel<Mob>(rendererContext.bakeLayer(ModModelLayers.FUNGUS_SACK))));
        }
    }

    @Override
    protected void scale(Mob mobEntity, PoseStack matrixStack, float v) {
        super.scale(mobEntity, matrixStack, v);
    }

    @Override
    public ResourceLocation getTextureLocation(Mob mobEntity) {
        // Check for the vanilla piglin or ziglin not holding a crossbow -  if so, we want to use the vanilla path for its texture
        boolean isVanillaMob = mobEntity.getType() == EntityType.PIGLIN || mobEntity.getType() == EntityType.ZOMBIFIED_PIGLIN;
        if (isVanillaMob && mobEntity instanceof ISmartCrossbowUser && !((ISmartCrossbowUser) mobEntity).isCrossbowUser()) {
            return super.getTextureLocation(mobEntity);
        }

        String skinVariantName = this.getSkinVariantName(mobEntity);
        return RESOURCE_LOCATION_MAP
                .computeIfAbsent(mobEntity.getType(), type -> new HashMap<>())
                .computeIfAbsent(skinVariantName, s -> new ResourceLocation(DungeonsMobs.MODID, this.getPath(skinVariantName)));
    }

    private String getPath(String skinVariantName) {
        return "textures/entity/piglin/" + skinVariantName + ".png";
    }

    private String getSkinVariantName(Mob mobEntity) {
        String skinVariantName = ForgeRegistries.ENTITY_TYPES.getKey(mobEntity.getType()).getPath();
        skinVariantName = maybeAddArmorPrefix(mobEntity, skinVariantName);
        skinVariantName = maybeAddHunterSuffix(mobEntity, skinVariantName);
        return skinVariantName;
    }

    private String maybeAddArmorPrefix(Mob mobEntity, String in) {
        Item helmetItem = mobEntity.getItemBySlot(EquipmentSlot.HEAD).getItem();
        if (helmetItem.equals(ModItems.GOLD_PIGLIN_HELMET.get()) || helmetItem.equals(ModItems.CRACKED_GOLD_PIGLIN_HELMET.get())) {
            return "gold_armored_" + in;
        } else if (helmetItem.equals(ModItems.NETHERITE_PIGLIN_HELMET.get()) || helmetItem.equals(ModItems.CRACKED_NETHERITE_PIGLIN_HELMET.get())) {
            return "netherite_armored_" + in;
        }
        return in;
    }

    private String maybeAddHunterSuffix(Mob mobEntity, String in) {
        if (mobEntity instanceof ISmartCrossbowUser && ((ISmartCrossbowUser) mobEntity).isCrossbowUser()) {
            return in + "_hunter";
        }
        return in;
    }

}