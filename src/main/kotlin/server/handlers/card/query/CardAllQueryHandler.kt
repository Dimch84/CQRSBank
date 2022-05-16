package server.handlers.card.query

import client.api.abstractions.CardBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.db.mongo.AccountRepository
import server.db.mongo.CardRepository
import server.db.mongo.UserRepository
import server.handlers.AnyQueryHandler
import server.queries.card.CardAllQuery

@Component
class CardAllQueryHandler @Autowired constructor(private val cardRepository: CardRepository,
                                                 private val userRepository: UserRepository,
                                                 private val accountRepository: AccountRepository)
        : AnyQueryHandler<CardAllQuery>() {
    override fun handle(query: CardAllQuery): String {
        val userId_ = query.login?.let { userRepository.findByLogin(it)?.id } ?: throw Exception("wrong login")
        val cards = accountRepository.findByUserId(userId_)
            .map { it.id }
            .map { cardRepository.findByAccountId(it) }
            .flatten()
            .map { CardBody(it.name, it.type, it.accountId) }
        return GSON.toJson(cards)
    }
}
