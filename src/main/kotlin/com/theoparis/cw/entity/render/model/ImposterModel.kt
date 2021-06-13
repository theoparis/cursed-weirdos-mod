package com.theoparis.cw.entity.render.model

import com.theoparis.cw.CursedWeirdosMod
import com.theoparis.cw.entity.ImposterEntity
import net.minecraft.util.Identifier
import software.bernie.geckolib3.model.AnimatedGeoModel

// Made with Blockbench 3.7.4
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports
class ImposterModel : AnimatedGeoModel<ImposterEntity>() {

    companion object {
        @JvmStatic
        fun getTexture() = Identifier(CursedWeirdosMod.modID, "textures/entity/imposter_gold.png")
    }

    override fun getModelLocation(obj: ImposterEntity): Identifier {
        return Identifier(CursedWeirdosMod.modID, "geo/imposter.geo.json")
    }

    override fun getTextureLocation(obj: ImposterEntity): Identifier = getTexture()

    override fun getAnimationFileLocation(obj: ImposterEntity): Identifier {
        return Identifier(CursedWeirdosMod.modID, "animations/imposter.animation.json")
    }
}
