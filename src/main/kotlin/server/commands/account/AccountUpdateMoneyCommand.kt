package server.commands.account

import server.commands.Command
import server.events.account.AccountUpdateMoneyEvent
import server.events.command.TypeCommand

class AccountUpdateMoneyCommand(val money: Long?=null, val id: Long?=null): Command {
    val event: AccountUpdateMoneyEvent
        get() = AccountUpdateMoneyEvent(money!!, id!!)

    override val typeCommand: TypeCommand
        get() = TypeCommand.ACCOUNT_UPDATE_MONEY_COMMAND

    override fun toMap() = mapOf("money" to money, "id" to id)
}
