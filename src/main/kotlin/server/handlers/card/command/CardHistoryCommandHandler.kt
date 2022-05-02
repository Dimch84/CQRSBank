package server.handlers.card.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
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

    override fun handle(simpleCommand: SimpleCommand): CardHistoryRes {
        val command = simpleCommand.store.command as CardHistoryCommand
        return cardEvents(command.id, init=false).run { update(command.event) }
    }
}
