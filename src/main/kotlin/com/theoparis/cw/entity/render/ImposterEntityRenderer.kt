package com.theoparis.cw.entity.render

import com.theoparis.cw.entity.ImposterEntity
import com.theoparis.cw.entity.render.base.AnimatedEntityRenderer
import com.theoparis.cw.entity.render.base.feature.HeldItemFeature
import com.theoparis.cw.entity.render.model.ImposterModel
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.entity.EntityRenderDispatcher
import net.minecraft.client.util.math.Vector3f

@Environment(EnvType.CLIENT)
class ImposterEntityRenderer(entityRenderDispatcher: EntityRenderDispatcher) :
    AnimatedEntityRenderer<ImposterEntity>(
        entityRenderDispatcher,
        ImposterModel()
    ) {

    init {
        shadowRadius = 0.5f
        addFeature(HeldItemFeature(Vector3f(0.5f, 0.5f, 0f)))
    }

    companion object {
    }
}
