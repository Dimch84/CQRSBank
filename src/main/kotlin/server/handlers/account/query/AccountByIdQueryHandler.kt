package server.handlers.account.query

import client.api.abstractions.AccountBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.db.mongo.AccountRepository
import server.handlers.AnyQueryHandler
import server.queries.account.AccountQuery

@Component
class AccountByIdQueryHandler @Autowired constructor(private val accountRepository: AccountRepository)
        : AnyQueryHandler<AccountQuery>() {
    override fun handle(query: AccountQuery): String {
        val account = accountRepository.findById(query.id).get().run { AccountBody(money, user_id, plan_id) }
        return GSON.toJson(account)
    }
}
