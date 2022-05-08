package server.handlers.card.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import server.abstractions.card.AnyCardEventRes
import server.commands.card.CardUpdateNameCommand
import server.db.postgresql.CardEventsRepository
import server.db.postgresql.TempEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.observers.ObserverCard

@Component
class CardUpdateNameCommandHandler @Autowired constructor(observerCard: ObserverCard,
                                                          private val cardEventsRepository: CardEventsRepository,
                                                          private val tempEventsRepository: TempEventsRepository)
    : AnyCardCommandHandler<AnyCardEventRes>(cardEventsRepository) {
    init {
        attach(observerCard)
    }

    @Transactional(rollbackFor = [Exception::class], isolation = Isolation.SERIALIZABLE)
    override fun handle(simpleCommand: SimpleCommand): String {
        val event = (simpleCommand.store.command as CardUpdateNameCommand).event
        cardEvents(event.id).run {
            val cardUpd = update(event)
            cardEventsRepository.save(this)
            tempEventsRepository.deleteById(simpleCommand.id!!)
            send(cardUpd)
        }
        return "ok"
    }
}
