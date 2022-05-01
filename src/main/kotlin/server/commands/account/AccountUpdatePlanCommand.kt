package server.commands.account

import server.commands.Command
import server.events.account.AccountUpdatePlanEvent
import server.events.command.TypeCommand

class AccountUpdatePlanCommand(val planId: Long=-1, val id: Long=-1): Command {
    val event: AccountUpdatePlanEvent
        get() = AccountUpdatePlanEvent(planId, id)

    override val typeCommand: TypeCommand
        get() = TypeCommand.ACCOUNT_UPDATE_PLAN_COMMAND

    override fun toMap() = mapOf("planId" to planId, "id" to id)
}
