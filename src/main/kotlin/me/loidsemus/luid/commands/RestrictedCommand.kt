package me.loidsemus.luid.commands

import com.jagrosh.jdautilities.command.Command
import com.jagrosh.jdautilities.command.CommandEvent
import me.loidsemus.luid.extensions.deleteAfter
import net.dv8tion.jda.api.Permission

/**
 * A command that requires a permission to be executed
 */
abstract class RestrictedCommand(private val requiredPermission: Permission) : Command() {

    /**
     * Ran after permissions have been checked, i.e the user will have the required permission
     */
    abstract fun checkedExecute(event: CommandEvent)

    final override fun execute(event: CommandEvent) {
        if (!event.member.hasPermission(requiredPermission)) {
            event.message.delete().queue()
            event.reply(
                "${event.member.asMention}, you don't have permission to do that " +
                        "(missing ${requiredPermission.getName()})"
            ) {
                it.deleteAfter(5)
            }
            return
        }
        checkedExecute(event)
    }
}