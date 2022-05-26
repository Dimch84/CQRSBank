package server.handlers.card.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import server.abstractions.card.AnyCardEventRes
import server.commands.SendOnlyCommand
import server.commands.account.AccountUpdateMoneyCommand
import server.commands.card.CardLocalTransferCommand
import server.db.mongo.AccountRepository
import server.db.mongo.CardRepository
import server.db.mongo.UserRepository
import server.db.postgresql.CardEventsRepository
import server.db.postgresql.TempEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.events.command.StoreCommand
import server.events.command.TypeCommand.SEND_ONLY_COMMAND
import server.handlers.account.command.AccountSendOnlyCommandHandler
import server.handlers.account.command.AccountUpdateMoneyCommandHandler
import server.observers.ObserverCard

@Component
class CardLocalTransferCommandHandler @Autowired constructor(observerCard: ObserverCard,
                                                             private val cardEventsRepository: CardEventsRepository,
                                                             private val tempEventsRepository: TempEventsRepository,
                                                             private val accountUpdateMoneyCommandHandler: AccountUpdateMoneyCommandHandler,
                                                             private val accountSendOnlyCommandHandler: AccountSendOnlyCommandHandler,
                                                             private val cardRepository: CardRepository,
                                                             private val userRepository: UserRepository,
                                                             private val accountRepository: AccountRepository)
    : AnyCardCommandHandler<AnyCardEventRes>(cardEventsRepository) {
    init {
        attach(observerCard)
    }

    override fun rollBack(simpleCommand: SimpleCommand) {
        val event = (simpleCommand.store.command as CardLocalTransferCommand).event
        cardEvents(event.idFrom).update().run {
            send(this)
            accountSendOnlyCommandHandler.handle(SimpleCommand(StoreCommand(SendOnlyCommand(accountId), SEND_ONLY_COMMAND)))
        }
        cardEvents(event.idTo).update().run {
            send(this)
            accountSendOnlyCommandHandler.handle(SimpleCommand(StoreCommand(SendOnlyCommand(accountId), SEND_ONLY_COMMAND)))
        }
        tempEventsRepository.deleteById(simpleCommand.id!!)
    }

    @Transactional(rollbackFor = [Exception::class], isolation = Isolation.SERIALIZABLE)
    override fun handle(simpleCommand: SimpleCommand): String {
        val event = (simpleCommand.store.command as CardLocalTransferCommand).event
        val userId_ = userRepository.findByLogin(event.login)?.id ?: throw Exception("wrong login")
        val accountIdFrom = cardRepository.findByIdOrNull(event.idFrom)?.accountId ?: throw Exception("wrong card id from")
        accountRepository.findByUserIdAndId(userId_, accountIdFrom) ?: throw Exception("wrong account id from")
        val accountIdTo = cardRepository.findByIdOrNull(event.idTo)?.accountId ?: throw Exception("wrong card id to")
        accountRepository.findByUserIdAndId(userId_, accountIdTo) ?: throw Exception("wrong account id to")
        val cardUpdFrom = cardEvents(event.idFrom).run {
            val cardUpd = update(event)
            val command = AccountUpdateMoneyCommand(event.money, cardUpd.accountId)
            accountUpdateMoneyCommandHandler.handle(SimpleCommand(StoreCommand(command, command.typeCommand)))
            cardEventsRepository.save(this)
            cardUpd
        }
        val cardUpdTo = cardEvents(event.idTo).run {
            val cardUpd = update(event)
            val command = AccountUpdateMoneyCommand(-event.money, cardUpd.accountId)
            accountUpdateMoneyCommandHandler.handle(SimpleCommand(StoreCommand(command, command.typeCommand)))
            cardEventsRepository.save(this)
            cardUpd
        }
        tempEventsRepository.deleteById(simpleCommand.id!!)
        send(cardUpdFrom)
        send(cardUpdTo)
        return "ok"
    }
}
