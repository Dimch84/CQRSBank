package server.handlers.user.command

import server.abstractions.user.AnyUserEventRes
import server.db.postgresql.UserEventsRepository
import server.db.postgresql.entities.UserEvents
import server.handlers.AnyCommandHandler

abstract class AnyUserCommandHandler<R : AnyUserEventRes>(private val userEventsRepository: UserEventsRepository)
    : AnyCommandHandler<R>() {
    protected fun userEvents(id: Long? = null): UserEvents {
        return (id?.let {
            userEventsRepository.findById(id).get().apply { initEvents() }
        } ?: userEventsRepository.save(UserEvents()))
    }
}
