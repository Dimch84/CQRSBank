package server.handlers.account.query

import client.api.abstractions.AccountBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.db.mongo.AccountRepository
import server.handlers.AnyQueryHandler
import server.queries.account.AccountAllQuery

@Component
class AccountAllQueryHandler @Autowired constructor(private val accountRepository: AccountRepository)
        : AnyQueryHandler<AccountAllQuery>() {
    override fun handle(query: AccountAllQuery): String {
        val account = accountRepository.findAll().map { AccountBody(it.money, it.user_id, it.plan_id) }
        return GSON.toJson(account)
    }
}
