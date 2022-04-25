package server.handlers.card.command

import server.abstractions.card.AnyCardEventRes
import server.db.postgresql.CardEventsRepository
import server.db.postgresql.entities.CardEvents
import server.handlers.AnyCommandHandler

abstract class AnyCardCommandHandler<R : AnyCardEventRes>(private val cardEventsRepository: CardEventsRepository)
    : AnyCommandHandler<R>() {
    protected fun cardEvents(id: Long? = null): CardEvents {
        return (id?.let {
            cardEventsRepository.findById(id).get().apply { initEvents() }
        } ?: cardEventsRepository.save(CardEvents()))
    }
}
