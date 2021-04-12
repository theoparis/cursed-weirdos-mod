package com.theoparis.example

import net.fabricmc.api.ClientModInitializer
import org.apache.logging.log4j.LogManager

class ExampleClient : ClientModInitializer {
    companion object {
        @JvmStatic
        val logger = LogManager.getFormatterLogger(InfiniteInsanityMod.MOD_ID)

        // Client-side registration

    }

    override fun onInitializeClient() {
        // Client-side initialization
    }
}
