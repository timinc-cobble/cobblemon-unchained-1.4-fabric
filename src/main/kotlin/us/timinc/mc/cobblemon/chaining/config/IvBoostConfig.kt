@file:Suppress("MayBeConstant")

package us.timinc.mc.cobblemon.chaining.config

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment
import net.minecraft.world.entity.player.Player
import us.timinc.mc.cobblemon.chaining.Chaining
import us.timinc.mc.cobblemon.counter.Counter

@Suppress("MemberVisibilityCanBePrivate")
@Config(name = "${Chaining.MOD_ID}/ivBoost")
class IvBoostConfig : ConfigData {
    @Comment("The number of points each of these counter types grant")
    val koStreakPoints = 0
    val koCountPoints = 0
    val captureStreakPoints = 1
    val captureCountPoints = 0

    @Comment("The distance at which a spawning Pokémon considers a player for this boost")
    val effectiveRange = 64

    @Comment("Thresholds for the points : perfect IVs")
    val thresholds: Map<Int, Int> = mutableMapOf(Pair(5, 1), Pair(10, 2), Pair(20, 3), Pair(30, 4))

    @Comment("Turn this to true to see log output")
    val debug = false

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
        ) * captureCountPoints)
    }

    fun getThreshold(points: Int): Int {
        return thresholds.maxOfOrNull { entry -> if (entry.key < points) entry.value else 0 } ?: 0
    }
}