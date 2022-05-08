package server.handlers.card.query

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.db.mongo.AccountRepository
import server.db.mongo.CardRepository
import server.handlers.AnyQueryHandler
import server.queries.card.CardMoneyQuery

@Component
class CardByIdMoneyQueryHandler @Autowired constructor(private val cardRepository: CardRepository,
                                                       private val accountRepository: AccountRepository)
        : AnyQueryHandler<CardMoneyQuery>() {
    override fun handle(query: CardMoneyQuery) =
        accountRepository.findById(cardRepository.findById(query.id).get().accountId).get().money
}
