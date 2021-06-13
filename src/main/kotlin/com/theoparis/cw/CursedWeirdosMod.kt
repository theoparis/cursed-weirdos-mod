package com.theoparis.cw

import com.theoparis.cw.entity.CreamerEntity
import com.theoparis.cw.entity.ImposterEntity
import com.theoparis.cw.item.CreamJarItem
import com.theoparis.cw.registry.CursedSounds
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricDefaultAttributeRegistry
import net.fabricmc.fabric.api.`object`.builder.v1.entity.FabricEntityTypeBuilder
import net.fabricmc.fabric.api.biome.v1.BiomeModifications
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors
import net.minecraft.entity.EntityDimensions
import net.minecraft.entity.EntityType
import net.minecraft.entity.SpawnGroup
import net.minecraft.item.Item
import net.minecraft.item.ItemGroup
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import net.minecraft.world.biome.Biome
import org.apache.logging.log4j.LogManager

class CursedWeirdosMod : ModInitializer {
    companion object {
        @JvmStatic
        val modID = "cursedweirdos"

        val creamJarItem = CreamJarItem(Item.Settings().group(ItemGroup.FOOD))
        val cucummberItem = Item(Item.Settings().group(ItemGroup.FOOD))
        val totemOfLying = Item(Item.Settings().group(ItemGroup.MISC))

        val creamerEntity: EntityType<CreamerEntity> = Registry.register(
            Registry.ENTITY_TYPE,
            Identifier(modID, "creamer"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ::CreamerEntity)
                .dimensions(
                    EntityDimensions.fixed(0.6f, 1.7f)
                )
                .trackRangeBlocks(8)
                .build()
        )

        val imposterEntity: EntityType<ImposterEntity> = Registry.register(
            Registry.ENTITY_TYPE,
            Identifier(modID, "imposter"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ::ImposterEntity)
                .dimensions(
                    EntityDimensions.fixed(1f, 1f)
                )
                .trackRangeBlocks(8)
                .build()
        )

        @JvmStatic
        val logger = LogManager.getFormatterLogger(modID)

        // Define your registry items here (eg. blocks)
    }

    override fun onInitialize() {
        Registry.register(Registry.ITEM, Identifier(modID, "cream_jar"), creamJarItem)
        Registry.register(Registry.ITEM, Identifier(modID, "cucummber"), cucummberItem)
        Registry.register(Registry.ITEM, Identifier(modID, "totem_of_lying"), totemOfLying)
        FabricDefaultAttributeRegistry.register(creamerEntity, CreamerEntity.createAttributes())
        FabricDefaultAttributeRegistry.register(imposterEntity, ImposterEntity.createAttributes())
        CursedSounds.register()
        BiomeModifications.addSpawn(
            BiomeSelectors.foundInOverworld()
                .and(
                    BiomeSelectors.categories(
                        Biome.Category.PLAINS,
                        Biome.Category.FOREST,
                        Biome.Category.TAIGA,
                        Biome.Category.EXTREME_HILLS
                    )
                ),
            SpawnGroup.MONSTER,
            creamerEntity,
            15,
            1,
            3
        )
        BiomeModifications.addSpawn(
            BiomeSelectors.foundInOverworld()
                .and(
                    BiomeSelectors.categories(
                        Biome.Category.PLAINS,
                        Biome.Category.FOREST,
                        Biome.Category.TAIGA,
                        Biome.Category.EXTREME_HILLS
                    )
                ),
            SpawnGroup.MONSTER,
            imposterEntity,
            11,
            1,
            2
        )
    }
}
