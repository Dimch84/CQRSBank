package server.commands.card

import server.commands.Command
import server.events.card.CardTransferEvent
import server.events.command.TypeCommand

class CardTransferCommand(val money: Long?=null, val id: Long?=null, val login: String?=null): Command {
    val event: CardTransferEvent
        get() = CardTransferEvent(-money!!, id!!, login!!)

    override val typeCommand: TypeCommand
        get() = TypeCommand.CARD_TRANSFER_COMMAND

    override fun toMap() = mapOf("money" to money?.let { -it }, "id" to id, "login" to login)
}
