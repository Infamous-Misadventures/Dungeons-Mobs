package com.infamous.dungeons_mobs.client.renderer.undead;

import com.infamous.dungeons_mobs.entities.undead.ArmoredZombieEntity;
import com.infamous.dungeons_mobs.entities.undead.FrozenZombieEntity;
import com.infamous.dungeons_mobs.entities.undead.JungleZombieEntity;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.entity.monster.HuskEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.util.ResourceLocation;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

public class CustomZombieRenderer extends ZombieRenderer {

    private static final ResourceLocation JUNGLE_ZOMBIE_TEXUTRE = new ResourceLocation(MODID, "textures/entity/zombie/jungle_zombie.png");
    private static final ResourceLocation FROZEN_ZOMBIE_TEXTURE = new ResourceLocation(MODID, "textures/entity/zombie/frozen_zombie.png");
    private static final ResourceLocation HUSK_ZOMBIE_TEXTURE = new ResourceLocation("textures/entity/zombie/husk.png");

    public CustomZombieRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    @Override
    protected void scale(ZombieEntity zombieEntity, MatrixStack matrixStack, float v) {
        if(zombieEntity instanceof ArmoredZombieEntity){
            float scaleFactor = 1.1F;
            matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        }
        if(zombieEntity instanceof HuskEntity){
            float scaleFactor = 1.2F;
            matrixStack.scale(scaleFactor, scaleFactor, scaleFactor);
        }
        super.scale(zombieEntity, matrixStack, v);
    }

    public ResourceLocation getTextureLocation(ZombieEntity zombieEntity) {
        if(zombieEntity instanceof JungleZombieEntity){
            return JUNGLE_ZOMBIE_TEXUTRE;
        }
        else if(zombieEntity instanceof FrozenZombieEntity){
            return FROZEN_ZOMBIE_TEXTURE;
        }
        else if(zombieEntity instanceof HuskEntity){
            return HUSK_ZOMBIE_TEXTURE;
        }
        else {
            return super.getTextureLocation(zombieEntity);
        }
    }
}
