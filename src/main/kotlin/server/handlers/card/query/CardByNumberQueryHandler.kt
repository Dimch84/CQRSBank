package server.handlers.card.query

import client.api.abstractions.CardBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.db.mongo.CardRepository
import server.handlers.AnyQueryHandler
import server.queries.card.CardQuery
import server.queries.card.CardQueryByNumber

@Component
class CardByNumberQueryHandler @Autowired constructor(private val cardRepository: CardRepository)
    : AnyQueryHandler<CardQueryByNumber>() {
    override fun handle(query: CardQueryByNumber): String {
        val card = cardRepository.findByCardNumber(query.number).first().run { CardBody(id, name, type, accountId, cardNumber, expDate, cvv) }
        return GSON.toJson(card)
    }
}
