package com.theoparis.cw.entity.util

import net.minecraft.entity.mob.MobEntity

fun MobEntity.shouldPlayWalkAnim() = !navigation.isIdle || velocity.x > 0 || velocity.z > 0 || velocity.y > 0
