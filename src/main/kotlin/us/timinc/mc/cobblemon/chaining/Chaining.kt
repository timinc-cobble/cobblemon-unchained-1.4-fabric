package us.timinc.mc.cobblemon.chaining

import com.cobblemon.mod.common.api.spawning.context.SpawningContext
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import draylar.omegaconfig.OmegaConfig
import net.fabricmc.api.ModInitializer
import us.timinc.mc.cobblemon.chaining.config.HiddenBoostConfig
import us.timinc.mc.cobblemon.chaining.config.IvBoostConfig
import us.timinc.mc.cobblemon.chaining.config.ShinyBoostConfig
import us.timinc.mc.cobblemon.chaining.config.SynchronizedNaturesConfig
import us.timinc.mc.cobblemon.chaining.modules.HiddenBooster
import us.timinc.mc.cobblemon.chaining.modules.IvBooster
import us.timinc.mc.cobblemon.chaining.modules.ShinyBooster
import us.timinc.mc.cobblemon.chaining.modules.SynchronizedNatures

object Chaining : ModInitializer {
    const val MOD_ID = "cobblemon_chaining"

    private var shinyBoostConfig: ShinyBoostConfig = OmegaConfig.register(ShinyBoostConfig::class.java)
    private var ivBoostConfig: IvBoostConfig = OmegaConfig.register(IvBoostConfig::class.java)
    private var hiddenBoostConfig: HiddenBoostConfig = OmegaConfig.register(HiddenBoostConfig::class.java)
    private var synchronizedNaturesConfig: SynchronizedNaturesConfig =
        OmegaConfig.register(SynchronizedNaturesConfig::class.java)

    private lateinit var ivBooster: IvBooster
    private lateinit var shinyBooster: ShinyBooster
    private lateinit var hiddenBooster: HiddenBooster
    private lateinit var synchronizedNatures: SynchronizedNatures

    override fun onInitialize() {
        shinyBooster = ShinyBooster(shinyBoostConfig)
        ivBooster = IvBooster(ivBoostConfig)
        hiddenBooster = HiddenBooster(hiddenBoostConfig)
        synchronizedNatures = SynchronizedNatures(synchronizedNaturesConfig)
    }

    fun possiblyMakeShiny(entity: PokemonEntity, ctx: SpawningContext) {
        shinyBooster.act(entity, ctx)
    }

    fun possiblyModifyIvs(entity: PokemonEntity, ctx: SpawningContext) {
        ivBooster.act(entity, ctx)
    }

    fun possiblyAddHiddenAbility(entity: PokemonEntity, ctx: SpawningContext) {
        hiddenBooster.act(entity, ctx)
    }

    fun possiblySynchronizeNatures(entity: PokemonEntity, ctx: SpawningContext) {
        synchronizedNatures.act(entity, ctx)
    }
}