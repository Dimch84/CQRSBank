package server.commands.card

import server.abstractions.card.HistoryMode
import server.commands.Command
import server.events.card.CardHistoryEvent
import server.events.command.TypeCommand

class CardHistoryCommand(val id: Long?=null, val mode: HistoryMode?=null): Command {
    val event: CardHistoryEvent
        get() = CardHistoryEvent(id!!, mode!!)

    override val typeCommand: TypeCommand
        get() = TypeCommand.CARD_HISTORY_COMMAND

    override fun toMap() = mapOf("id" to id)
}
