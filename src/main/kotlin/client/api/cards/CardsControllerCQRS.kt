package client.api.cards

import client.api.abstractions.CardBody
import client.api.abstractions.PaymentBody
import client.api.abstractions.TransferBody
import client.api.abstractions.UpdateName
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import client.api.requests.sendToUrl
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import server.commands.card.*
import server.queries.card.CardHistoryQuery
import server.queries.card.CardQuery

// TODO(not hardcode http://localhost:8080)
@RestController
@Api(description = "cards operations cqrs")
class CardsControllerCQRS {
    companion object {
        private val GSON = Gson()
    }
    private val log: Logger = LoggerFactory.getLogger(CardsControllerCQRS::class.java)

    @ApiOperation(value = "Return card")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/cards/{id}")
    suspend fun getCardsById(@PathVariable id: Long): String {  // CardBody
        log.info("GET Response: /cqrs/cards/${id}")
        val query = CardQuery(id)
        val cardJson = sendToUrl("http://localhost:8080/cardsCommands/byId", query.toMap())
        return try {
            val card: CardBody = GSON.fromJson(cardJson, object : TypeToken<CardBody>() {}.type)
            card.toString()
        } catch (ex: Exception) {
            cardJson
        }
    }

    @ApiOperation(value = "Return card history")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/cards/{id}/history")
    suspend fun getCardsHistoryById(@PathVariable id: Long): Any {
        log.info("GET Response: /cards/${id}/history")
        val query = CardHistoryQuery(id)
        return sendToUrl("http://localhost:8080/cardsCommands/byIdHistory", query.toMap())
    }

    //

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

    @ApiOperation(value = "Card payment")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/cards/{id}/payment")
    suspend fun postCardsPaymentById(@PathVariable id: Long, @RequestBody paymentBody: PaymentBody): String {
        log.info("POST Response: /cqrs/cards/${id}/payment")
        val command = CardPayCommand(paymentBody.money, id)
        return sendToUrl("http://localhost:8080/cardsCommands/payment", command.toMap())
    }

    @ApiOperation(value = "Card transfer")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/cards/{id}/transfer")
    suspend fun postCardsTransferById(@PathVariable id: Long, @RequestBody transferBody: TransferBody): String {
        log.info("POST Response: /cqrs/cards/${id}/transfer")
        val command = CardTransferCommand(transferBody.money, id)
        return sendToUrl("http://localhost:8080/cardsCommands/transfer", command.toMap())
    }

    @ApiOperation(value = "Card receipt")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/cards/{id}/receipt")
    suspend fun postCardsReceiptById(@PathVariable id: Long, @RequestBody transferBody: TransferBody): String {
        log.info("POST Response: /cqrs/cards/${id}/receipt")
        val command = CardReceiptCommand(transferBody.money, id)
        return sendToUrl("http://localhost:8080/cardsCommands/receipt", command.toMap())
    }

    @ApiOperation(value = "Delete card")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @DeleteMapping("/cqrs/cards/{id}")
    suspend fun deleteCardsById(@PathVariable id: Long): String {
        log.info("DELETE Response: /cqrs/cards/${id}")
        val command = CardDeleteCommand(id)
        return sendToUrl("http://localhost:8080/cardsCommands/delete", command.toMap())
    }
}