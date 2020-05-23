package me.loidsemus.luid.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import net.dean.jraw.RedditClient
import net.dean.jraw.models.Subreddit

class RedditCommand(private val client: RedditClient) : Command() {

    init {
        with(this) {
            name = "reddit"
            help = "Random submission from reddit sub"
        }
    }

    override fun execute(event: CommandEvent) {
        if (event.args.isEmpty()) {
            event.reply("You must specify subreddit name")
            return
        }

        event.reply("Loading...") {
            val subredditRef = client.subreddit(event.args)
            val subreddit: Subreddit
            try {
                subreddit = subredditRef.about()
            } catch (e: NullPointerException) {
                it.editMessage("That subreddit (${event.args}) doesn't exist").queue()
                return@reply
            }

            if (subreddit.isNsfw) {
                it.editMessage("NSFW subs are disabled").queue()
                return@reply
            }


            val submission = subredditRef.randomSubmission().subject
            if (!submission.selfText.isNullOrEmpty()) {
                val text: String = submission.selfText.toString()
                if (text.length > 1990) {
                    it.editMessage("that message 2 long").queue()
                    return@reply
                }
                it.editMessage(
                    "**${submission.title}**\n" +
                            text
                ).queue()
                return@reply
            }

            it.editMessage(
                "**${submission.title}**\n" +
                        submission.url
            ).queue()
        }
    }
}