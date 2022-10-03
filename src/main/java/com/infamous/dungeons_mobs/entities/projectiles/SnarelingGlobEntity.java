package com.infamous.dungeons_mobs.entities.projectiles;

import com.infamous.dungeons_mobs.mod.ModEntityTypes;
import com.infamous.dungeons_mobs.mod.ModSoundEvents;
import com.infamous.dungeons_mobs.entities.ender.AbstractEnderlingEntity;
import com.infamous.dungeons_mobs.mod.ModEffects;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class SnarelingGlobEntity extends ProjectileItemEntity {
	
	   public SnarelingGlobEntity(EntityType<? extends SnarelingGlobEntity> p_i50159_1_, World p_i50159_2_) {
		      super(p_i50159_1_, p_i50159_2_);
		   }
	   
	   public SnarelingGlobEntity(World p_i1774_1_, LivingEntity p_i1774_2_) {
		      super(ModEntityTypes.SNARELING_GLOB.get(), p_i1774_2_, p_i1774_1_);
		   }

		   protected Item getDefaultItem() {
		      return Items.SLIME_BALL;
		   }

		   @OnlyIn(Dist.CLIENT)
		   private IParticleData getParticle() {
		      ItemStack itemstack = this.getItemRaw();
		      return (IParticleData)(itemstack.isEmpty() ? ParticleTypes.ITEM_SLIME : new ItemParticleData(ParticleTypes.ITEM, itemstack));
		   }

		   @OnlyIn(Dist.CLIENT)
		   public void handleEntityEvent(byte p_70103_1_) {
		      if (p_70103_1_ == 3) {
		         IParticleData iparticledata = this.getParticle();

		         for(int i = 0; i < 8; ++i) {
		            this.level.addParticle(iparticledata, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
		         }
		      }

		   }

		   protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
		      super.onHitEntity(p_213868_1_);
		      Entity entity = p_213868_1_.getEntity();
		      if (entity instanceof LivingEntity && !(entity instanceof AbstractEnderlingEntity)) {
		      entity.hurt(DamageSource.thrown(this, this.getOwner()), 3);
		      }
		      
		      if (entity instanceof LivingEntity && !entity.level.isClientSide) {
		      ((LivingEntity) entity).addEffect(new EffectInstance(ModEffects.ENSNARED.get(), 100));
		      }
		   }

		   protected void onHit(RayTraceResult p_70227_1_) {
		      super.onHit(p_70227_1_);
		      if (!this.level.isClientSide) {
		         this.level.broadcastEntityEvent(this, (byte)3);
		         this.remove();
		      }
		      
			  this.playSound(ModSoundEvents.SNARELING_GLOB_LAND.get(), 2.0F, 1.0F);

		   }
		   
		   @Override
		public IPacket<?> getAddEntityPacket() {
			return NetworkHooks.getEntitySpawningPacket(this);
		}
		}
