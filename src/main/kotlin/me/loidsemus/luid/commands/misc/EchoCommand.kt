package me.loidsemus.luid.commands.misc

import com.jagrosh.jdautilities.command.CommandEvent
import me.loidsemus.luid.commands.RestrictedCommand
import net.dv8tion.jda.api.Permission

class EchoCommand : RestrictedCommand(Permission.ADMINISTRATOR) {

    init {
        with(this) {
            name = "echo"
            help = "echo"
            aliases = arrayOf("say", "print")
        }
    }

    override fun checkedExecute(event: CommandEvent) {
        event.message.delete().queue()
        if (!event.args.isNullOrEmpty()) {
            event.reply(event.args)
        }
    }
}