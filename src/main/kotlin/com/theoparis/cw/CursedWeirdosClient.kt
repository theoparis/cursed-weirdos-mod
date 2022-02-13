package com.theoparis.cw

import com.theoparis.cw.entity.render.CreamerEntityRenderer
import com.theoparis.cw.entity.render.ImposterEntityRenderer
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry
import net.minecraft.client.render.entity.EntityRendererFactory
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

        EntityRendererRegistry.register(
            CursedWeirdosMod.creamerEntity
        ) { ctx: EntityRendererFactory.Context ->
            CreamerEntityRenderer(
                ctx
            )
        }

        EntityRendererRegistry.register(
            CursedWeirdosMod.imposterEntity
        ) { ctx: EntityRendererFactory.Context ->
            ImposterEntityRenderer(
                ctx
            )
        }
    }
}
