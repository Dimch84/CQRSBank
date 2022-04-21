package server.eventsDomain

import server.abstractions.Res
import server.db.postgresql.events.AnyStoreEvent

interface DomainEvents<T: AnyStoreEvent, R: Res> {
    val events: MutableList<T>

    fun initEvents()

    fun update(): R
}
