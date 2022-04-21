package server.observers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.abstractions.CardEventRes
import server.db.mongo.CardRepository
import server.db.mongo.entities.CardEntity

@Component
class ObserverCard @Autowired constructor(private val cardRepository: CardRepository): AnyObserver<CardEventRes> {
    override fun update(domain: CardEventRes) {
        cardRepository.save(CardEntity(domain.id!!, domain.name!!, domain.type!!, domain.account_id!!))
    }
}
