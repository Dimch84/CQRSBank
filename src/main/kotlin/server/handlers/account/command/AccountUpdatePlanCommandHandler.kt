package server.handlers.account.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import server.abstractions.account.AnyAccountEventRes
import server.commands.account.AccountUpdatePlanCommand
import server.db.mongo.AccountRepository
import server.db.mongo.UserRepository
import server.db.postgresql.AccountEventsRepository
import server.db.postgresql.TempEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.observers.ObserverAccount

@Component
class AccountUpdatePlanCommandHandler @Autowired constructor(observerAccount: ObserverAccount,
                                                             private val accountEventsRepository: AccountEventsRepository,
                                                             private val tempEventsRepository: TempEventsRepository,
                                                             private val userRepository: UserRepository,
                                                             private val accountRepository: AccountRepository)
    : AnyAccountCommandHandler<AnyAccountEventRes>(accountEventsRepository) {
    init {
        attach(observerAccount)
    }

    @Transactional(rollbackFor = [Exception::class], isolation = Isolation.SERIALIZABLE)
    override fun handle(simpleCommand: SimpleCommand): String {
        val event = (simpleCommand.store.command as AccountUpdatePlanCommand).event
        val userId = userRepository.findByLogin(event.login)?.id ?: throw Exception("wrong login")
        accountRepository.findByUserIdAndId(userId, event.id) ?: throw Exception("wrong account id")
        accountEvents(event.id).run {
            val accountUpd = update(event)
            accountEventsRepository.save(this)
            tempEventsRepository.deleteById(simpleCommand.id!!)
            send(accountUpd)
        }
        return "ok"
    }
}
