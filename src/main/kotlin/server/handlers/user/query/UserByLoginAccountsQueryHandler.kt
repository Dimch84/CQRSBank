package server.handlers.user.query

import client.api.abstractions.AccountBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.db.mongo.AccountRepository
import server.db.mongo.UserRepository
import server.handlers.AnyQueryHandler
import server.queries.user.UserAccountsQuery

@Component
class UserByLoginAccountsQueryHandler @Autowired constructor(private val accountRepository: AccountRepository,
                                                             private val userRepository: UserRepository)
        : AnyQueryHandler<UserAccountsQuery>() {
    override fun handle(query: UserAccountsQuery) : String {
        val userId = userRepository.findByLogin(query.login)?.id ?: throw Exception("wrong login")
        val accounts = accountRepository.findAllByUserId(userId).map { AccountBody(it.money, it.userId, it.planId) }
        return GSON.toJson(accounts)
    }
}
