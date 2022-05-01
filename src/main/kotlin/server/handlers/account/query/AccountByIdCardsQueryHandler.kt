package server.handlers.account.query

import client.api.abstractions.CardBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.db.mongo.CardRepository
import server.handlers.AnyQueryHandler
import server.queries.account.AccountCardsQuery

@Component
class AccountByIdCardsQueryHandler @Autowired constructor(private val cardRepository: CardRepository)
        : AnyQueryHandler<AccountCardsQuery>() {
    override fun handle(query: AccountCardsQuery): String {
        val cards = cardRepository.findByAccountId(query.id).map { CardBody(it.name, it.type, it.accountId) }
        return GSON.toJson(cards)
    }
}
