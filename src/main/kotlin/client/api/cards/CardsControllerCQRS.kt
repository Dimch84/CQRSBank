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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import server.abstractions.card.HistoryMode
import server.commands.card.*
import server.db.mongo.UserRepository
import server.db.mongo.entities.UserEntity
import server.queries.card.CardAllQuery
import server.queries.card.CardHistoryQuery
import server.queries.card.CardMoneyQuery
import server.queries.card.CardQuery

// TODO(not hardcode http://localhost:8080)
@RestController
@Api(description = "cards operations cqrs")
class CardsControllerCQRS @Autowired constructor(private val userRepository: UserRepository) {
    companion object {
        private val GSON = Gson()
    }
    private val log: Logger = LoggerFactory.getLogger(CardsControllerCQRS::class.java)
    private val userLogin: String by lazy { SecurityContextHolder.getContext().authentication.name }
    private val userEntity: UserEntity
        get() = userRepository.findByLogin(userLogin) ?: throw Exception("wrong login")

    @ApiOperation(value = "Return card")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/cards/{id}")
    suspend fun getCardsById(@PathVariable id: Long): String {  // CardBody
        log.info("GET Response: /cqrs/cards/${id}")
        val (login, password) = userEntity.run { login to password }
        val query = CardQuery(id, login)
        val cardJson = sendToUrl("http://localhost:8080/cardsCommands/byId", query.toMap(), login=login, password=password)
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
        val (login, password) = userEntity.run { login to password }
        val query = CardMoneyQuery(id, login)
        return sendToUrl("http://localhost:8080/cardsCommands/byIdMoney", query.toMap(), login=login, password=password)
    }

    @ApiOperation(value = "Return card history")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/cards/{id}/history")
    suspend fun getCardsHistoryById(@PathVariable id: Long): Any {
        log.info("GET Response: /cards/${id}/history")
        val (login, password) = userEntity.run { login to password }
        val query = CardHistoryQuery(id, login=login)
        return sendToUrl("http://localhost:8080/cardsCommands/byIdHistory", query.toMap(), login=login, password=password)
    }

    @ApiOperation(value = "Return card history (only changing balance)")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/cards/{id}/history/pays")
    suspend fun getCardsHistoryPaysById(@PathVariable id: Long): Any {
        log.info("GET Response: /cards/${id}/history/pays")
        val (login, password) = userEntity.run { login to password }
        val query = CardHistoryQuery(id, HistoryMode.PAYS, login)
        return sendToUrl("http://localhost:8080/cardsCommands/byIdHistory", query.toMap(), login=login, password=password)
    }

    @ApiOperation(value = "Return all user cards")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/cards/all")
    suspend fun getCardsAll(): List<CardBody> {
        log.info("GET Response: /cqrs/cards/all")
        val (login, password) = userEntity.run { login to password }
        val query = CardAllQuery(login)
        val cardsJson = sendToUrl("http://localhost:8080/cardsCommands/all", query.toMap(), login=login, password=password)
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
        val (login, password) = userEntity.run { login to password }
        val command = CardCreateCommand(cardBody.name, cardBody.type, cardBody.accountId, login)
        return sendToUrl("http://localhost:8080/cardsCommands/create", command.toMap(), login=login, password=password)
    }

    @ApiOperation(value = "Update card name")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/cards/{id}/updateName")
    suspend fun postCardsUpdateNameById(@PathVariable id: Long, @RequestBody cardName: UpdateName): String {
        log.info("POST Response: /cqrs/cards/${id}/updateName")
        val (login, password) = userEntity.run { login to password }
        val command = CardUpdateNameCommand(cardName.name, id, login)
        return sendToUrl("http://localhost:8080/cardsCommands/updateName", command.toMap(), login=login, password=password)
    }

    @ApiOperation(value = "Card payment")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/cards/{id}/payment")
    suspend fun postCardsPaymentById(@PathVariable id: Long, @RequestBody paymentBody: PaymentBody): String {
        log.info("POST Response: /cqrs/cards/${id}/payment")
        val (login, password) = userEntity.run { login to password }
        val command = CardPayCommand(paymentBody.money, id, login)
        return sendToUrl("http://localhost:8080/cardsCommands/payment", command.toMap(), login=login, password=password)
    }

    @ApiOperation(value = "Card transfer")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/cards/{id}/transfer")
    suspend fun postCardsTransferById(@PathVariable id: Long, @RequestBody transferBody: TransferBody): String {
        log.info("POST Response: /cqrs/cards/${id}/transfer")
        val (login, password) = userEntity.run { login to password }
        val command = CardTransferCommand(transferBody.money, id, login)
        return sendToUrl("http://localhost:8080/cardsCommands/transfer", command.toMap(), login=login, password=password)
    }

    @ApiOperation(value = "Card receipt")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/cards/{id}/receipt")
    suspend fun postCardsReceiptById(@PathVariable id: Long, @RequestBody transferBody: TransferBody): String {
        log.info("POST Response: /cqrs/cards/${id}/receipt")
        val (login, password) = userEntity.run { login to password }
        val command = CardReceiptCommand(transferBody.money, id, login)
        return sendToUrl("http://localhost:8080/cardsCommands/receipt", command.toMap(), login=login, password=password)
    }

    @ApiOperation(value = "Transfer between own cards")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/cards/{id}/localTransfer")
    suspend fun postCardsLocalTransferById(@PathVariable id: Long, @RequestBody transferToBody: TransferToBody): String {
        log.info("POST Response: /cqrs/cards/${id}/localTransfer")
        val (login, password) = userEntity.run { login to password }
        val command = CardLocalTransferCommand(transferToBody.money, id, transferToBody.idTo, login)
        return sendToUrl("http://localhost:8080/cardsCommands/localTransfer", command.toMap(), login=login, password=password)
    }

    @ApiOperation(value = "Delete card")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @DeleteMapping("/cqrs/cards/{id}")
    suspend fun deleteCardsById(@PathVariable id: Long): String {
        log.info("DELETE Response: /cqrs/cards/${id}")
        val (login, password) = userEntity.run { login to password }
        val command = CardDeleteCommand(id, login)
        return sendToUrl("http://localhost:8080/cardsCommands/delete", command.toMap(), login=login, password=password)
    }
}
