package us.timinc.mc.cobblemon.chaining

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.spawning.context.SpawningContext
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import me.shedaniel.autoconfig.AutoConfig
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer
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

    private lateinit var shinyBoostConfig: ShinyBoostConfig
    private lateinit var ivBoostConfig: IvBoostConfig
    private lateinit var hiddenBoostConfig: HiddenBoostConfig
    private lateinit var synchronizedNaturesConfig: SynchronizedNaturesConfig

    private lateinit var ivBooster: IvBooster
    private lateinit var shinyBooster: ShinyBooster
    private lateinit var hiddenBooster: HiddenBooster
    private lateinit var synchronizedNatures: SynchronizedNatures

    override fun onInitialize() {
        AutoConfig.register(ShinyBoostConfig::class.java) { definition, configClass ->
            JanksonConfigSerializer(definition, configClass)
        }
        shinyBoostConfig = AutoConfig.getConfigHolder(ShinyBoostConfig::class.java).config
        shinyBooster = ShinyBooster(shinyBoostConfig)

        AutoConfig.register(IvBoostConfig::class.java) { definition, configClass ->
            JanksonConfigSerializer(definition, configClass)
        }
        ivBoostConfig = AutoConfig.getConfigHolder(IvBoostConfig::class.java).config
        ivBooster = IvBooster(ivBoostConfig)

        AutoConfig.register(HiddenBoostConfig::class.java) { definition, configClass ->
            JanksonConfigSerializer(definition, configClass)
        }
        hiddenBoostConfig = AutoConfig.getConfigHolder(HiddenBoostConfig::class.java).config
        hiddenBooster = HiddenBooster(hiddenBoostConfig)

        AutoConfig.register(SynchronizedNaturesConfig::class.java) { definition, configClass ->
            JanksonConfigSerializer(definition, configClass)
        }
        synchronizedNaturesConfig = AutoConfig.getConfigHolder(SynchronizedNaturesConfig::class.java).config
        synchronizedNatures = SynchronizedNatures(synchronizedNaturesConfig)
    }

    fun possiblyMakeShiny(ctx: SpawningContext, props: PokemonProperties) {
        shinyBooster.act(null, ctx, props)
    }

    fun possiblyModifyIvs(entity: PokemonEntity, ctx: SpawningContext, props: PokemonProperties) {
        ivBooster.act(entity, ctx, props)
    }

    fun possiblyAddHiddenAbility(entity: PokemonEntity, ctx: SpawningContext, props: PokemonProperties) {
        hiddenBooster.act(entity, ctx, props)
    }

    fun possiblySynchronizeNatures(ctx: SpawningContext, props: PokemonProperties) {
        synchronizedNatures.act(null, ctx, props)
    }
}