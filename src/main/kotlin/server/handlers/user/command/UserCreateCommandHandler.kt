package server.handlers.user.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import server.abstractions.user.AnyUserEventRes
import server.commands.user.UserCreateCommand
import server.db.mongo.UserRepository
import server.db.postgresql.TempEventsRepository
import server.db.postgresql.UserEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.observers.ObserverUser

@Component
class UserCreateCommandHandler @Autowired constructor(observerUser: ObserverUser,
                                                      private val userEventsRepository: UserEventsRepository,
                                                      private val tempEventsRepository: TempEventsRepository,
                                                      private val userRepository: UserRepository)
    : AnyUserCommandHandler<AnyUserEventRes>(userEventsRepository) {
    init {
        attach(observerUser)
    }

    @Transactional(rollbackFor = [Exception::class], isolation = Isolation.SERIALIZABLE)
    override fun handle(simpleCommand: SimpleCommand): Long {
        val event = (simpleCommand.store.command as UserCreateCommand).event
        userRepository.findByLogin(event.login)?.let { throw Exception("user with login=${event.login} is already register") }
        return userEvents().run {
            val userUpd = update(event)
            userEventsRepository.save(this)
            tempEventsRepository.deleteById(simpleCommand.id!!)
            val idRet = id ?: throw Exception("UserCreateCommandHandler id error")
            send(userUpd)
            idRet
        }
    }
}
