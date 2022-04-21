package server.handlers

import server.abstractions.Res
import server.observers.AnyObserver
import server.commands.Command
import server.db.postgresql.CardEventsRepository
import server.eventsDomain.card.CardEvents

abstract class AnyHandler<T : Command, R : Res>(private val cardEventsRepository: CardEventsRepository) {
    protected fun cardEvents(id: Long? = null): CardEvents {
        return (id?.let {
            cardEventsRepository.findById(id).get().apply { initEvents() }
        } ?: cardEventsRepository.save(CardEvents()))
    }

    private val anyObservers = mutableListOf<AnyObserver<R>>()

    fun attach(ob: AnyObserver<R>) = anyObservers.add(ob)

    fun attach(obs: List<AnyObserver<R>>) = anyObservers.addAll(obs)

    fun send(res: R) = anyObservers.forEach { it.update(res) }

    abstract fun handle(command: T)
}
