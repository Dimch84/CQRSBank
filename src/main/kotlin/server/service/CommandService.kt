package server.service

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import server.commands.Command
import server.db.postgresql.TempEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.events.command.StoreCommand
import java.util.concurrent.Executors
import java.util.concurrent.Future
import server.events.command.TypeCommand.*
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
import server.queries.card.CardAllQuery
import server.queries.card.CardHistoryQuery
import server.queries.card.CardQuery
import server.queries.user.UserAccountsQuery
import server.queries.user.UserAllQuery
import server.queries.user.UserQuery

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
                                            private val userAllQueryHandler: UserAllQueryHandler) {
    private val executorService = Executors.newFixedThreadPool(10)
    private val log: Logger = LoggerFactory.getLogger(CommandService::class.java)

    // TODO(optimize later)
    private fun handle(simpleCommand: SimpleCommand) = try {
        when (simpleCommand.store.type) {
            CARD_CREATE_COMMAND -> cardCreateCommandHandler.handle(simpleCommand)
            CARD_UPDATE_NAME_COMMAND -> cardUpdateNameCommandHandler.handle(simpleCommand)
            CARD_PAY_COMMAND -> cardPayCommandHandler.handle(simpleCommand)
            CARD_TRANSFER_COMMAND -> cardTransferCommandHandler.handle(simpleCommand)
            CARD_RECEIPT_COMMAND -> cardReceiptCommandHandler.handle(simpleCommand)
            CARD_DELETE_COMMAND -> cardDeleteCommandHandler.handle(simpleCommand)

            USER_CREATE_COMMAND -> userCreateCommandHandler.handle(simpleCommand)
            USER_UPDATE_PROFILE_COMMAND -> userUpdateProfileCommandHandler.handle(simpleCommand)
            USER_DELETE_COMMAND -> userDeleteCommandHandler.handle(simpleCommand)
        }
    } catch (ex: Exception) {
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
            is CardQuery -> cardByIdQueryHandler.handle(query)
            is CardHistoryQuery -> cardByIdHistoryQueryHandler.handle(query)
            is CardAllQuery -> cardAllQueryHandler.handle(query)

            is UserQuery -> userByLoginQueryHandler.handle(query)
            is UserAccountsQuery -> userByLoginAccountsQueryHandler.handle(query)
            is UserAllQuery -> userAllQueryHandler.handle(query)
            else -> throw Exception("wrong query")
        }
    } catch (ex: Exception) {
        log.error(ex.message)
        ex.message
    }
}
