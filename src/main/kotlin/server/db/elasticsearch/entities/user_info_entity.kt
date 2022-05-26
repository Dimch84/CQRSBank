package server.db.elasticsearch.entities

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document


@Document
class user_info_entity(@Id val id: Long, val data: List<String> = listOf()) {
    override fun toString() = "user_info_entity(id=$id, data=$data)"
}
