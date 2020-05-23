package me.loidsemus.luid.extensions

import net.dv8tion.jda.api.entities.Message
import java.util.concurrent.TimeUnit

/**
 * Deletes the message after specified amount of seconds
 */
fun Message.deleteAfter(seconds: Long) {
    this.delete().queueAfter(seconds, TimeUnit.SECONDS)
}