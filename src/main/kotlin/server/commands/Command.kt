package server.commands

import server.events.command.TypeCommand

interface Command {
    val typeCommand: TypeCommand
    fun toMap(): Map<String, Any?>
}
