package server.handlers.account.query

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.db.mongo.AccountRepository
import server.handlers.AnyQueryHandler
import server.queries.account.AccountMoneyQuery

@Component
class AccountByIdMoneyQueryHandler @Autowired constructor(private val accountRepository: AccountRepository)
        : AnyQueryHandler<AccountMoneyQuery>() {
    override fun handle(query: AccountMoneyQuery) = accountRepository.findById(query.id).get().money
}
