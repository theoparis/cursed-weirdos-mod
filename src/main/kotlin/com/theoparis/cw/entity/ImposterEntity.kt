package com.theoparis.cw.entity

import com.theoparis.cw.CursedWeirdosMod
import net.minecraft.entity.EntityType
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.world.World
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.PlayState
import software.bernie.geckolib3.core.builder.AnimationBuilder
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.event.predicate.AnimationEvent
import software.bernie.geckolib3.core.manager.AnimationData
import software.bernie.geckolib3.core.manager.AnimationFactory

class ImposterEntity(entityType: EntityType<out HostileEntity>?, world: World?) :
    HostileEntity(entityType, world),
    IAnimatable {
    private val factory = AnimationFactory(this)

    override fun initGoals() {
        goalSelector.add(1, SwimGoal(this))
        goalSelector.add(4, MeleeAttackGoal(this, 1.0, false))
        goalSelector.add(5, WanderAroundFarGoal(this, 0.8))
        goalSelector.add(6, LookAtEntityGoal(this, PlayerEntity::class.java, 8.0f))
        goalSelector.add(6, LookAroundGoal(this))
        targetSelector.add(
            1,
            ActiveTargetGoal(
                this,
                PlayerEntity::class.java, true
            )
        )
        targetSelector.add(2, RevengeGoal(this, *arrayOfNulls(0)))
    }

    private fun shouldPlayWalkAnim() = !navigation.isIdle || velocity.x > 0 || velocity.z > 0 || velocity.y > 0

    private fun <P : IAnimatable> predicate(event: AnimationEvent<P>): PlayState {
        event.controller.setAnimation(
            AnimationBuilder().addAnimation(
                "animation.imposter.walk",
                shouldPlayWalkAnim()
            )
        )
        return PlayState.CONTINUE
    }

    override fun registerControllers(data: AnimationData) {
        @Suppress("UNCHECKED_CAST")
        data.addAnimationController(
            AnimationController(
                this,
                "controller",
                0f
            ) { ev -> predicate(ev) }
        )
    }

    init {
        this.equipStack(EquipmentSlot.MAINHAND, ItemStack(Items.IRON_SWORD))
    }

    override fun dropEquipment(source: DamageSource?, lootingMultiplier: Int, allowDrops: Boolean) {
        super.dropEquipment(source, lootingMultiplier, allowDrops)
        dropItem {
            CursedWeirdosMod.totemOfLying
        }
    }

    override fun getFactory(): AnimationFactory {
        return factory
    }

    companion object {
        fun createAttributes(): DefaultAttributeContainer.Builder {
            return createHostileAttributes()
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35)
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0)
        }
    }
}
