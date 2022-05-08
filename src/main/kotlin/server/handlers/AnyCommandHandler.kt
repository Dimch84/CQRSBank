package server.handlers

import server.abstractions.Res
import server.db.postgresql.entities.SimpleCommand
import server.observers.AnyObserver

abstract class AnyCommandHandler<R : Res> {
    private val anyObservers = mutableListOf<AnyObserver<R>>()

    fun attach(ob: AnyObserver<R>) = anyObservers.add(ob)

    fun attach(obs: List<AnyObserver<R>>) = anyObservers.addAll(obs)

    fun send(res: R) = anyObservers.forEach { it.update(res) }

    abstract fun handle(simpleCommand: SimpleCommand): Any

    open fun rollBack(simpleCommand: SimpleCommand): Any = {}
}
