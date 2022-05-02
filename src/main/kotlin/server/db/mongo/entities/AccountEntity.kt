package server.db.mongo.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class AccountEntity(@Id val id: Long, val money: Long, val userId: Long, val planId: Long): AnyEntity {
    override fun toString() = "AccountEntity(id=$id, money=$money, userId=$userId, planId=$planId)"
}
