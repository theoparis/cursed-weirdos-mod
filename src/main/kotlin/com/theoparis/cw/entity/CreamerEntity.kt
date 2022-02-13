package com.theoparis.cw.entity

import com.theoparis.cw.CursedWeirdosMod
import com.theoparis.cw.entity.goal.ExplosiveIgniteGoal
import com.theoparis.cw.entity.util.IExplosiveEntity
import com.theoparis.cw.entity.util.shouldPlayWalkAnim
import com.theoparis.cw.registry.CursedSounds
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment
import net.fabricmc.api.EnvironmentInterface
import net.fabricmc.api.EnvironmentInterfaces
import net.minecraft.client.render.entity.feature.SkinOverlayOwner
import net.minecraft.entity.AreaEffectCloudEntity
import net.minecraft.entity.Entity
import net.minecraft.entity.EntityType
import net.minecraft.entity.LightningEntity
import net.minecraft.entity.ai.goal.*
import net.minecraft.entity.attribute.DefaultAttributeContainer
import net.minecraft.entity.attribute.EntityAttributes
import net.minecraft.entity.damage.DamageSource
import net.minecraft.entity.data.DataTracker
import net.minecraft.entity.data.TrackedData
import net.minecraft.entity.data.TrackedDataHandlerRegistry
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.mob.HostileEntity
import net.minecraft.entity.passive.CatEntity
import net.minecraft.entity.passive.OcelotEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Items
import net.minecraft.nbt.NbtCompound
import net.minecraft.particle.ParticleTypes
import net.minecraft.server.world.ServerWorld
import net.minecraft.sound.SoundEvent
import net.minecraft.sound.SoundEvents
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.math.MathHelper
import net.minecraft.world.GameRules
import net.minecraft.world.World
import net.minecraft.world.explosion.Explosion.DestructionType
import software.bernie.geckolib3.core.IAnimatable
import software.bernie.geckolib3.core.PlayState
import software.bernie.geckolib3.core.builder.AnimationBuilder
import software.bernie.geckolib3.core.controller.AnimationController
import software.bernie.geckolib3.core.event.predicate.AnimationEvent
import software.bernie.geckolib3.core.manager.AnimationData
import software.bernie.geckolib3.core.manager.AnimationFactory

@EnvironmentInterfaces(EnvironmentInterface(value = EnvType.CLIENT, itf = SkinOverlayOwner::class))
class CreamerEntity(entityType: EntityType<out HostileEntity>, world: World) :
    HostileEntity(entityType, world),
    IExplosiveEntity,
    SkinOverlayOwner,
    IAnimatable {
    private var lastFuseTime = 0
    private var currentFuseTime = 0
    override var fuseTime = 30

    private var explosionRadius = 3
    private var headsDropped = 0
    private val factory = AnimationFactory(this)

    init {
        ignoreCameraFrustum = true
    }

    override fun initGoals() {
        goalSelector.add(1, SwimGoal(this))
        goalSelector.add(2, ExplosiveIgniteGoal(this))
        goalSelector.add(3, FleeEntityGoal(this, OcelotEntity::class.java, 6.0f, 1.0, 1.2))
        goalSelector.add(3, FleeEntityGoal(this, CatEntity::class.java, 6.0f, 1.0, 1.2))
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

    override fun getSafeFallDistance(): Int {
        return if (target == null) 3 else 3 + (this.health - 1.0f).toInt()
    }

    override fun handleFallDamage(fallDistance: Float, damageMultiplier: Float, source: DamageSource): Boolean {
        val bl = super.handleFallDamage(fallDistance, damageMultiplier, source)
        currentFuseTime = (currentFuseTime.toFloat() + fallDistance * 1.5f).toInt()
        if (currentFuseTime > fuseTime - 5) {
            currentFuseTime = fuseTime - 5
        }
        return bl
    }

    override fun initDataTracker() {
        super.initDataTracker()
        dataTracker.startTracking(FUSE_SPEED, -1)
        dataTracker.startTracking(CHARGED, false)
        dataTracker.startTracking(IGNITED, false)
    }

    override fun writeCustomDataToNbt(tag: NbtCompound) {
        super.writeCustomDataToNbt(tag)
        if (dataTracker.get(CHARGED) as Boolean)
            tag.putBoolean("powered", true)

        tag.putShort("fuse", fuseTime.toShort())
        tag.putByte("explosionRadius", explosionRadius.toByte())
        tag.putBoolean("ignited", ignited)
    }

    override fun readCustomDataFromNbt(tag: NbtCompound) {
        super.readCustomDataFromNbt(tag)
        dataTracker.set(CHARGED, tag.getBoolean("powered"))
        if (tag.contains("fuse", 99))
            fuseTime = tag.getShort("fuse").toInt()

        if (tag.contains("explosionRadius", 99))
            explosionRadius = tag.getByte("explosionRadius").toInt()

        if (tag.getBoolean("ignited"))
            ignite()
    }

    override fun tick() {
        if (this.isAlive) {
            lastFuseTime = currentFuseTime
            if (ignited)
                fuseSpeed = 1
            val i = fuseSpeed
            if (i > 0 && currentFuseTime == 0)
                playSound(SoundEvents.ENTITY_CREEPER_PRIMED, 1.0f, 0.5f)
            currentFuseTime += i
            if (currentFuseTime < 0)
                currentFuseTime = 0
            if (currentFuseTime > fuseTime / 2)
                world.addParticle(ParticleTypes.CLOUD, true, pos.x, pos.y, pos.z, 0.1, 0.2, 0.1)
            if (currentFuseTime >= fuseTime) {
                currentFuseTime = fuseTime
                explode()
            }
        }
        super.tick()
    }

    override fun getHurtSound(source: DamageSource): SoundEvent {
        return CursedSounds.CREAMER_HURT
    }

    override fun getAmbientSound(): SoundEvent {
        return CursedSounds.CREAMER_AMBIENT
    }

    override fun getDeathSound(): SoundEvent {
        return CursedSounds.CREAMER_HURT
    }

    override fun dropEquipment(source: DamageSource, lootingMultiplier: Int, allowDrops: Boolean) {
        super.dropEquipment(source, lootingMultiplier, allowDrops)
        val entity = source.attacker
        if (entity !== this)
            this.dropItem(CursedWeirdosMod.cucummberItem)
    }

    override fun tryAttack(target: Entity): Boolean {
        return true
    }

    override fun shouldRenderOverlay(): Boolean {
        return dataTracker.get(CHARGED) as Boolean
    }

    @Environment(EnvType.CLIENT)
    fun getClientFuseTime(timeDelta: Float): Float {
        return MathHelper.lerp(timeDelta, lastFuseTime.toFloat(), currentFuseTime.toFloat()) / (fuseTime - 2).toFloat()
    }

    override var fuseSpeed: Int
        get() = dataTracker.get(FUSE_SPEED) as Int
        set(fuseSpeed) {
            dataTracker.set(FUSE_SPEED, fuseSpeed)
        }

    override fun onStruckByLightning(world: ServerWorld, lightning: LightningEntity) {
        super.onStruckByLightning(world, lightning)
        dataTracker.set(CHARGED, true)
    }

    override fun interactMob(player: PlayerEntity, hand: Hand): ActionResult {
        val itemStack = player.getStackInHand(hand)
        return if (itemStack.item === Items.FLINT_AND_STEEL) {
            world.playSound(
                player,
                this.x,
                this.y,
                this.z,
                SoundEvents.ITEM_FLINTANDSTEEL_USE,
                this.soundCategory,
                1.0f,
                random.nextFloat() * 0.4f + 0.8f
            )
            if (!world.isClient) {
                ignite()
                itemStack.damage(
                    1, player
                ) { playerEntity: PlayerEntity ->
                    playerEntity.sendToolBreakStatus(
                        hand
                    )
                }
            }
            ActionResult.success(world.isClient)
        } else
            super.interactMob(player, hand)
    }

    private fun explode() {
        if (!world.isClient) {
            val destructionType =
                if (world.gameRules.getBoolean(GameRules.DO_MOB_GRIEFING)) DestructionType.DESTROY else DestructionType.NONE
            val f = if (shouldRenderOverlay()) 2.0f else 1.0f
            dead = true
            world.createExplosion(this, this.x, this.y, this.z, explosionRadius.toFloat() * f, destructionType)
            this.remove(RemovalReason.KILLED)
            spawnEffectsCloud()
        }
    }

    private fun spawnEffectsCloud() {
        val collection = this.statusEffects
        if (!collection.isEmpty()) {
            val areaEffectCloudEntity = AreaEffectCloudEntity(world, this.x, this.y, this.z)
            areaEffectCloudEntity.radius = 2.5f
            areaEffectCloudEntity.radiusOnUse = -0.5f
            areaEffectCloudEntity.waitTime = 10
            areaEffectCloudEntity.duration = areaEffectCloudEntity.duration / 2
            areaEffectCloudEntity.radiusGrowth = -areaEffectCloudEntity.radius / areaEffectCloudEntity.duration
                .toFloat()
            val var3: Iterator<*> = collection.iterator()
            while (var3.hasNext()) {
                val statusEffectInstance = var3.next() as StatusEffectInstance
                areaEffectCloudEntity.addEffect(StatusEffectInstance(statusEffectInstance))
            }
            world.spawnEntity(areaEffectCloudEntity)
        }
    }

    val ignited: Boolean
        get() = dataTracker.get(IGNITED) as Boolean

    override fun ignite() {
        dataTracker.set(IGNITED, true)
    }

    companion object {
        private var FUSE_SPEED: TrackedData<Int>? = null
        private var CHARGED: TrackedData<Boolean>? = null
        private var IGNITED: TrackedData<Boolean>? = null

        fun createAttributes(): DefaultAttributeContainer.Builder {
            return createHostileAttributes().add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25)
        }

        init {
            FUSE_SPEED = DataTracker.registerData(CreamerEntity::class.java, TrackedDataHandlerRegistry.INTEGER)
            CHARGED = DataTracker.registerData(CreamerEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
            IGNITED = DataTracker.registerData(CreamerEntity::class.java, TrackedDataHandlerRegistry.BOOLEAN)
        }
    }

    private fun predicate(event: AnimationEvent<CreamerEntity>): PlayState {
        event.controller.setAnimation(
            AnimationBuilder().addAnimation(
                "animation.creamer.walk",
                this.shouldPlayWalkAnim()
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

    override fun getFactory(): AnimationFactory {
        return factory
    }
}
