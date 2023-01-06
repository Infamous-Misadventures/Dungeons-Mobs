package com.infamous.dungeons_mobs.entities.piglin;

import javax.annotation.Nullable;

import com.infamous.dungeons_mobs.entities.piglin.ai.FungusThrowerAi;
import com.infamous.dungeons_mobs.interfaces.ISmartCrossbowUser;
import com.infamous.dungeons_mobs.items.BlueNethershroomItem;
import com.infamous.dungeons_mobs.mixin.PiglinAccessor;
import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModItems;
import com.infamous.dungeons_mobs.utils.PiglinHelper;
import com.mojang.serialization.Dynamic;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.monster.piglin.Piglin;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;

public class FungusThrowerEntity extends Piglin {

	public FungusThrowerEntity(EntityType<? extends FungusThrowerEntity> entityType, Level world) {
		super(entityType, world);
	}

	@Override
	public boolean canFireProjectileWeapon(ProjectileWeaponItem shootableItem) {
		return super.canFireProjectileWeapon(shootableItem) || shootableItem instanceof BlueNethershroomItem;
	}

	@Override
	protected Brain<?> makeBrain(Dynamic<?> dynamic) {
		Brain<?> brain = super.makeBrain(dynamic);
		// noinspection unchecked
		FungusThrowerAi.addFungusThrowerTasks((Brain<FungusThrowerEntity>) brain);
		return brain;
	}

	@Nullable
	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverWorld, DifficultyInstance difficultyInstance,
			MobSpawnType spawnReason, @Nullable SpawnGroupData spawnDataIn, @Nullable CompoundTag compoundNBT) {
		SpawnGroupData spawnData = super.finalizeSpawn(serverWorld, difficultyInstance, spawnReason, spawnDataIn,
				compoundNBT);
		if (this instanceof ISmartCrossbowUser && ((ISmartCrossbowUser) this).isCrossbowUser()) {
			((ISmartCrossbowUser) this).setCrossbowUser(false);
		}
		if (spawnReason != MobSpawnType.STRUCTURE) {
			if (this.isAdult()) {
				this.setItemSlot(EquipmentSlot.MAINHAND, ModItems.BLUE_NETHERSHROOM.get().getDefaultInstance());
			}
		}
		return spawnData;
	}

	@Override
	protected void finishConversion(ServerLevel serverWorld) {
		PiglinHelper.stopAdmiringItem(this);
		((PiglinAccessor) this).getInventory().removeAllItems().forEach(this::spawnAtLocation);
		PiglinHelper.zombify(ModEntityTypes.ZOMBIFIED_FUNGUS_THROWER.get(), this);
	}

	@Override
	protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance p_180481_1_) {
		// NO-OP
	}

	@Override
	protected void populateDefaultEquipmentEnchantments(RandomSource randomSource, DifficultyInstance p_180483_1_) {
		// NO-OP
	}

	@Override
	protected boolean canReplaceCurrentItem(ItemStack replacement, ItemStack current) {
		boolean canReplaceCurrentItem = super.canReplaceCurrentItem(replacement, current);

		boolean takeReplacement = FungusThrowerAi.isBlueNethershroom(replacement);
		boolean keepCurrent = FungusThrowerAi.isBlueNethershroom(current);
		if (takeReplacement && !keepCurrent) {
			return true;
		} else if (!takeReplacement && keepCurrent) {
			return canReplaceCurrentItem;
		} else {
			return (!this.isAdult() || !FungusThrowerAi.isBlueNethershroom(replacement)
					|| !(FungusThrowerAi.isBlueNethershroom(current))) && canReplaceCurrentItem;
		}
	}

}
