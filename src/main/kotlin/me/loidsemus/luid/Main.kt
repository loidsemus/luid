@file:JvmName("Main")

package me.loidsemus.luid

import co.aikar.idb.DB
import co.aikar.idb.DatabaseOptions
import co.aikar.idb.PooledDatabaseOptions
import com.jagrosh.jdautilities.command.CommandClientBuilder
import me.loidsemus.luid.commands.misc.EchoCommand
import me.loidsemus.luid.commands.util.PingCommand
import me.loidsemus.luid.commands.`fun`.RedditCommand
import net.dean.jraw.RedditClient
import net.dean.jraw.http.OkHttpNetworkAdapter
import net.dean.jraw.http.UserAgent
import net.dean.jraw.oauth.Credentials
import net.dean.jraw.oauth.OAuthHelper
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity

fun main() {
    initDb()
    val redditClient = initReddit()
    val commandClient = CommandClientBuilder().apply {
        setOwnerId("219846009864454146")
        setPrefix("*;")
        setActivity(Activity.watching("you"))
        addCommands(
            PingCommand(), RedditCommand(redditClient),
            EchoCommand()
        )
    }.build()

    JDABuilder.createDefault(System.getenv("luid_token"))
        .addEventListeners(commandClient)
        .build()
}

fun initDb() {
    val options = DatabaseOptions.builder().sqlite("data.db").build()
    val db = PooledDatabaseOptions.builder().options(options).createHikariDatabase()
    DB.setGlobalDatabase(db)
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
