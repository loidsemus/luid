package me.loidsemus.luid.commands.`fun`

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import me.loidsemus.luid.APIProvider
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class UDCommand : Command() {

    companion object {
        private const val baseUrl = "http://api.urbandictionary.com/v0/define?term="
    }

    init {
        with(this) {
            name = "ud"
            help = "Search on urban dictionary"
            aliases = arrayOf("define", "urban")
        }
    }

    override fun execute(event: CommandEvent) {
        if (event.args.isNullOrEmpty()) {
            event.reply("You need to specify a word")
            return
        }

        val request = Request.Builder()
            .url(baseUrl + event.args)
            .build()

        event.reply("Loading...") {
            APIProvider.okHttpClient.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    it.editMessage("Error").queue()
                }

                override fun onResponse(call: Call, response: Response) {
                    val udResponse: UDResponse =
                        APIProvider.gson.fromJson(response.body!!.string(), UDResponse::class.java)
                    if (udResponse.list.isEmpty()) {
                        it.editMessage("No definitions found").queue()
                        return
                    }
                    val entry = udResponse.list[0]
                    it.editMessage("${entry.formattedDefinition()}\n\n*${entry.formattedExample()}*").queue()
                }
            })
        }
    }

    private data class UDResponse(val list: List<UDEntry>)
    private data class UDEntry(val word: String, val definition: String, val example: String) {

        fun formattedDefinition(): String {
            return definition.replace("[", "").replace("]", "")
        }

        fun formattedExample(): String {
            return example.replace("[", "").replace("]", "")
        }
    }
}