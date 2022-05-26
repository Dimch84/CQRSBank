package server.handlers.card.query

import client.api.abstractions.CardBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import server.db.mongo.AccountRepository
import server.db.mongo.CardRepository
import server.db.mongo.UserRepository
import server.handlers.AnyQueryHandler
import server.queries.card.CardQuery

@Component
class CardByIdQueryHandler @Autowired constructor(private val cardRepository: CardRepository,
                                                  private val userRepository: UserRepository,
                                                  private val accountRepository: AccountRepository)
        : AnyQueryHandler<CardQuery>() {
    override fun handle(query: CardQuery): String {
        val userId_ = query.login?.let { userRepository.findByLogin(it)?.id } ?: throw Exception("wrong login")
        val card = cardRepository.findByIdOrNull(query.id)?.run { CardBody(id, name, type, accountId, cardNumber, expDate, cvv) } ?: throw Exception("wrong card id")
        accountRepository.findByUserIdAndId(userId_, card.accountId) ?: throw Exception("wrong account id")
        return GSON.toJson(card)
    }
}
