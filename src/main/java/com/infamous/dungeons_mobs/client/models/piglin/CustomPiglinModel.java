package com.infamous.dungeons_mobs.client.models.piglin;

import net.minecraft.client.renderer.entity.model.PiglinModel;
import net.minecraft.entity.MobEntity;

public class CustomPiglinModel<T extends MobEntity> extends PiglinModel<T> {

    public CustomPiglinModel(float p_i232336_1_, int p_i232336_2_, int p_i232336_3_) {
        super(p_i232336_1_, p_i232336_2_, p_i232336_3_);
    }

    @Override
    public void setupAnim(T piglinLike, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {
        super.setupAnim(piglinLike, p_225597_2_, p_225597_3_, p_225597_4_, p_225597_5_, p_225597_6_);
    }
}
