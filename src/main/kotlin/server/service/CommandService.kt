package server.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import server.commands.Command
import server.commands.card.CardCreateCommand
import server.commands.card.CardUpdateNameCommand
import server.db.postgresql.TempEventsRepository
import server.db.postgresql.entities.SimpleCommand
import server.events.command.StoreCommand
import server.handlers.card.CardCreateCommandHandler
import server.handlers.card.CardUpdateNameCommandHandler
import java.util.concurrent.Executors
import java.util.concurrent.Future
import server.events.command.TypeCommand.*

@Service
class CommandService @Autowired constructor(private val cardCreateCommandHandler: CardCreateCommandHandler,
                                            private val cardUpdateNameCommandHandler: CardUpdateNameCommandHandler,
                                            private val tempEventsRepository: TempEventsRepository) {
    private val executorService = Executors.newFixedThreadPool(10)

    private fun handle(simpleCommand: SimpleCommand) = when (simpleCommand.command.type) {
        CARD_CREATE_COMMAND -> cardCreateCommandHandler.handle(simpleCommand)
        CARD_UPDATE_NAME_COMMAND -> cardUpdateNameCommandHandler.handle(simpleCommand)
    }

    fun send(command: Command): Any? {
        val simpleCommand = tempEventsRepository.save(SimpleCommand(StoreCommand(command, command.typeCommand)))
        val future: Future<Any> = executorService.submit<Any> { handle(simpleCommand) }
        return future.get()
    }
}
