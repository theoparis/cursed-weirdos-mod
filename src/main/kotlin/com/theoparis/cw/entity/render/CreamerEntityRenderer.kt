package com.theoparis.cw.entity.render

import com.theoparis.cw.entity.CreamerEntity
import com.theoparis.cw.entity.render.model.CreamerModel
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.MathHelper
import net.minecraft.util.math.Vec3f
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer

@Environment(EnvType.CLIENT)
class CreamerEntityRenderer(ctx: EntityRendererFactory.Context) :
    GeoEntityRenderer<CreamerEntity>(
        ctx,
        CreamerModel()
    ) {

    init {
        shadowRadius = 0.5f
    }

    fun getScale(animatable: CreamerEntity?, partialTicks: Float): Vec3f {
        if (animatable == null) return Vec3f()
        var g: Float = animatable.getClientFuseTime(partialTicks)
        val h = 1.0f + MathHelper.sin(g * 100.0f) * g * 0.01f
        g = MathHelper.clamp(g, 0.0f, 1.0f)
        g *= g
        g *= g
        val i = (1.0f + g * 0.4f) * h
        val j = (1.0f + g * 0.1f) / h
        return Vec3f(i, j, i)
    }

    override fun renderEarly(
        animatable: CreamerEntity?,
        stackIn: MatrixStack?,
        ticks: Float,
        renderTypeBuffer: VertexConsumerProvider?,
        vertexBuilder: VertexConsumer?,
        packedLightIn: Int,
        packedOverlayIn: Int,
        red: Float,
        green: Float,
        blue: Float,
        partialTicks: Float
    ) {
        val sc = getScale(animatable, partialTicks)
        stackIn?.scale(sc.x, sc.y, sc.z)

        super.renderEarly(
            animatable,
            stackIn,
            ticks,
            renderTypeBuffer,
            vertexBuilder,
            packedLightIn,
            packedOverlayIn,
            red,
            green,
            blue,
            partialTicks
        )
    }

    init {
        // FEATURES
    }
}
