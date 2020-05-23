@file:JvmName("Main")
package me.loidsemus.luid

import com.jagrosh.jdautilities.command.CommandClientBuilder
import me.loidsemus.luid.commands.PingCommand
import me.loidsemus.luid.commands.RedditCommand
import net.dean.jraw.RedditClient
import net.dean.jraw.http.OkHttpNetworkAdapter
import net.dean.jraw.http.UserAgent
import net.dean.jraw.oauth.Credentials
import net.dean.jraw.oauth.OAuthHelper
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity

fun main() {

    val redditClient = initReddit()
    val commandClient = CommandClientBuilder().apply {
        setOwnerId("219846009864454146")
        setPrefix("*;")
        setActivity(Activity.watching("you"))
        addCommands(PingCommand(), RedditCommand(redditClient))
    }.build()

    JDABuilder.createDefault(System.getenv("luid_token"))
        .addEventListeners(commandClient)
        .build()
}

fun initReddit(): RedditClient {
    val credentials = Credentials.script(
        System.getenv("reddit_username"), System.getenv("reddit_password"),
        System.getenv("reddit_clientid"), System.getenv("reddit_clientsecret")
    )
    val userAgent = UserAgent("jda", "me.loidsemus.luid", "1", "kebabelele")
    val adapter = OkHttpNetworkAdapter(userAgent)
    return OAuthHelper.automatic(adapter, credentials).also {
        println("Authenticated reddit: ${it.me().username}")
    }
}
