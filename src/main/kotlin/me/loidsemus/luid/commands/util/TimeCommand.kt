package me.loidsemus.luid.commands.util

import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.PendingResult
import com.google.maps.TimeZoneApi
import com.google.maps.model.GeocodingResult
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class TimeCommand : Command() {

    private val apiKey = System.getenv("google_apikey")
    private val context: GeoApiContext

    init {
        context = GeoApiContext.Builder()
            .apiKey(apiKey)
            .build()
    }

    init {
        with(this) {
            name = "time"
            help = "Check time in some timezone"
            aliases = arrayOf("timezone", "tz")
        }
    }

    override fun execute(event: CommandEvent) {
        if (event.args.isNullOrEmpty()) {
            event.reply("You need to specify a place")
            return
        }

        event.reply("Getting location...") {
            GeocodingApi.geocode(context, event.args).setCallback(
                object : PendingResult.Callback<Array<GeocodingResult>> {
                    override fun onFailure(e: Throwable) {
                        it.editMessage("Error").queue()
                        e.printStackTrace()
                    }

                    override fun onResult(result: Array<GeocodingResult>) {
                        if (result.isEmpty()) {
                            it.editMessage("Invalid location: ${event.args}").queue()
                            return
                        }

                        it.editMessage("Getting timezone...").queue()

                        val geocodingResult = result[0]
                        TimeZoneApi.getTimeZone(context, geocodingResult.geometry.location).setCallback(
                            object : PendingResult.Callback<TimeZone> {
                                override fun onFailure(e: Throwable) {
                                    it.editMessage("Error").queue()
                                    e.printStackTrace()
                                }

                                override fun onResult(result: TimeZone) {
                                    it.editMessage(
                                        "**Time in ${geocodingResult.formattedAddress} " +
                                                "(${result.getDisplayName(Locale.ENGLISH)})**\n" +
                                                getCurrentTimeFormatted(result)
                                    ).queue()
                                }
                            }
                        )
                    }
                }
            )
        }

    }

    private fun getCurrentTimeFormatted(timeZone: TimeZone): String {
        val localTime = LocalDateTime.now(timeZone.toZoneId())
        val formatter = DateTimeFormatter.ofPattern("hh:mm a, MMM dd")
        return localTime.format(formatter)
    }
}