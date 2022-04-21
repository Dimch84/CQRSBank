package server.commands.card

import server.commands.Command
import server.db.postgresql.events.card.CardUpdateNameEvent

class CardUpdateNameCommand(val name: String, val id: Long): Command {
    val event: CardUpdateNameEvent
        get() = CardUpdateNameEvent(name, id)
}
