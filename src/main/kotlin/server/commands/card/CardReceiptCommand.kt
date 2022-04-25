package server.commands.card

import server.commands.Command
import server.events.card.CardTransferEvent
import server.events.command.TypeCommand

class CardReceiptCommand(val money: Long=0, val id: Long=-1): Command {
    val event: CardTransferEvent
        get() = CardTransferEvent(money, id)

    override val typeCommand: TypeCommand
        get() = TypeCommand.CARD_RECEIPT_COMMAND

    override fun toMap() = mapOf("money" to money, "id" to id)
}
