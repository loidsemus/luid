package me.loidsemus.luid

import com.jagrosh.jdautilities.command.CommandClientBuilder
import me.loidsemus.luid.commands.PingCommand
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity

fun main() {

    val commandClient = CommandClientBuilder().apply {
        setOwnerId("219846009864454146")
        setPrefix("*;")
        setActivity(Activity.watching("you"))
        addCommands(PingCommand())
    }.build()

    JDABuilder.createDefault(System.getenv("luid_token"))
        .addEventListeners(commandClient)
        .build()
}