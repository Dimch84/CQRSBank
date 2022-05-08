package server.handlers.card.query

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.commands.card.CardHistoryCommand
import server.db.postgresql.entities.SimpleCommand
import server.events.card.CardEvent
import server.events.command.StoreCommand
import server.handlers.AnyQueryHandler
import server.handlers.card.command.CardHistoryCommandHandler
import server.queries.card.CardHistoryQuery

@Component
class CardByIdHistoryQueryHandler @Autowired constructor(private val cardHistoryCommandHandler: CardHistoryCommandHandler)
        : AnyQueryHandler<CardHistoryQuery>() {
    override fun handle(query: CardHistoryQuery): List<CardEvent> {
        val command = CardHistoryCommand(query.id, query.mode)
        return cardHistoryCommandHandler.handle(SimpleCommand(StoreCommand(command, command.typeCommand))).history
    }
}
