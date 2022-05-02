package server.handlers.account.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
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

    override fun handle(simpleCommand: SimpleCommand): Long {
        val command = simpleCommand.store.command as AccountCreateCommand
        if (userRepository.findById(command.event.userId).isEmpty)
            throw Exception("wrong user id")
        return accountEvents().run {
            val accountUpd = update(command.event)
            accountEventsRepository.save(this)
            send(accountUpd)
            tempEventsRepository.deleteById(simpleCommand.id!!)
            id ?: throw Exception("AccountCreateCommandHandler id error")
        }
    }
}
