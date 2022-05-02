package server.handlers.account.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.abstractions.account.AnyAccountEventRes
import server.commands.account.AccountUpdateMoneyCommand
import server.db.postgresql.AccountEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.observers.ObserverAccount

@Component
class AccountUpdateMoneyCommandHandler @Autowired constructor(observerAccount: ObserverAccount,
                                                              private val accountEventsRepository: AccountEventsRepository)
    : AnyAccountCommandHandler<AnyAccountEventRes>(accountEventsRepository) {
    init {
        attach(observerAccount)
    }

    override fun handle(simpleCommand: SimpleCommand): String {
        val command = simpleCommand.store.command as AccountUpdateMoneyCommand
        accountEvents(command.id).run {
            val accountUpd = update(command.event)
            accountEventsRepository.save(this)
            send(accountUpd)
        }
        return "ok"
    }
}
