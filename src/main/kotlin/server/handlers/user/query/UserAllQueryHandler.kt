package server.handlers.user.query

import client.api.abstractions.UserProfileBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.db.mongo.UserRepository
import server.handlers.AnyQueryHandler
import server.queries.user.UserAllQuery

@Component
class UserAllQueryHandler @Autowired constructor(private val userRepository: UserRepository)
        : AnyQueryHandler<UserAllQuery>() {
    override fun handle(query: UserAllQuery): String {
        val userProfiles = userRepository.findAll().map { UserProfileBody(it.name, it.login, it.phone, it.email) }
        return GSON.toJson(userProfiles)
    }
}
