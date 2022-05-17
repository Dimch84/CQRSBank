package server.db.mongo.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class AccountEntity(@Id val id: Long, val name: String, val money: Long, val userId: Long, val planId: Long): AnyEntity {
    override fun toString() = "AccountEntity(id=$id, name=$name, money=$money, userId=$userId, planId=$planId)"
}
