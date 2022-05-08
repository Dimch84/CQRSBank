package server.commands.card

import server.commands.Command
import server.events.card.CardCreateEvent
import server.events.command.TypeCommand

class CardCreateCommand(val name: String?=null, val type: String?=null, val accountId: Long?=null, val cardNumber: String?=null, val expDate: String?=null, val cvv: String? = null): Command {
    val event: CardCreateEvent
        get() = CardCreateEvent(name!!, type!!, accountId!!, cardNumber!!, expDate!!, cvv!!)

    override val typeCommand: TypeCommand
        get() = TypeCommand.CARD_CREATE_COMMAND

    override fun toMap() = mapOf("name" to name,
        "type" to type,
        "accountId" to accountId,
        "cardNumber" to cardNumber,
        "expDate" to expDate,
        "cvv" to cvv
    )
}
