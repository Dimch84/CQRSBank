package server.observers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.abstractions.account.AnyAccountEventRes
import server.abstractions.account.AccountDeleteEventRes
import server.abstractions.account.AccountEventRes
import server.db.mongo.AccountRepository
import server.db.mongo.entities.AccountEntity

@Component
class ObserverAccount @Autowired constructor(private val accountRepository: AccountRepository): AnyObserver<AnyAccountEventRes> {
    override fun update(res: AnyAccountEventRes): Unit = when(res) {
        is AccountEventRes -> {
            accountRepository.save(AccountEntity(res.id!!, res.money!!, res.user_id!!, res.plan_id!!))
            Unit
        }
        is AccountDeleteEventRes -> {
            accountRepository.deleteById(res.id!!)
        }
        else -> throw Exception("wrong event")
    }
}
