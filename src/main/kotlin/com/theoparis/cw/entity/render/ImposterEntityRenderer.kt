package com.theoparis.cw.entity.render

import com.theoparis.cw.entity.ImposterEntity
import com.theoparis.cw.entity.render.base.AnimatedEntityRenderer
import com.theoparis.cw.entity.render.base.feature.HeldItemFeature
import com.theoparis.cw.entity.render.model.ImposterModel
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.util.math.Vec3f

@Environment(EnvType.CLIENT)
class ImposterEntityRenderer(ctx:  EntityRendererFactory.Context) :
    AnimatedEntityRenderer<ImposterEntity>(
        ctx,
        ImposterModel()
    ) {

    init {
        shadowRadius = 0.5f
        addFeature(HeldItemFeature(Vec3f(0.5f, 0.5f, 0f)))
    }

    companion object
}
