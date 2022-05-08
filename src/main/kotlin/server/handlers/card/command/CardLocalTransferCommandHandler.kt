package server.handlers.card.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import server.abstractions.card.AnyCardEventRes
import server.commands.SendOnlyCommand
import server.commands.account.AccountUpdateMoneyCommand
import server.commands.card.CardLocalTransferCommand
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
                                                             private val accountSendOnlyCommandHandler: AccountSendOnlyCommandHandler)
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
