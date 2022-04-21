package server.handlers.card

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.abstractions.CardEventRes
import server.commands.card.CardCreateCommand
import server.db.postgresql.CardEventsRepository
import server.handlers.AnyHandler
import server.observers.ObserverCard

@Component
class CardCreateCommandHandler @Autowired constructor(observerCard: ObserverCard,
                                                      private val cardEventsRepository: CardEventsRepository)
    : AnyHandler<CardCreateCommand, CardEventRes>(cardEventsRepository) {
    init {
        attach(observerCard)
    }

    override fun handle(command: CardCreateCommand) {
        cardEvents().run {
            val cardUpd = update(command.event)
            cardEventsRepository.save(this)
            send(cardUpd)
        }
    }
}
