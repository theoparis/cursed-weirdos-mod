package com.theoparis.cw.entity.render.base.feature

import com.theoparis.cw.entity.render.base.AnimatedFeatureRenderer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.model.json.ModelTransformation
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import net.minecraft.util.math.Vec3f
import software.bernie.geckolib3.core.IAnimatable

class HeldItemFeature<T>(private val translation: Vec3f) :
    AnimatedFeatureRenderer<T> where T : IAnimatable, T : LivingEntity {
    private val mc = MinecraftClient.getInstance()

    override fun render(
        entity: T,
        matrices: MatrixStack?,
        ticks: Float,
        provider: VertexConsumerProvider?,
        light: Int,
        overlay: Int
    ) {
        matrices?.push()
        matrices?.scale(0.75f, 0.75f, 0.75f)
        matrices?.translate(translation.x.toDouble(), translation.y.toDouble(), translation.z.toDouble())
        mc.itemRenderer.renderItem(
            entity,
            entity.mainHandStack,
            ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND,
            false,
            matrices,
            provider,
            entity.world,
            light,
            overlay,
            0
        )
        matrices?.pop()
/*        mc.itemRenderer.renderItem(
            entity,
            entity.offHandStack,
            ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND,
            true,
            matrices,
            provider,
            entity.world,
            light,
            1
        )*/
    }
}
