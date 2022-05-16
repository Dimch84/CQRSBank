package server.commands.card

import server.commands.Command
import server.events.card.CardUpdateNameEvent
import server.events.command.TypeCommand

class CardUpdateNameCommand(val name: String?=null, val id: Long?=null, val login: String?=null): Command {
    val event: CardUpdateNameEvent
        get() = CardUpdateNameEvent(name!!, id!!, login!!)

    override val typeCommand: TypeCommand
        get() = TypeCommand.CARD_UPDATE_NAME_COMMAND

    override fun toMap() = mapOf("name" to name, "id" to id, "login" to login)
}
