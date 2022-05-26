package server.db.postgresql.entities

import server.abstractions.Res
import server.events.AnyStore

interface DomainEvents<T: AnyStore, R: Res> {
    val events: MutableList<T>

    fun initEvents()

    fun update(): R
}
