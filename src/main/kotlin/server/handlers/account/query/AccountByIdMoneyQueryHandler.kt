package server.handlers.account.query

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.db.mongo.AccountRepository
import server.db.mongo.UserRepository
import server.handlers.AnyQueryHandler
import server.queries.account.AccountMoneyQuery

@Component
class AccountByIdMoneyQueryHandler @Autowired constructor(private val accountRepository: AccountRepository, private val userRepository: UserRepository)
        : AnyQueryHandler<AccountMoneyQuery>() {
    override fun handle(query: AccountMoneyQuery): Long {
        val userId_ = query.login?.let { userRepository.findByLogin(it)?.id } ?: throw Exception("wrong login")
        return accountRepository.findByUserIdAndId(userId_, query.id)?.money ?: throw Exception("wrong account id")
    }
}
