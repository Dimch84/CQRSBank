package server.handlers.card.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.abstractions.card.AnyCardEventRes
import server.commands.card.CardCreateCommand
import server.db.postgresql.CardEventsRepository
import server.db.postgresql.TempEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.observers.ObserverCard

@Component
class CardCreateCommandHandler @Autowired constructor(observerCard: ObserverCard,
                                                      private val cardEventsRepository: CardEventsRepository,
                                                      private val tempEventsRepository: TempEventsRepository)
    : AnyCardCommandHandler<AnyCardEventRes>(cardEventsRepository) {
    init {
        attach(observerCard)
    }

    override fun handle(simpleCommand: SimpleCommand): Long {
        val command = simpleCommand.store.command as CardCreateCommand
        return cardEvents().run {
            val cardUpd = update(command.event)
            cardEventsRepository.save(this)
            send(cardUpd)
            tempEventsRepository.deleteById(simpleCommand.id!!)
            id ?: throw Exception("CardCreateCommandHandler id error")
        }
    }
}