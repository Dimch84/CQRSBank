package server.db.mongo.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class CardEntity(@Id val id: Long, val name: String, val type: String, val account_id: Int): AnyEntity {
    override fun toString() = "CardEntity(id=$id, name=$name, type=$type, account_id=$account_id)"
}
