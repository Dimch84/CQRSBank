package server.handlers.card.query

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import server.commands.card.CardHistoryCommand
import server.db.mongo.AccountRepository
import server.db.mongo.CardRepository
import server.db.mongo.UserRepository
import server.db.postgresql.entities.SimpleCommand
import server.events.card.CardEvent
import server.events.command.StoreCommand
import server.handlers.AnyQueryHandler
import server.handlers.card.command.CardHistoryCommandHandler
import server.queries.card.CardHistoryQuery

@Component
class CardByIdHistoryQueryHandler @Autowired constructor(private val cardHistoryCommandHandler: CardHistoryCommandHandler,
                                                         private val cardRepository: CardRepository,
                                                         private val userRepository: UserRepository,
                                                         private val accountRepository: AccountRepository
)
        : AnyQueryHandler<CardHistoryQuery>() {
    override fun handle(query: CardHistoryQuery): List<CardEvent> {
        val userId_ = query.login?.let { userRepository.findByLogin(it)?.id } ?: throw Exception("wrong login")
        val accountId = cardRepository.findByIdOrNull(query.id)?.accountId ?: throw Exception("wrong card id")
        accountRepository.findByUserIdAndId(userId_, accountId) ?: throw Exception("wrong account id")
        val command = CardHistoryCommand(query.id, query.mode)
        return cardHistoryCommandHandler.handle(SimpleCommand(StoreCommand(command, command.typeCommand))).history
    }
}
