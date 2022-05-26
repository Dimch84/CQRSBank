package server.handlers.userInfo.comand

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import server.abstractions.userInfo.AnyUserInfoEventRes
import server.commands.userInfo.UserInfoAddCommand
import server.db.postgresql.TempEventsRepository
import server.db.postgresql.UserInfoEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.observers.ObserverUserInfo

@Component
class UserInfoAddCommandHandler @Autowired constructor(observerUserInfo: ObserverUserInfo,
                                                       private val userInfoEventsRepository: UserInfoEventsRepository,
                                                       private val tempEventsRepository: TempEventsRepository)
    : AnyUserInfoCommandHandler<AnyUserInfoEventRes>(userInfoEventsRepository) {
    init {
        attach(observerUserInfo)
    }

    @Transactional(rollbackFor = [Exception::class], isolation = Isolation.SERIALIZABLE)
    override fun handle(simpleCommand: SimpleCommand): String {
        val event = (simpleCommand.store.command as UserInfoAddCommand).event
        userInfoEvents(event.id).run {
            val userInfoUpd = update(event)
            userInfoEventsRepository.save(this)
            tempEventsRepository.deleteById(simpleCommand.id!!)
            send(userInfoUpd)
        }
        return "ok"
    }
}
