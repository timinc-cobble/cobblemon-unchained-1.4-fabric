package us.timinc.mc.cobblemon.chaining.config

import me.shedaniel.autoconfig.ConfigData
import me.shedaniel.autoconfig.annotation.Config
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment
import net.minecraft.world.entity.player.Player
import us.timinc.mc.cobblemon.chaining.Chaining
import us.timinc.mc.cobblemon.counter.Counter

@Suppress("MemberVisibilityCanBePrivate")
@Config(name = "${Chaining.MOD_ID}/hiddenBoost")
class HiddenBoostConfig : ConfigData {
    @Comment("The number of points each of these counter types grant")
    val koStreakPoints = 10
    val koCountPoints = 1
    val captureStreakPoints = 0
    val captureCountPoints = 0

    @Comment("The distance at which a spawning Pokémon considers a player for this boost")
    val effectiveRange = 64

    @Comment("Thresholds for the points: {first/good marbles, second/total marbles}")
    val thresholds: Map<Int, Pair<Int, Int>> = mutableMapOf(10 to (1 to 2))

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

    fun getThreshold(points: Int): Pair<Int, Int>? {
        if (thresholds.isEmpty()) return null
        if (thresholds.size == 1) return thresholds.values.first()

        val targetThreshold = thresholds.entries
            .filter { it.key < points }
            .maxByOrNull { it.key }
        return targetThreshold?.value
    }
}