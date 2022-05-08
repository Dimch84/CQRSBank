package server.handlers.user.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.abstractions.user.AnyUserEventRes
import server.commands.SendOnlyCommand
import server.db.postgresql.UserEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.observers.ObserverUser

@Component
class UserSendOnlyCommandHandler @Autowired constructor(observerUser: ObserverUser,
                                                        userEventsRepository: UserEventsRepository)
    : AnyUserCommandHandler<AnyUserEventRes>(userEventsRepository) {
    init {
        attach(observerUser)
    }

    override fun handle(simpleCommand: SimpleCommand): String {
        val command = simpleCommand.store.command as SendOnlyCommand
        userEvents(command.event.id).run {
            send(update())
        }
        return "ok"
    }
}
