package server.commands.card

import server.commands.Command
import server.events.card.CardLocalTransferEvent
import server.events.command.TypeCommand

class CardLocalTransferCommand(val money: Long?=null, val idFrom: Long?=null, val idTo: Long?=null): Command {
    val event: CardLocalTransferEvent
        get() = CardLocalTransferEvent(money!!, idFrom!!, idTo!!)

    override val typeCommand: TypeCommand
        get() = TypeCommand.CARD_LOCAL_TRANSFER_COMMAND

    override fun toMap() = mapOf("money" to money?.let { -it }, "idFrom" to idFrom, "idTo" to idTo)
}
