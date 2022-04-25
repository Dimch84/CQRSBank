package server.handlers.card.query

import client.api.abstractions.CardBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.db.mongo.CardRepository
import server.handlers.AnyQueryHandler
import server.queries.card.CardAllQuery

@Component
class CardAllQueryHandler @Autowired constructor(private val cardRepository: CardRepository)
        : AnyQueryHandler<CardAllQuery>() {
    override fun handle(query: CardAllQuery): String {
        val card = cardRepository.findAll().map { CardBody(it.name, it.type, it.account_id) }
        return GSON.toJson(card)
    }
}
