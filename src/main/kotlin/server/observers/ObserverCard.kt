package server.observers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.abstractions.card.AnyCardEventRes
import server.abstractions.card.CardDeleteEventRes
import server.abstractions.card.CardEventRes
import server.db.mongo.CardRepository
import server.db.mongo.entities.CardEntity

@Component
class ObserverCard @Autowired constructor(private val cardRepository: CardRepository): AnyObserver<AnyCardEventRes> {
    override fun update(res: AnyCardEventRes): Unit = when(res) {
        is CardEventRes -> {
            cardRepository.save(CardEntity(res.id!!, res.name!!, res.type!!, res.cardNumber!!, res.expDate!!, res.cvv!!, res.accountId!!))
            Unit
        }
        is CardDeleteEventRes -> {
            cardRepository.deleteById(res.id!!)
        }
        else -> throw Exception("wrong event")
    }
}
