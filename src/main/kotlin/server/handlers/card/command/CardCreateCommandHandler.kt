package server.handlers.card.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import server.abstractions.card.AnyCardEventRes
import server.commands.card.CardCreateCommand
import server.db.mongo.AccountRepository
import server.db.mongo.CardRepository
import server.db.mongo.UserRepository
import server.db.postgresql.CardEventsRepository
import server.db.postgresql.TempEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.observers.ObserverCard

@Component
class CardCreateCommandHandler @Autowired constructor(observerCard: ObserverCard,
                                                      private val cardEventsRepository: CardEventsRepository,
                                                      private val accountRepository: AccountRepository,
                                                      private val tempEventsRepository: TempEventsRepository,
                                                      private val userRepository: UserRepository,
                                                      private val cardRepository: CardRepository)
    : AnyCardCommandHandler<AnyCardEventRes>(cardEventsRepository) {
    init {
        attach(observerCard)
    }

    @Transactional(rollbackFor = [Exception::class], isolation = Isolation.SERIALIZABLE)
    override fun handle(simpleCommand: SimpleCommand): Long {
        val event = (simpleCommand.store.command as CardCreateCommand).event
        val userId_ = userRepository.findByLogin(event.login)?.id ?: throw Exception("wrong login")
        accountRepository.findByUserIdAndId(userId_, event.accountId) ?: throw Exception("wrong account id")
        return cardEvents().run {
            val cardUpd = update(event)
            cardEventsRepository.save(this)
            tempEventsRepository.deleteById(simpleCommand.id!!)
            val retId = id ?: throw Exception("CardCreateCommandHandler id error")
            send(cardUpd)
            retId
        }
    }
}
