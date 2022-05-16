package server.handlers.card.query

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import server.db.mongo.AccountRepository
import server.db.mongo.CardRepository
import server.db.mongo.UserRepository
import server.handlers.AnyQueryHandler
import server.queries.card.CardMoneyQuery

@Component
class CardByIdMoneyQueryHandler @Autowired constructor(private val cardRepository: CardRepository,
                                                       private val userRepository: UserRepository,
                                                       private val accountRepository: AccountRepository)
        : AnyQueryHandler<CardMoneyQuery>() {
    override fun handle(query: CardMoneyQuery): Long {
        val userId_ = query.login?.let { userRepository.findByLogin(it)?.id } ?: throw Exception("wrong login")
        val accountId = cardRepository.findByIdOrNull(query.id)?.accountId ?: throw Exception("wrong card id")
        accountRepository.findByUserIdAndId(userId_, accountId) ?: throw Exception("wrong account id")
        return accountRepository.findByIdOrNull(accountId)?.money ?: throw Exception("wrong account id")
    }
}
