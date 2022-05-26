package server.handlers.userInfo.comand

import org.springframework.data.repository.findByIdOrNull
import server.abstractions.userInfo.AnyUserInfoEventRes
import server.db.postgresql.UserInfoEventsRepository
import server.db.postgresql.entities.UserInfoEvents
import server.handlers.AnyCommandHandler

abstract class AnyUserInfoCommandHandler<R : AnyUserInfoEventRes>(private val userInfoEventsRepository: UserInfoEventsRepository)
    : AnyCommandHandler<R>() {
    protected fun userInfoEvents(id: Long, throw_else: Boolean=false): UserInfoEvents {
        return userInfoEventsRepository.findByIdOrNull(id)?.apply { initEvents() }
            ?: if (throw_else) throw Exception("wrong id or not exist info")
            else userInfoEventsRepository.save(UserInfoEvents(id))
    }
}
