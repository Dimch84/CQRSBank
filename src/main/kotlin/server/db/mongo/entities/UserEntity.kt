package server.db.mongo.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
class UserEntity(@Id val id: Long, val name: String, val login: String, val password: String, val phone: String?,
                 val email: String?): AnyEntity {
    override fun toString() = "CardEntity(id=$id, name=$name, login=$login, password=$password, phone=$phone, " +
            "email=$email)"
}
