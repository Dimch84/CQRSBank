package server.handlers.card

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.abstractions.CardEventRes
import server.commands.card.CardUpdateNameCommand
import server.db.postgresql.CardEventsRepository
import server.handlers.AnyHandler
import server.observers.ObserverCard

@Component
class CardUpdateNameCommandHandler @Autowired constructor(observerCard: ObserverCard,
                                                          private val cardEventsRepository: CardEventsRepository)
    : AnyHandler<CardUpdateNameCommand, CardEventRes>(cardEventsRepository) {
    init {
        attach(observerCard)
    }

    override fun handle(command: CardUpdateNameCommand) {
        cardEvents(command.id).run {
            val cardUpd = update(command.event)
            cardEventsRepository.save(this)
            send(cardUpd)
        }
    }
}
