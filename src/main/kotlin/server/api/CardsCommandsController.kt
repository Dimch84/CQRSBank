package server.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import server.commands.card.CardCreateCommand
import server.commands.card.CardUpdateNameCommand
import server.queries.card.CardByIdQuery
import server.service.CommandService

@RestController
@Api(description = "cards commands")
class CardsCommandsController @Autowired constructor(private val service: CommandService) {
    @ApiOperation(value = "Get card by id")
    @PostMapping("/cardsCommands/byId")
    suspend fun postCardsByIdQuery(@RequestBody cardByIdQuery: CardByIdQuery): String {
        return service.send(cardByIdQuery)
    }

    @ApiOperation(value = "Return id new card")
    @PostMapping("/cardsCommands/create")
    suspend fun postCardsCreate(@RequestBody cardCreateCommand: CardCreateCommand) =
        service.send(cardCreateCommand)

    @ApiOperation(value = "Update card name, return 'ok'")
    @PostMapping("/cardsCommands/updateName")
    suspend fun postCardsUpdateName(@RequestBody cardUpdateNameCommand: CardUpdateNameCommand) =
        service.send(cardUpdateNameCommand)
}
