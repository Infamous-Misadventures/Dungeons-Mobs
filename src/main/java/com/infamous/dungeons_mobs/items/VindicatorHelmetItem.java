package com.infamous.dungeons_mobs.items;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;
import static com.infamous.dungeons_mobs.client.models.geom.ModModelLayers.VINDICATOR_HELMET;

import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.client.models.armor.VindicatorHelmetModel;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

public class VindicatorHelmetItem extends ArmorItem {
	private final boolean isDiamond;

	public VindicatorHelmetItem(ArmorMaterial materialIn, EquipmentSlot slot, Properties builderIn,
			boolean isDiamondIn) {
		super(materialIn, slot, builderIn);
		this.isDiamond = isDiamondIn;
	}

	@Override
	public void initializeClient(
			java.util.function.Consumer<net.minecraftforge.client.extensions.common.IClientItemExtensions> consumer) {
		consumer.accept(new IClientItemExtensions() {
			private VindicatorHelmetModel model;

			@Nullable
			@Override
			public net.minecraft.client.model.HumanoidModel<?> getHumanoidArmorModel(LivingEntity entityLiving,
					ItemStack itemStack, EquipmentSlot armorSlot,
					net.minecraft.client.model.HumanoidModel<?> _default) {
				if (null == model) {
					model = new VindicatorHelmetModel(
							net.minecraft.client.Minecraft.getInstance().getEntityModels().bakeLayer(VINDICATOR_HELMET),
							entityLiving, isDiamond);
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
			return MODID + ":textures/models/armor/diamond_vindicator_helmet.png";
		}
		return MODID + ":textures/models/armor/gold_vindicator_helmet.png";
	}
}
