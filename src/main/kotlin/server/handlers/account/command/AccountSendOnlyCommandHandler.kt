package server.handlers.account.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.abstractions.account.AnyAccountEventRes
import server.commands.SendOnlyCommand
import server.db.postgresql.AccountEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.observers.ObserverAccount

@Component
class AccountSendOnlyCommandHandler @Autowired constructor(observerAccount: ObserverAccount,
                                                           accountEventsRepository: AccountEventsRepository)
    : AnyAccountCommandHandler<AnyAccountEventRes>(accountEventsRepository) {
    init {
        attach(observerAccount)
    }

    override fun handle(simpleCommand: SimpleCommand): String {
        val command = simpleCommand.store.command as SendOnlyCommand
        accountEvents(command.id).run {
            send(update())
        }
        return "ok"
    }
}
