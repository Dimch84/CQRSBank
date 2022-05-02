package server.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import server.commands.Command
import server.db.postgresql.TempEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.events.command.StoreCommand
import server.events.command.TypeCommand.*
import server.handlers.account.command.AccountCreateCommandHandler
import server.handlers.account.command.AccountDeleteCommandHandler
import server.handlers.account.command.AccountUpdateMoneyCommandHandler
import server.handlers.account.command.AccountUpdatePlanCommandHandler
import server.handlers.account.query.AccountAllQueryHandler
import server.handlers.account.query.AccountByIdCardsQueryHandler
import server.handlers.account.query.AccountByIdMoneyQueryHandler
import server.handlers.account.query.AccountByIdQueryHandler
import server.handlers.card.command.*
import server.handlers.card.query.CardAllQueryHandler
import server.handlers.card.query.CardByIdHistoryQueryHandler
import server.handlers.card.query.CardByIdQueryHandler
import server.handlers.user.command.UserCreateCommandHandler
import server.handlers.user.command.UserDeleteCommandHandler
import server.handlers.user.command.UserUpdateProfileCommandHandler
import server.handlers.user.query.UserAllQueryHandler
import server.handlers.user.query.UserByLoginAccountsQueryHandler
import server.handlers.user.query.UserByLoginQueryHandler
import server.queries.Query
import server.queries.account.AccountAllQuery
import server.queries.account.AccountCardsQuery
import server.queries.account.AccountMoneyQuery
import server.queries.account.AccountQuery
import server.queries.card.CardAllQuery
import server.queries.card.CardHistoryQuery
import server.queries.card.CardQuery
import server.queries.user.UserAccountsQuery
import server.queries.user.UserAllQuery
import server.queries.user.UserQuery
import java.util.concurrent.Executors
import java.util.concurrent.Future

@Service
class CommandService @Autowired constructor(private val tempEventsRepository: TempEventsRepository,
                                            private val cardCreateCommandHandler: CardCreateCommandHandler,
                                            private val cardUpdateNameCommandHandler: CardUpdateNameCommandHandler,
                                            private val cardPayCommandHandler: CardPayCommandHandler,
                                            private val cardTransferCommandHandler: CardTransferCommandHandler,
                                            private val cardReceiptCommandHandler: CardReceiptCommandHandler,
                                            private val cardDeleteCommandHandler: CardDeleteCommandHandler,
                                            private val cardByIdQueryHandler: CardByIdQueryHandler,
                                            private val cardByIdHistoryQueryHandler: CardByIdHistoryQueryHandler,
                                            private val cardAllQueryHandler: CardAllQueryHandler,

                                            private val userCreateCommandHandler: UserCreateCommandHandler,
                                            private val userUpdateProfileCommandHandler: UserUpdateProfileCommandHandler,
                                            private val userDeleteCommandHandler: UserDeleteCommandHandler,
                                            private val userByLoginQueryHandler: UserByLoginQueryHandler,
                                            private val userByLoginAccountsQueryHandler: UserByLoginAccountsQueryHandler,
                                            private val userAllQueryHandler: UserAllQueryHandler,

                                            private val accountCreateCommandHandler: AccountCreateCommandHandler,
                                            private val accountUpdatePlanCommandHandler: AccountUpdatePlanCommandHandler,
                                            private val accountUpdateMoneyCommandHandler: AccountUpdateMoneyCommandHandler,
                                            private val accountDeleteCommandHandler: AccountDeleteCommandHandler,
                                            private val accountAllQueryHandler: AccountAllQueryHandler,
                                            private val accountByIdCardsQueryHandler: AccountByIdCardsQueryHandler,
                                            private val accountByIdMoneyQueryHandler: AccountByIdMoneyQueryHandler,
                                            private val accountByIdQueryHandler: AccountByIdQueryHandler) {
    private val executorService = Executors.newFixedThreadPool(10)
    private val log: Logger = LoggerFactory.getLogger(CommandService::class.java)

    private fun handle(simpleCommand: SimpleCommand, catch: Boolean=true) = try {
        when (simpleCommand.store.type) {
            CARD_CREATE_COMMAND         -> cardCreateCommandHandler.handle(simpleCommand)
            CARD_UPDATE_NAME_COMMAND    -> cardUpdateNameCommandHandler.handle(simpleCommand)
            CARD_PAY_COMMAND            -> cardPayCommandHandler.handle(simpleCommand)
            CARD_TRANSFER_COMMAND       -> cardTransferCommandHandler.handle(simpleCommand)
            CARD_RECEIPT_COMMAND        -> cardReceiptCommandHandler.handle(simpleCommand)
            CARD_DELETE_COMMAND         -> cardDeleteCommandHandler.handle(simpleCommand)

            USER_CREATE_COMMAND         -> userCreateCommandHandler.handle(simpleCommand)
            USER_UPDATE_PROFILE_COMMAND -> userUpdateProfileCommandHandler.handle(simpleCommand)
            USER_DELETE_COMMAND         -> userDeleteCommandHandler.handle(simpleCommand)

            ACCOUNT_CREATE_COMMAND      -> accountCreateCommandHandler.handle(simpleCommand)
            ACCOUNT_UPDATE_PLAN_COMMAND -> accountUpdatePlanCommandHandler.handle(simpleCommand)
            ACCOUNT_DELETE_COMMAND      -> accountDeleteCommandHandler.handle(simpleCommand)

            else -> Exception("command ${simpleCommand.store.type} not permitted")
        }
    } catch (ex: Exception) {
        if (!catch)
            throw ex
        log.error(ex.message)
        ex.message
    }

    fun send(command: Command): Any? {
        val simpleCommand = tempEventsRepository.save(SimpleCommand(StoreCommand(command, command.typeCommand)))
        val future: Future<Any> = executorService.submit<Any> { handle(simpleCommand) }
        return future.get()
    }

    fun send(query: Query) = try {
        when (query) {
            is CardQuery            -> cardByIdQueryHandler.handle(query)
            is CardHistoryQuery     -> cardByIdHistoryQueryHandler.handle(query)
            is CardAllQuery         -> cardAllQueryHandler.handle(query)

            is UserQuery            -> userByLoginQueryHandler.handle(query)
            is UserAccountsQuery    -> userByLoginAccountsQueryHandler.handle(query)
            is UserAllQuery         -> userAllQueryHandler.handle(query)

            is AccountQuery         -> accountByIdQueryHandler.handle(query)
            is AccountCardsQuery    -> accountByIdCardsQueryHandler.handle(query)
            is AccountMoneyQuery    -> accountByIdMoneyQueryHandler.handle(query)
            is AccountAllQuery      -> accountAllQueryHandler.handle(query)

            else                    -> throw Exception("wrong query")
        }
    } catch (ex: Exception) {
        log.error(ex.message)
        ex.message
    }
}
