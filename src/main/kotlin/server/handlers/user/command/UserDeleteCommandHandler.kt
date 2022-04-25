package server.handlers.user.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
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

    override fun handle(simpleCommand: SimpleCommand): String {
        val command = simpleCommand.store.command as UserDeleteCommand
        userEvents(command.login).run {
            val userUpd = update(command.event)
            userEventsRepository.save(this)
            send(userUpd)
            tempEventsRepository.deleteById(simpleCommand.id!!)
        }
        return "ok"
    }
}
