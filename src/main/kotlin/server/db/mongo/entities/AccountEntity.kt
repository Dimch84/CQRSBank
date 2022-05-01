package server.db.mongo.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class AccountEntity(@Id val id: Long, val money: Long, val user_id: Long, val plan_id: Long): AnyEntity {
    override fun toString() = "AccountEntity(id=$id, money=$money, user_id=$user_id, plan_id=$plan_id)"
}
