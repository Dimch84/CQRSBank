package server.handlers.card.command

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.abstractions.card.AnyCardEventRes
import server.commands.account.AccountUpdateMoneyCommand
import server.commands.card.CardPayCommand
import server.db.postgresql.CardEventsRepository
import server.db.postgresql.TempEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.events.command.StoreCommand
import server.handlers.account.command.AccountUpdateMoneyCommandHandler
import server.observers.ObserverCard

@Component
class CardPayCommandHandler @Autowired constructor(observerCard: ObserverCard,
                                                   private val cardEventsRepository: CardEventsRepository,
                                                   private val tempEventsRepository: TempEventsRepository,
                                                   private val accountUpdateMoneyCommandHandler: AccountUpdateMoneyCommandHandler)
    : AnyCardCommandHandler<AnyCardEventRes>(cardEventsRepository) {
    init {
        attach(observerCard)
    }

    override fun handle(simpleCommand: SimpleCommand): String {
        val event = (simpleCommand.store.command as CardPayCommand).event
        cardEvents(event.id).run {
            val cardUpd = update(event)
            val command = AccountUpdateMoneyCommand(event.money, cardUpd.accountId)
            accountUpdateMoneyCommandHandler.handle(SimpleCommand(StoreCommand(command, command.typeCommand)))
            cardEventsRepository.save(this)
            send(cardUpd)
            tempEventsRepository.deleteById(simpleCommand.id!!)
        }
        return "ok"
    }
}
