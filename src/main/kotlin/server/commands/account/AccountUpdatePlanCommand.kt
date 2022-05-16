package server.commands.account

import server.commands.Command
import server.events.account.AccountUpdatePlanEvent
import server.events.command.TypeCommand

class AccountUpdatePlanCommand(val planId: Long?=null, val id: Long?=null, val login: String?=null): Command {
    val event: AccountUpdatePlanEvent
        get() = AccountUpdatePlanEvent(planId!!, id!!, login!!)

    override val typeCommand: TypeCommand
        get() = TypeCommand.ACCOUNT_UPDATE_PLAN_COMMAND

    override fun toMap() = mapOf("planId" to planId, "id" to id, "login" to login)
}
