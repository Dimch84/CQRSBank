package server.handlers.account.query

import client.api.abstractions.AccountBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.db.mongo.AccountRepository
import server.db.mongo.UserRepository
import server.handlers.AnyQueryHandler
import server.queries.account.AccountAllQuery

@Component
class AccountAllQueryHandler @Autowired constructor(private val accountRepository: AccountRepository, private val userRepository: UserRepository)
        : AnyQueryHandler<AccountAllQuery>() {
    override fun handle(query: AccountAllQuery): String {
        val userId_ = query.login?.let { userRepository.findByLogin(it)?.id } ?: throw Exception("wrong login")
        val accounts = accountRepository.findAllByUserId(userId_).map { AccountBody(it.money, it.userId, it.planId) }
        return GSON.toJson(accounts)
    }
}
