package com.theoparis.cw.entity.render.base

import net.minecraft.client.render.VertexConsumer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.EntityRendererFactory
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.LivingEntity
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.model.AnimatedGeoModel
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer

open class AnimatedEntityRenderer<T>(ctx: EntityRendererFactory.Context, modelProvider: AnimatedGeoModel<T>) :
    GeoEntityRenderer<T>(ctx, modelProvider) where
T : LivingEntity,
T : IAnimatable {
    private val features = mutableListOf<AnimatedFeatureRenderer<T>>()

    fun getFeatures() = features.toList()

    fun addFeature(newFeature: AnimatedFeatureRenderer<T>): Boolean = features.add(newFeature)

    override fun renderEarly(
        animatable: T,
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

        features.forEach {
            it.render(animatable, stackIn, partialTicks, renderTypeBuffer, packedLightIn, packedOverlayIn)
        }
    }
}

interface AnimatedFeatureRenderer<T> where
T : LivingEntity,
T : IAnimatable {
    fun render(
        entity: T,
        matrices: MatrixStack?,
        ticks: Float,
        provider: VertexConsumerProvider?,
        light: Int,
        overlay: Int
    )
}
