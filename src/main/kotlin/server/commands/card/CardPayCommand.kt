package server.commands.card

import server.commands.Command
import server.events.card.CardPayEvent
import server.events.command.TypeCommand

class CardPayCommand(val money: Long?=null, val id: Long?=null, val login: String?=null): Command {
    val event: CardPayEvent
        get() = CardPayEvent(money!!, id!!, login!!)

    override val typeCommand: TypeCommand
        get() = TypeCommand.CARD_PAY_COMMAND

    override fun toMap() = mapOf("money" to money, "id" to id, "login" to login)
}
