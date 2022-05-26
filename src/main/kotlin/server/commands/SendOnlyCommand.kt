package server.commands

import server.events.SendOnlyEvent
import server.events.command.TypeCommand

class SendOnlyCommand(val id: Long?=null): Command {
    val event: SendOnlyEvent
        get() = SendOnlyEvent(id!!)

    override val typeCommand: TypeCommand
        get() = TypeCommand.SEND_ONLY_COMMAND

    override fun toMap() = mapOf("id" to id)
}
