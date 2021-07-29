package com.infamous.dungeons_mobs.client.renderer.creeper;

import com.infamous.dungeons_mobs.entities.creepers.IcyCreeperEntity;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import static com.infamous.dungeons_mobs.DungeonsMobs.MODID;

@OnlyIn(Dist.CLIENT)
public class CustomCreeperRenderer extends CreeperRenderer {
    private static final ResourceLocation ICY_CREEPER_TEXTURE = new ResourceLocation(MODID,"textures/entity/creeper/icy_creeper.png");

    public CustomCreeperRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager);
    }

    @Override
    public ResourceLocation getTextureLocation(CreeperEntity creeperEntity) {
        if(creeperEntity instanceof IcyCreeperEntity){
            return ICY_CREEPER_TEXTURE;
        }
        return super.getTextureLocation(creeperEntity);
    }
}
