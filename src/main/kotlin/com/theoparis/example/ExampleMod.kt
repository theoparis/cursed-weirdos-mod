package com.theoparis.example

import net.fabricmc.api.ModInitializer
import org.apache.logging.log4j.LogManager


class ExampleMod : ModInitializer {
    companion object {
        @JvmStatic
        val MOD_ID = "example"

        @JvmStatic
        val logger = LogManager.getFormatterLogger(MOD_ID)

        // Define your registry items here (eg. blocks)

    }

    override fun onInitialize() {

    }
}
