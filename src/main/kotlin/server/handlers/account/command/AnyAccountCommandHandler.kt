package server.handlers.account.command

import server.abstractions.account.AnyAccountEventRes
import server.db.postgresql.AccountEventsRepository
import server.db.postgresql.entities.AccountEvents
import server.handlers.AnyCommandHandler

abstract class AnyAccountCommandHandler<R : AnyAccountEventRes>(private val accountEventsRepository: AccountEventsRepository)
    : AnyCommandHandler<R>() {
    protected fun accountEvents(id: Long? = null): AccountEvents {
        return (id?.let {
            accountEventsRepository.findById(id).get().apply { initEvents() }
        } ?: accountEventsRepository.save(AccountEvents()))
    }
}
