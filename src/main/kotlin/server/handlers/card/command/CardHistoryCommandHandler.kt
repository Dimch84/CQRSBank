package server.handlers.card.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import server.abstractions.card.AnyCardEventRes
import server.abstractions.card.CardHistoryRes
import server.commands.card.CardHistoryCommand
import server.db.postgresql.CardEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.observers.ObserverCard

@Component
class CardHistoryCommandHandler @Autowired constructor(observerCard: ObserverCard,
                                                       private val cardEventsRepository: CardEventsRepository)
    : AnyCardCommandHandler<AnyCardEventRes>(cardEventsRepository) {
    init {
        attach(observerCard)
    }

    @Transactional(rollbackFor = [Exception::class], isolation = Isolation.SERIALIZABLE)
    override fun handle(simpleCommand: SimpleCommand): CardHistoryRes {
        val event = (simpleCommand.store.command as CardHistoryCommand).event
        return cardEvents(event.id, init=false).run { update(event) }
    }
}
