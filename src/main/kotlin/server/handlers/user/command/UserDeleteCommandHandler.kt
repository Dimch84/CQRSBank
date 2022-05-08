package server.handlers.user.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import server.abstractions.user.AnyUserEventRes
import server.commands.user.UserDeleteCommand
import server.db.postgresql.TempEventsRepository
import server.db.postgresql.UserEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.observers.ObserverUser

@Component
class UserDeleteCommandHandler @Autowired constructor(observerUser: ObserverUser,
                                                      private val userEventsRepository: UserEventsRepository,
                                                      private val tempEventsRepository: TempEventsRepository)
    : AnyUserCommandHandler<AnyUserEventRes>(userEventsRepository) {
    init {
        attach(observerUser)
    }

    @Transactional(rollbackFor = [Exception::class], isolation = Isolation.SERIALIZABLE)
    override fun handle(simpleCommand: SimpleCommand): String {
        val event = (simpleCommand.store.command as UserDeleteCommand).event
        userEvents(event.login).run {
            val userUpd = update(event)
            userEventsRepository.save(this)
            tempEventsRepository.deleteById(simpleCommand.id!!)
            send(userUpd)
        }
        return "ok"
    }
}
