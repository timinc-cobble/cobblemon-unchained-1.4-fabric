package us.timinc.mc.cobblemon.chaining.modules

import com.cobblemon.mod.common.api.spawning.context.SpawningContext
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.server.network.ServerPlayerEntity
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import kotlin.math.sqrt

abstract class SpawnActionModifier(name: String) {
    private var logger: Logger

    init {
        logger = LogManager.getLogger(name)
    }

    abstract fun act(entity: PokemonEntity, ctx: SpawningContext)

    fun info(msg: String, debug: Boolean) {
        if (!debug) return
        logger.info(msg)
    }

    fun getNearbyPlayers(ctx: SpawningContext, range: Double): List<ServerPlayerEntity> {
        return ctx.world.getPlayers { playerEntity ->
            sqrt(playerEntity.squaredDistanceTo(ctx.position.toCenterPos())) < range
        }
    }
}
