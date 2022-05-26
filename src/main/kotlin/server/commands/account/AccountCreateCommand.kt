package server.commands.account

import server.commands.Command
import server.events.account.AccountCreateEvent
import server.events.command.TypeCommand

class AccountCreateCommand(val name: String?=null, val money: Long?=null, val userId: Long?=null, val planId: Long?=null): Command {
    val event: AccountCreateEvent
        get() = AccountCreateEvent(name!!, money!!, userId!!, planId!!)

    override val typeCommand: TypeCommand
        get() = TypeCommand.ACCOUNT_CREATE_COMMAND

    override fun toMap() = mapOf("name" to name, "money" to money, "userId" to userId, "planId" to planId)
}
