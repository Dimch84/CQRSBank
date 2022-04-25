package server.handlers.card.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.abstractions.card.AnyCardEventRes
import server.commands.card.CardPayCommand
import server.commands.card.CardReceiptCommand
import server.commands.card.CardTransferCommand
import server.db.postgresql.CardEventsRepository
import server.db.postgresql.TempEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.handlers.AnyCommandHandler
import server.observers.ObserverCard

@Component
class CardReceiptCommandHandler @Autowired constructor(observerCard: ObserverCard,
                                                       private val cardEventsRepository: CardEventsRepository,
                                                       private val tempEventsRepository: TempEventsRepository)
    : AnyCommandHandler<AnyCardEventRes>(cardEventsRepository) {
    init {
        attach(observerCard)
    }

    override fun handle(simpleCommand: SimpleCommand): String {
        val command = simpleCommand.command.cardCommand as CardReceiptCommand
        cardEvents(command.id).run {
            val cardUpd = update(command.event)
            cardEventsRepository.save(this)
            send(cardUpd)
            tempEventsRepository.deleteById(simpleCommand.id!!)
        }
        return "ok"
    }
}
