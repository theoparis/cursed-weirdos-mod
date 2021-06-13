package com.theoparis.cw.entity.goal

import com.theoparis.cw.entity.util.IExplosiveEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.ai.goal.Goal
import net.minecraft.entity.mob.MobEntity
import java.util.EnumSet

class ExplosiveIgniteGoal(private val entity: MobEntity) : Goal() {
    private var target: LivingEntity? = null
    private var explosiveEntity: IExplosiveEntity

    override fun canStart(): Boolean {
        val livingEntity = entity.target
        return if (livingEntity != null) explosiveEntity.fuseSpeed > 0 || entity.squaredDistanceTo(livingEntity) < 9.0
        else false
    }

    override fun start() {
        entity.navigation.stop()
        target = entity.target
    }

    init {
        require(entity is IExplosiveEntity) {
            "${entity.uuid} must be an instance of IExplosiveEntity!"
        }
        explosiveEntity = entity
    }

    override fun stop() {
        target = null
    }

    override fun tick() {
        if (target == null) {
            explosiveEntity.fuseSpeed = -1
        } else if (entity.squaredDistanceTo(target) > 49.0) {
            explosiveEntity.fuseSpeed = -1
        } else if (!entity.visibilityCache.canSee(target)) {
            explosiveEntity.fuseSpeed = -1
        } else {
            explosiveEntity.fuseSpeed = 1
        }
    }

    init {
        controls = EnumSet.of(Control.MOVE)
    }
}
