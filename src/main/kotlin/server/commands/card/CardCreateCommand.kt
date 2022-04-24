package server.commands.card

import server.commands.Command
import server.events.card.CardCreateEvent
import server.events.command.TypeCommand

data class CardCreateCommand(val name: String, val type: String, val account_id: Int): Command {
    val event: CardCreateEvent
        get() = CardCreateEvent(name, type, account_id)

    override val typeCommand: TypeCommand
        get() = TypeCommand.CARD_CREATE_COMMAND

    fun toMap() = mapOf("name" to name, "type" to type, "account_id" to account_id)
}
