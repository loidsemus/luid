package me.loidsemus.luid.commands.util

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import java.time.temporal.ChronoUnit

class PingCommand : Command() {

    init {
        with(this) {
            name = "ping"
            help = "Ping pong"
            aliases = arrayOf("latency")
        }
    }

    override fun execute(event: CommandEvent) {
        event.reply("Ping: ...") {
            val ping = event.message.timeCreated.until(it.timeCreated, ChronoUnit.MILLIS)
            it.editMessage("Ping: ${ping}ms total (${event.jda.gatewayPing}ms gateway)").queue()
        }
    }
}