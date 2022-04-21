package server.commands.card

import server.commands.Command
import server.db.postgresql.events.card.CardCreateEvent

class CardCreateCommand(val name: String, val type: String, val account_id: Int): Command {
    val event: CardCreateEvent
        get() = CardCreateEvent(name, type, account_id)
}
