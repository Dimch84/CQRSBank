package server.api

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import server.commands.card.*
import server.queries.card.CardAllQuery
import server.queries.card.CardHistoryQuery
import server.queries.card.CardQuery
import server.service.CommandService

@RestController
@Api(description = "cards commands")
class CardsCommandsController @Autowired constructor(private val service: CommandService) {
    @ApiOperation(value = "Get card by id")
    @PostMapping("/cardsCommands/byId")
    suspend fun postCardsByIdQuery(@RequestBody cardByIdQuery: CardQuery) =
        service.send(cardByIdQuery)

    @ApiOperation(value = "Get card history")
    @PostMapping("/cardsCommands/byIdHistory")
    suspend fun postCardsByIdHistoryQuery(@RequestBody cardHistoryQuery: CardHistoryQuery) =
        service.send(cardHistoryQuery)

    @ApiOperation(value = "Get all cards")
    @PostMapping("/cardsCommands/all")
    suspend fun postCardsAllQuery(@RequestBody cardAllQuery: CardAllQuery) =
        service.send(cardAllQuery)

    //

    @ApiOperation(value = "Return id new card")
    @PostMapping("/cardsCommands/create")
    suspend fun postCardsCreate(@RequestBody cardCreateCommand: CardCreateCommand) =
        service.send(cardCreateCommand)

    @ApiOperation(value = "Update card name, return 'ok'")
    @PostMapping("/cardsCommands/updateName")
    suspend fun postCardsUpdateName(@RequestBody cardUpdateNameCommand: CardUpdateNameCommand) =
        service.send(cardUpdateNameCommand)

    @ApiOperation(value = "Pay, return 'ok'")
    @PostMapping("/cardsCommands/payment")
    suspend fun postCardsPayment(@RequestBody cardPayCommand: CardPayCommand) =
        service.send(cardPayCommand)

    @ApiOperation(value = "Transfer, return 'ok'")
    @PostMapping("/cardsCommands/transfer")
    suspend fun postCardsTransfer(@RequestBody cardTransferCommand: CardTransferCommand) =
        service.send(cardTransferCommand)

    @ApiOperation(value = "Receipt, return 'ok'")
    @PostMapping("/cardsCommands/receipt")
    suspend fun postCardsReceipt(@RequestBody cardReceiptCommand: CardReceiptCommand) =
        service.send(cardReceiptCommand)

    @ApiOperation(value = "Delete card, return 'ok'")
    @PostMapping("/cardsCommands/delete")
    suspend fun postCardsDelete(@RequestBody cardDeleteCommand: CardDeleteCommand) =
        service.send(cardDeleteCommand)
}
