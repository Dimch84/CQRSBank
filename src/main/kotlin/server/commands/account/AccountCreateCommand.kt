package server.commands.account

import server.commands.Command
import server.events.account.AccountCreateEvent
import server.events.command.TypeCommand

class AccountCreateCommand(val money: Long=0, val userId: Long=-1, val planId: Long=-1): Command {
    val event: AccountCreateEvent
        get() = AccountCreateEvent(money, userId, planId)

    override val typeCommand: TypeCommand
        get() = TypeCommand.ACCOUNT_CREATE_COMMAND

    override fun toMap() = mapOf("money" to money, "userId" to userId, "planId" to planId)
}
