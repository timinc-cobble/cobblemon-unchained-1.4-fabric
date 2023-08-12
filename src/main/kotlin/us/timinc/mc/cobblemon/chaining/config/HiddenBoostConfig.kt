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
    @Comment("The multiplier for the player's latest KO streak for a given species")
    val koStreakPoints = 100
    @Comment("The multiplier for the player's total KOs for a given species")
    val koCountPoints = 1
    @Comment("The multiplier for the player's latest capture streak for a given species")
    val captureStreakPoints = 0
    @Comment("The multiplier for the player's total captures for a given species")
    val captureCountPoints = 0

    @Comment("The distance at which a spawning Pokémon considers a player for this boost")
    val effectiveRange = 64

    @Comment("Thresholds for the points: {first/good marbles, second/total marbles}")
    val thresholds: Map<Int, Pair<Int, Int>> = mutableMapOf(99 to (1 to 5))

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

    fun getThreshold(points: Int): Pair<Int, Int>? {
        if (thresholds.isEmpty()) return null
        if (thresholds.size == 1) return thresholds.values.first()

        val targetThreshold = thresholds.entries
            .filter { it.key < points }
            .maxByOrNull { it.key }
        return targetThreshold?.value
    }
}