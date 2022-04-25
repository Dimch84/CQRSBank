package server.handlers.card.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.abstractions.card.AnyCardEventRes
import server.abstractions.card.CardEventRes
import server.commands.card.CardUpdateNameCommand
import server.db.postgresql.CardEventsRepository
import server.db.postgresql.TempEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.handlers.AnyCommandHandler
import server.observers.ObserverCard

@Component
class CardUpdateNameCommandHandler @Autowired constructor(observerCard: ObserverCard,
                                                          private val cardEventsRepository: CardEventsRepository,
                                                          private val tempEventsRepository: TempEventsRepository)
    : AnyCommandHandler<AnyCardEventRes>(cardEventsRepository) {
    init {
        attach(observerCard)
    }

    override fun handle(simpleCommand: SimpleCommand): String {
        val command = simpleCommand.command.cardCommand as CardUpdateNameCommand
        cardEvents(command.id).run {
            val cardUpd = update(command.event)
            cardEventsRepository.save(this)
            send(cardUpd)
            tempEventsRepository.deleteById(simpleCommand.id!!)
        }
        return "ok"
    }
}
