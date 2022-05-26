package server.commands.card

import server.commands.Command
import server.events.card.CardLocalTransferEvent
import server.events.command.TypeCommand

class CardLocalTransferCommand(val money: Long?=null, val idFrom: Long?=null, val idTo: Long?=null, val login: String?=null): Command {
    val event: CardLocalTransferEvent
        get() = CardLocalTransferEvent(money!!, idFrom!!, idTo!!, login!!)

    override val typeCommand: TypeCommand
        get() = TypeCommand.CARD_LOCAL_TRANSFER_COMMAND

    override fun toMap() = mapOf("money" to money, "idFrom" to idFrom, "idTo" to idTo, "login" to login)
}
