package server.handlers.user.query

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.db.mongo.UserRepository
import server.handlers.AnyQueryHandler
import server.queries.user.UserAccountsQuery

@Component
class UserByLoginAccountsQueryHandler @Autowired constructor(private val userRepository: UserRepository)
        : AnyQueryHandler<UserAccountsQuery>() {
    override fun handle(query: UserAccountsQuery) = TODO("do it")
}
