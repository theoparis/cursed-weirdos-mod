package com.theoparis.cw.registry

import net.minecraft.sound.SoundEvent
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry

object CursedSounds {
    private val CREAMER_AMBIENT_ID: Identifier = Identifier("cursedweirdos:creamer_ambient")
    val CREAMER_AMBIENT = SoundEvent(CREAMER_AMBIENT_ID)

    private val CREAMER_HURT_ID: Identifier = Identifier("cursedweirdos:creamer_hurt")
    val CREAMER_HURT = SoundEvent(CREAMER_HURT_ID)

    fun register() {
        Registry.register(Registry.SOUND_EVENT, CREAMER_AMBIENT_ID, CREAMER_AMBIENT)
        Registry.register(Registry.SOUND_EVENT, CREAMER_HURT_ID, CREAMER_HURT)
    }
}
