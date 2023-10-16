@file:Suppress("MemberVisibilityCanBePrivate")

package us.timinc.mc.cobblemon.chaining.config

import com.google.gson.GsonBuilder
import net.minecraft.entity.player.PlayerEntity
import us.timinc.mc.cobblemon.chaining.Chaining
import us.timinc.mc.cobblemon.counter.Counter
import java.io.File
import java.io.FileReader
import java.io.PrintWriter

class SynchronizedNaturesConfig {
    //    @Comment("The multiplier for the player's latest KO streak for a given species")
    val koStreakPoints = 1

    //    @Comment("The multiplier for the player's total KOs for a given species")
    val koCountPoints = 0

    //    @Comment("The multiplier for the player's latest capture streak for a given species")
    val captureStreakPoints = 0

    //    @Comment("The multiplier for the player's total captures for a given species")
    val captureCountPoints = 0

    //    @Comment("The distance at which a spawning Pokémon considers a player for this boost")
    val effectiveRange = 64

    //    @Comment("Whether or not the Pokemon with synchronize must be the first in your party in order to be considered")
    val mustBeFirst = true

    //    @Comment("Turn this to true to see log output")
    val debug = false

    //    @Comment("A list of Pokémon species and form labels to ignore")
    val blacklist = mutableSetOf<String>()

    //    @Comment("A list of Pokémon species and form labels to exclusively consider")
    val whitelist = mutableSetOf<String>()

    val percentChance = 0.5;

    @Suppress("KotlinConstantConditions")
    fun getPoints(player: PlayerEntity, species: String): Int {
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

    class Builder {
        companion object {
            fun load() : SynchronizedNaturesConfig {
                val gson = GsonBuilder()
                    .disableHtmlEscaping()
                    .setPrettyPrinting()
                    .create()

                var config = SynchronizedNaturesConfig()
                val configFile = File("config/${Chaining.MOD_ID}/synchronizedNatures.json")
                configFile.parentFile.mkdirs()

                if (configFile.exists()) {
                    try {
                        val fileReader = FileReader(configFile)
                        config = gson.fromJson(fileReader, SynchronizedNaturesConfig::class.java)
                        fileReader.close()
                    } catch (e: Exception) {
                        println("Error reading config file")
                    }
                }

                val pw = PrintWriter(configFile)
                gson.toJson(config, pw)
                pw.close()

                return config
            }
        }
    }
}