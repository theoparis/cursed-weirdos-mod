package com.theoparis.cw

import com.theoparis.cw.entity.render.CreamerEntityRenderer
import com.theoparis.cw.entity.render.ImposterEntityRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.EntityRenderDispatcher
import org.apache.logging.log4j.LogManager
import software.bernie.geckolib3.GeckoLib

class CursedWeirdosClient : ClientModInitializer {
    companion object {
        @JvmStatic
        val logger = LogManager.getFormatterLogger(CursedWeirdosMod.modID)

        // Client-side registration
    }

    override fun onInitializeClient() {
        // Client-side initialization
        GeckoLib.initialize()

        EntityRendererRegistry.INSTANCE.register(
            CursedWeirdosMod.creamerEntity
        ) { dispatcher: EntityRenderDispatcher, _: EntityRendererRegistry.Context ->
            CreamerEntityRenderer(
                dispatcher
            )
        }

        EntityRendererRegistry.INSTANCE.register(
            CursedWeirdosMod.imposterEntity
        ) { dispatcher: EntityRenderDispatcher, _: EntityRendererRegistry.Context ->
            ImposterEntityRenderer(
                dispatcher
            )
        }
    }
}
