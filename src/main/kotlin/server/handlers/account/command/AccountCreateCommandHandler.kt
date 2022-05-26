package server.handlers.account.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import server.abstractions.account.AnyAccountEventRes
import server.commands.account.AccountCreateCommand
import server.db.mongo.UserRepository
import server.db.postgresql.AccountEventsRepository
import server.db.postgresql.TempEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.observers.ObserverAccount

@Component
class AccountCreateCommandHandler @Autowired constructor(observerAccount: ObserverAccount,
                                                         private val accountEventsRepository: AccountEventsRepository,
                                                         private val userRepository: UserRepository,
                                                         private val tempEventsRepository: TempEventsRepository)
    : AnyAccountCommandHandler<AnyAccountEventRes>(accountEventsRepository) {
    init {
        attach(observerAccount)
    }

    @Transactional(rollbackFor = [Exception::class], isolation = Isolation.SERIALIZABLE)
    override fun handle(simpleCommand: SimpleCommand): Long {
        val event = (simpleCommand.store.command as AccountCreateCommand).event
        if (userRepository.findById(event.userId).isEmpty)
            throw Exception("wrong user id")
        return accountEvents().run {
            val accountUpd = update(event)
            accountEventsRepository.save(this)
            tempEventsRepository.deleteById(simpleCommand.id!!)
            val idRet = id ?: throw Exception("AccountCreateCommandHandler id error")
            send(accountUpd)
            idRet
        }
    }
}
