package server.db.mongo.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class CardEntity(@Id val id: Long, val name: String, val type: String,
                 val cardNumber: String, val expDate: String, val cvv: String,
                 val accountId: Long): AnyEntity {
    override fun toString() = "CardEntity(id=$id, name=$name, type=$type, accountId=$accountId, " +
            "cardNumber = $cardNumber, expDate = $expDate, cvv = $cvv)"
}
