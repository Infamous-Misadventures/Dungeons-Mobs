package com.infamous.dungeons_mobs.items;

import com.infamous.dungeons_mobs.client.models.armor.PillagerHelmetModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IItemRenderProperties;

import javax.annotation.Nullable;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;
import static com.infamous.dungeons_mobs.client.models.geom.ModModelLayers.PILLAGER_HELMET;

public class PillagerHelmetItem extends ArmorItem {
    private final boolean isDiamond;

    public PillagerHelmetItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties builderIn, boolean isDiamondIn) {
        super(materialIn, slot, builderIn);
        this.isDiamond = isDiamondIn;
    }

    @Override
    public void initializeClient(java.util.function.Consumer<net.minecraftforge.client.IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            private PillagerHelmetModel model;

            @Nullable
            @Override
            public net.minecraft.client.model.HumanoidModel<?> getArmorModel(LivingEntity entityLiving, ItemStack itemStack, EquipmentSlot armorSlot, net.minecraft.client.model.HumanoidModel<?> _default) {
                if (null == model) {
                    model = new PillagerHelmetModel(
                            net.minecraft.client.Minecraft.getInstance().getEntityModels()
                                    .bakeLayer(PILLAGER_HELMET),
                            entityLiving);
                }
                return model;
            }
        });
    }

    @Nullable
    @Override
    @OnlyIn(Dist.CLIENT)
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        if (this.isDiamond) {
            return MODID + ":textures/models/armor/diamond_pillager_helmet.png";
        }
        return MODID + ":textures/models/armor/gold_pillager_helmet.png";
    }
}
