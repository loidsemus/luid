package me.loidsemus.luid.commands.util

import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.PendingResult
import com.google.maps.TimeZoneApi
import com.google.maps.errors.ZeroResultsException
import com.google.maps.model.GeocodingResult
import com.google.maps.model.LatLng
import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import java.util.concurrent.CompletableFuture

class TimeCommand : Command() {

    private val apiKey = System.getenv("google_apikey")
    private val context: GeoApiContext

    init {
        with(this) {
            name = "time"
            help = "Check time in some timezone"
            aliases = arrayOf("timezone", "tz")
        }

        context = GeoApiContext.Builder()
            .apiKey(apiKey)
            .build()
    }

    override fun execute(event: CommandEvent) {
        if (event.args.isNullOrEmpty()) {
            event.reply("You need to specify a place")
            return
        }

        event.reply("Getting location...") { message ->
            val builder = StringBuilder()

            geocode(event.args).thenCompose {
                message.editMessage("Getting timezone...").queue()
                builder.append("**Time in ${it.formattedAddress}")
                timezone(it.geometry.location)
            }.thenAccept {
                builder.append(" (${it.displayName})**")
                    .append("\n")
                    .append(getCurrentTimeFormatted(it))

                message.editMessage(builder.toString()).queue()
            }.exceptionally {
                if (it.cause is ZeroResultsException) {
                    message.editMessage("No locations found for ${event.args}").queue()
                } else {
                    message.editMessage("Unknown error").queue()
                }
                null
            }
        }

    }

    private fun geocode(input: String): CompletableFuture<GeocodingResult> {
        val future = CompletableFuture<GeocodingResult>()

        GeocodingApi.geocode(context, input).setCallback(
            object : PendingResult.Callback<Array<GeocodingResult>> {
                override fun onFailure(e: Throwable) {
                    future.completeExceptionally(e)
                }

                override fun onResult(result: Array<GeocodingResult>) {
                    if (result.isNullOrEmpty()) {
                        future.completeExceptionally(ZeroResultsException("No locations found for $input"))
                        return
                    }
                    future.complete(result[0])
                }
            })
        return future
    }

    private fun timezone(location: LatLng): CompletableFuture<TimeZone> {
        val future = CompletableFuture<TimeZone>()

        TimeZoneApi.getTimeZone(context, location).setCallback(
            object : PendingResult.Callback<TimeZone> {
                override fun onFailure(e: Throwable) {
                    future.completeExceptionally(e)
                }

                override fun onResult(result: TimeZone) {
                    future.complete(result)
                }
            }
        )

        return future
    }

    private fun getCurrentTimeFormatted(timeZone: TimeZone): String {
        val localTime = LocalDateTime.now(timeZone.toZoneId())
        val formatter = DateTimeFormatter.ofPattern("hh:mm a, MMM dd")
        return localTime.format(formatter)
    }
}