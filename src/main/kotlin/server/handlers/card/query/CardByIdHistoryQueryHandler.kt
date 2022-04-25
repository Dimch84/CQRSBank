package server.handlers.card.query

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.db.mongo.CardRepository
import server.handlers.AnyQueryHandler
import server.queries.card.CardHistoryQuery

@Component
class CardByIdHistoryQueryHandler @Autowired constructor(private val cardRepository: CardRepository)
        : AnyQueryHandler<CardHistoryQuery>() {
    override fun handle(query: CardHistoryQuery) = TODO("create history")
}
