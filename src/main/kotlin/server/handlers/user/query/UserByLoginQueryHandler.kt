package server.handlers.user.query

import client.api.abstractions.UserProfileBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.db.mongo.UserRepository
import server.handlers.AnyQueryHandler
import server.queries.user.UserQuery

@Component
class UserByLoginQueryHandler @Autowired constructor(private val userRepository: UserRepository)
        : AnyQueryHandler<UserQuery>() {
    override fun handle(query: UserQuery): String {
        val card = userRepository.findByLogin(query.login)?.run { UserProfileBody(id, name, login, phone, email) }
            ?: throw Exception("wrong login")
        return GSON.toJson(card)
    }
}
