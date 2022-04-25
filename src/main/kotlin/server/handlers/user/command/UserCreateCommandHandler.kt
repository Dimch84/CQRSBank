package server.handlers.user.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.abstractions.user.AnyUserEventRes
import server.commands.user.UserCreateCommand
import server.db.postgresql.TempEventsRepository
import server.db.postgresql.UserEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.observers.ObserverUser

@Component
class UserCreateCommandHandler @Autowired constructor(observerUser: ObserverUser,
                                                      private val userEventsRepository: UserEventsRepository,
                                                      private val tempEventsRepository: TempEventsRepository)
    : AnyUserCommandHandler<AnyUserEventRes>(userEventsRepository) {
    init {
        attach(observerUser)
    }

    override fun handle(simpleCommand: SimpleCommand): Long {
        val command = simpleCommand.store.command as UserCreateCommand
        return userEvents().run {
            val userUpd = update(command.event)
            userEventsRepository.save(this)
            send(userUpd)
            tempEventsRepository.deleteById(simpleCommand.id!!)
            id ?: throw Exception("UserCreateCommandHandler id error")
        }
    }
}
