package us.timinc.mc.cobblemon.chaining.modules

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.spawning.context.SpawningContext
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger

abstract class SpawnActionModifier(name: String) {
    var logger: Logger

    init {
        logger = LogManager.getLogger(name)
    }

    abstract fun act(entity: PokemonEntity?, ctx: SpawningContext, props: PokemonProperties)

    fun info(msg: String) {
        logger.info(msg)
    }
}