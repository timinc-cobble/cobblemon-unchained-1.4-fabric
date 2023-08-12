package us.timinc.mc.cobblemon.chaining.modules

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.spawning.context.SpawningContext
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.ai.targeting.TargetingConditions
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

abstract class SpawnActionModifier(name: String) {
    private var logger: Logger

    init {
        logger = LogManager.getLogger(name)
    }

    abstract fun act(entity: PokemonEntity?, ctx: SpawningContext, props: PokemonProperties)

    fun info(msg: String, debug: Boolean) {
        if (!debug) return
        logger.info(msg)
    }

    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun getNearbyPlayers(ctx: SpawningContext, range: Double): List<ServerPlayer> {
        return ctx.world.getNearbyPlayers(
            TargetingConditions.forNonCombat().ignoreLineOfSight().ignoreInvisibilityTesting(), null, AABB.ofSize(
                Vec3.atCenterOf(ctx.position), range, range, range
            )
        ).mapNotNull { it as? ServerPlayer }
    }
}