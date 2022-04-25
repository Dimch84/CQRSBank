package server.handlers.card

import client.api.abstractions.CardBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.db.mongo.CardRepository
import server.handlers.AnyQueryHandler
import server.queries.card.CardByIdQuery

@Component
class CardByIdQueryHandler @Autowired constructor(private val cardRepository: CardRepository)
        : AnyQueryHandler<CardByIdQuery>() {
    override fun handle(query: CardByIdQuery): String {
        val card = cardRepository.findById(query.id).get().run { CardBody(name, type, account_id) }
        return GSON.toJson(card)
    }
}
