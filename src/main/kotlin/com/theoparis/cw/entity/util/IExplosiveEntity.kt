package com.theoparis.cw.entity.util

interface IExplosiveEntity {
    /**
     * Explosion fuse in ticks
     */
    var fuseTime: Int
    var fuseSpeed: Int
    fun ignite()
}
