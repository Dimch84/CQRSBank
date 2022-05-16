package server.commands.account

import server.commands.Command
import server.events.account.AccountDeleteEvent
import server.events.command.TypeCommand

class AccountDeleteCommand(val id: Long?=null, val login: String?=null): Command {
    val event: AccountDeleteEvent
        get() = AccountDeleteEvent(id!!, login!!)

    override val typeCommand: TypeCommand
        get() = TypeCommand.ACCOUNT_DELETE_COMMAND

    override fun toMap() = mapOf("id" to id, "login" to login)
}
