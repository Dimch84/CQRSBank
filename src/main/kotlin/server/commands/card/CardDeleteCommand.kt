package server.commands.card

import server.commands.Command
import server.events.card.CardDeleteEvent
import server.events.command.TypeCommand

class CardDeleteCommand(val id: Long?=null): Command {
    val event: CardDeleteEvent
        get() = CardDeleteEvent(id!!)

    override val typeCommand: TypeCommand
        get() = TypeCommand.CARD_DELETE_COMMAND

    override fun toMap() = mapOf("id" to id)
}
