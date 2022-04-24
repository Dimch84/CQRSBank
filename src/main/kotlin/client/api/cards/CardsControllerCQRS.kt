package client.api.cards

import client.api.abstractions.CardBody
import client.api.abstractions.UpdateName
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import client.api.requests.sendToUrl
import server.commands.card.CardCreateCommand
import server.commands.card.CardUpdateNameCommand

// TODO(not hardcode http://localhost:8080)
@RestController
@Api(description = "cards operations cqrs")
class CardsControllerCQRS {
    private val log: Logger = LoggerFactory.getLogger(CardsControllerCQRS::class.java)

//    @ApiOperation(value = "Return card")
//    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
//    @GetMapping("/cards/{id}")
//    suspend fun getCardsById(@PathVariable id: Int): CardBody {
//        log.info("GET Response: /cards/${id}")
//        return Card(id).data
//    }
//
//    @ApiOperation(value = "Return card history")
//    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
//    @GetMapping("/cards/{id}/history")
//    suspend fun getCardsHistoryById(@PathVariable id: Int): Any {
//        log.info("GET Response: /cards/${id}/history")
//        return Card(id).history
//    }

    @ApiOperation(value = "Add card")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/cards")
    suspend fun postCards(@RequestBody cardBody: CardBody): String {
        log.info("POST Response: /cqrs/cards")
        val command = CardCreateCommand(cardBody.name, cardBody.type, cardBody.account_id)
        return sendToUrl("http://localhost:8080/cardsCommands/create", command.toMap())
    }

    @ApiOperation(value = "Update card name")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/cards/{id}/updateName")
    suspend fun postCardsUpdateNameById(@PathVariable id: Long, @RequestBody cardName: UpdateName): String {
        log.info("POST Response: /cqrs/cards/${id}/updateName")
        val command = CardUpdateNameCommand(cardName.name, id)
        return sendToUrl("http://localhost:8080/cardsCommands/updateName", command.toMap())
    }

//    @ApiOperation(value = "Card payment")
//    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
//    @PostMapping("/cards/{id}/payment")
//    suspend fun postCardsPaymentById(@PathVariable id: Int, @RequestBody paymentBody: PaymentBody): Any {
//        log.info("POST Response: /cards/${id}/payment")
//        return Card(id).pay(paymentBody)
//    }
//
//    @ApiOperation(value = "Card transfer")
//    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
//    @PostMapping("/cards/{id}/transfer")
//    suspend fun postCardsTransferById(@PathVariable id: Int, @RequestBody transferBody: TransferBody): Any {
//        log.info("POST Response: /cards/${id}/transfer")
//        return Card(id).transfer(transferBody)
//    }
//
//    @ApiOperation(value = "Card receipt")
//    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
//    @PostMapping("/cards/{id}/receipt")
//    suspend fun postCardsReceiptById(@PathVariable id: Int, @RequestBody transferBody: TransferBody): Any {
//        log.info("POST Response: /cards/${id}/transfer")
//        return Card(id).receipt(transferBody)
//    }
//
//    @ApiOperation(value = "Delete card")
//    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
//    @DeleteMapping("/cards/{id}")
//    suspend fun deleteCardsById(@PathVariable id: Int) {
//        log.info("DELETE Response: /cards/${id}")
//        return Card(id).delete()
//    }
}
