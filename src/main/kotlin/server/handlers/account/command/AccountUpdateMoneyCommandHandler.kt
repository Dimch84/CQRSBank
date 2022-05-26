package server.handlers.account.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
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

    @Transactional(rollbackFor = [Exception::class], isolation = Isolation.SERIALIZABLE)
    override fun handle(simpleCommand: SimpleCommand): String {
        val event = (simpleCommand.store.command as AccountUpdateMoneyCommand).event
        accountEvents(event.id).run {
            val accountUpd = update(event)
            accountEventsRepository.save(this)
            send(accountUpd)
        }
        return "ok"
    }
}
