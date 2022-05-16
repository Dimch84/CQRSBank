package server.handlers.account.query

import client.api.abstractions.CardBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.db.mongo.AccountRepository
import server.db.mongo.CardRepository
import server.db.mongo.UserRepository
import server.handlers.AnyQueryHandler
import server.queries.account.AccountCardsQuery

@Component
class AccountByIdCardsQueryHandler @Autowired constructor(private val accountRepository: AccountRepository,
                                                          private val cardRepository: CardRepository,
                                                          private val userRepository: UserRepository)
        : AnyQueryHandler<AccountCardsQuery>() {
    override fun handle(query: AccountCardsQuery): String {
        val userId_ = query.login?.let { userRepository.findByLogin(it)?.id } ?: throw Exception("wrong login")
        accountRepository.findByUserIdAndId(userId_, query.id) ?: throw Exception("wrong account id")
        val cards = cardRepository.findByAccountId(query.id).map { CardBody(it.name, it.type, it.accountId) }
        return GSON.toJson(cards)
    }
}
