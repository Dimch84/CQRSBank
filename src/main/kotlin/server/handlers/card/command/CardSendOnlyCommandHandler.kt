package server.handlers.card.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.abstractions.card.AnyCardEventRes
import server.commands.SendOnlyCommand
import server.commands.card.CardCreateCommand
import server.db.mongo.AccountRepository
import server.db.postgresql.CardEventsRepository
import server.db.postgresql.TempEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.observers.ObserverCard

@Component
class CardSendOnlyCommandHandler @Autowired constructor(observerCard: ObserverCard,
                                                        cardEventsRepository: CardEventsRepository)
    : AnyCardCommandHandler<AnyCardEventRes>(cardEventsRepository) {
    init {
        attach(observerCard)
    }

    override fun handle(simpleCommand: SimpleCommand): String {
        val command = simpleCommand.store.command as SendOnlyCommand
        cardEvents(command.event.id).run {
            send(update())
        }
        return "ok"
    }
}
