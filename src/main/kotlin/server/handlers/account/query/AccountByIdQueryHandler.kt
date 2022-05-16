package server.handlers.account.query

import client.api.abstractions.AccountBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.db.mongo.AccountRepository
import server.db.mongo.UserRepository
import server.handlers.AnyQueryHandler
import server.queries.account.AccountQuery

@Component
class AccountByIdQueryHandler @Autowired constructor(private val accountRepository: AccountRepository, private val userRepository: UserRepository)
        : AnyQueryHandler<AccountQuery>() {
    override fun handle(query: AccountQuery): String {
        val userId_ = query.login?.let { userRepository.findByLogin(it)?.id } ?: throw Exception("wrong login")
        val account = accountRepository.findByUserIdAndId(userId_, query.id)?.run { AccountBody(money, userId, planId) }
            ?: throw Exception("wrong account id")
        return GSON.toJson(account)
    }
}
