package server.commands.card

import server.commands.Command
import server.events.card.CardUpdateNameEvent
import server.events.command.TypeCommand

class CardUpdateNameCommand(val name: String, val id: Long): Command {
    val event: CardUpdateNameEvent
        get() = CardUpdateNameEvent(name, id)

    override val typeCommand: TypeCommand
        get() = TypeCommand.CARD_UPDATE_NAME_COMMAND
}
