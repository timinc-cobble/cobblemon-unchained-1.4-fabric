@file:Suppress("MemberVisibilityCanBePrivate")

package us.timinc.mc.cobblemon.chaining.config

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment
import net.minecraft.world.entity.player.Player
import us.timinc.mc.cobblemon.chaining.Chaining
import us.timinc.mc.cobblemon.counter.Counter

@Config(name = "${Chaining.MOD_ID}/synchronizedNatures")
class SynchronizedNaturesConfig : ConfigData {
    @Comment("The number of points each of these counter types grant")
    val koStreakPoints = 1
    val koCountPoints = 0
    val captureStreakPoints = 0
    val captureCountPoints = 0

    @Suppress("KotlinConstantConditions")
    fun getPoints(player: Player, species: String): Int {
        return (Counter.getPlayerKoStreak(
            player, species
        ) * koStreakPoints) + (Counter.getPlayerKoCount(
            player, species
        ) * koCountPoints) + (Counter.getPlayerCaptureStreak(
            player, species
        ) * captureStreakPoints) + (Counter.getPlayerCaptureCount(
            player, species
        ) * captureCountPoints) + 1
    }

    @Comment("The distance at which a spawning Pokémon considers a player for this boost")
    val effectiveRange = 64

    @Comment("Whether or not the Pokemon with synchronize must be the first in your party in order to be considered")
    val mustBeFirst = true

    @Comment("Turn this to true to see log output")
    val debug = false
}