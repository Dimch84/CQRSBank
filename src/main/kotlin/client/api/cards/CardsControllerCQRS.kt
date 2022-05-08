package client.api.cards

import client.api.abstractions.*
import client.api.requests.sendToUrl
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import server.abstractions.card.HistoryMode
import server.commands.card.*
import server.queries.card.CardAllQuery
import server.queries.card.CardHistoryQuery
import server.queries.card.CardMoneyQuery
import server.queries.card.CardQuery
import server.queries.card.CardQueryByNumber

// TODO(not hardcode http://localhost:8080)
@RestController
@Api(description = "cards operations cqrs")
@CrossOrigin(origins = ["http://localhost:8081", "http://172.20.10.13:8081"])
class CardsControllerCQRS {
    companion object {
        private val GSON = Gson()
    }
    private val log: Logger = LoggerFactory.getLogger(CardsControllerCQRS::class.java)
    private val userLogin: String by lazy { SecurityContextHolder.getContext().authentication.name }

    @ApiOperation(value = "Return card")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/cards/{id}")
    suspend fun getCardsById(@PathVariable id: Long): String {  // CardBody
        log.info("GET Response: /cqrs/cards/${id}")
        val query = CardQuery(id, userLogin)
        val cardJson = sendToUrl("http://localhost:8080/cardsCommands/byId", query.toMap())
        return try {
            cardJson    // for test only
//            val card: CardBody = GSON.fromJson(cardJson, object : TypeToken<CardBody>() {}.type)
//            card.toString()
        } catch (ex: Exception) {
            cardJson
        }
    }

    @ApiOperation(value = "Return card money")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/cards/{id}/money")
    suspend fun getCardsMoneyById(@PathVariable id: Long): String {
        log.info("GET Response: /cqrs/cards/${id}/money")
        val query = CardMoneyQuery(id, userLogin)
        return sendToUrl("http://localhost:8080/cardsCommands/byIdMoney", query.toMap())
    }

    @ApiOperation(value = "Return card by name")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/cards/byNumber/{cardNumber}")
    suspend fun getCardsById(@PathVariable cardNumber: String): String {  // CardBody
        log.info("GET Response: /cqrs/cards/byNumber${cardNumber}")
        val query = CardQueryByNumber(cardNumber)
        val cardJson = sendToUrl("http://localhost:8080/cardsCommands/byNumber", query.toMap())
        return try {
            cardJson
        } catch (ex: Exception) {
            cardJson
        }
    }

    @ApiOperation(value = "Return card history")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/cards/{id}/history")
    suspend fun getCardsHistoryById(@PathVariable id: Long): Any {
        log.info("GET Response: /cards/${id}/history")
        val query = CardHistoryQuery(id, login=userLogin)
        return sendToUrl("http://localhost:8080/cardsCommands/byIdHistory", query.toMap())
    }

    @ApiOperation(value = "Return card history (only changing balance)")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/cards/{id}/history/pays")
    suspend fun getCardsHistoryPaysById(@PathVariable id: Long): Any {
        log.info("GET Response: /cards/${id}/history/pays")
        val query = CardHistoryQuery(id, HistoryMode.PAYS, userLogin)
        return sendToUrl("http://localhost:8080/cardsCommands/byIdHistory", query.toMap())
    }

    @ApiOperation(value = "Return all user cards")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/cards/all")
    suspend fun getCardsAll(): List<CardBody> {
        log.info("GET Response: /cqrs/cards/all")
        val query = CardAllQuery(userLogin)
        val cardsJson = sendToUrl("http://localhost:8080/cardsCommands/all", query.toMap())
        return try {
            GSON.fromJson(cardsJson, object : TypeToken<List<CardBody>>() {}.type)
        } catch (ex: Exception) {
            listOf()
        }
    }

    //

    @ApiOperation(value = "Add card")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/cards")
    suspend fun postCards(@RequestBody cardBody: CardBody): String {
        log.info("POST Response: /cqrs/cards")
        val command = CardCreateCommand(cardBody.name, cardBody.type, cardBody.accountId, cardBody.cardNumber, cardBody.expDate, cardBody.cvv)
        return sendToUrl("http://localhost:8080/cardsCommands/create", command.toMap())
    }

    @ApiOperation(value = "Update card name")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/cards/{id}/updateName")
    suspend fun postCardsUpdateNameById(@PathVariable id: Long, @RequestBody cardName: UpdateName): String {
        log.info("POST Response: /cqrs/cards/${id}/updateName")
        val command = CardUpdateNameCommand(cardName.name, id, userLogin)
        return sendToUrl("http://localhost:8080/cardsCommands/updateName", command.toMap())
    }

    @ApiOperation(value = "Card payment")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/cards/{id}/payment")
    suspend fun postCardsPaymentById(@PathVariable id: Long, @RequestBody paymentBody: PaymentBody): String {
        log.info("POST Response: /cqrs/cards/${id}/payment")
        val command = CardPayCommand(paymentBody.money, id, userLogin)
        return sendToUrl("http://localhost:8080/cardsCommands/payment", command.toMap())
    }

    @ApiOperation(value = "Card transfer")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/cards/{id}/transfer")
    suspend fun postCardsTransferById(@PathVariable id: Long, @RequestBody transferBody: TransferBody): String {
        log.info("POST Response: /cqrs/cards/${id}/transfer")
        val command = CardTransferCommand(transferBody.money, id, userLogin)
        return sendToUrl("http://localhost:8080/cardsCommands/transfer", command.toMap())
    }

    @ApiOperation(value = "Card receipt")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/cards/{id}/receipt")
    suspend fun postCardsReceiptById(@PathVariable id: Long, @RequestBody transferBody: TransferBody): String {
        log.info("POST Response: /cqrs/cards/${id}/receipt")
        val command = CardReceiptCommand(transferBody.money, id, userLogin)
        return sendToUrl("http://localhost:8080/cardsCommands/receipt", command.toMap())
    }

    @ApiOperation(value = "Transfer between own cards")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/cards/{id}/localTransfer")
    suspend fun postCardsLocalTransferById(@PathVariable id: Long, @RequestBody transferToBody: TransferToBody): String {
        log.info("POST Response: /cqrs/cards/${id}/localTransfer")
        val command = CardLocalTransferCommand(transferToBody.money, id, transferToBody.idTo, userLogin)
        return sendToUrl("http://localhost:8080/cardsCommands/localTransfer", command.toMap())
    }

    @ApiOperation(value = "Delete card")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @DeleteMapping("/cqrs/cards/{id}")
    suspend fun deleteCardsById(@PathVariable id: Long): String {
        log.info("DELETE Response: /cqrs/cards/${id}")
        val command = CardDeleteCommand(id, userLogin)
        return sendToUrl("http://localhost:8080/cardsCommands/delete", command.toMap())
    }
}
