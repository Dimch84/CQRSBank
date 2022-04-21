package server.commands

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import server.commands.card.CardCreateCommand
import server.commands.card.CardUpdateNameCommand
import server.handlers.card.CardCreateCommandHandler
import server.handlers.card.CardUpdateNameCommandHandler

@Service
class CommandService @Autowired constructor(private val cardCreateCommandHandler: CardCreateCommandHandler,
                                            private val cardUpdateNameCommandHandler: CardUpdateNameCommandHandler
) {
    fun send(command: Command) {
        when (command) {
            is CardCreateCommand -> cardCreateCommandHandler.handle(command)
            is CardUpdateNameCommand -> cardUpdateNameCommandHandler.handle(command)
            else -> {

            }
        }
    }
}
