package api.cards

import api.abstractions.CardBody
import api.abstractions.PaymentBody
import api.abstractions.TransferBody
import domain.Card
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@Api(description = "cards operations")
class CardsController {
    private val log: Logger = LoggerFactory.getLogger(CardsController::class.java)

    @ApiOperation(value = "Return card")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cards/{id}")
    suspend fun getCardsById(@PathVariable id: String): CardBody {
        log.info("GET Response: /cards/${id}")
        return Card(id).data
    }

    @ApiOperation(value = "Return card history")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cards/{id}/history")
    suspend fun getCardsHistoryById(@PathVariable id: String): Any {
        log.info("GET Response: /cards/${id}/history")
        return Card(id).history
    }

    @ApiOperation(value = "Add card")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cards")
    suspend fun postCards(@RequestBody cardBody: CardBody): String {
        log.info("POST Response: /cards")
        return Card().post(cardBody)
    }

    @ApiOperation(value = "Card payment")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cards/{id}/payment")
    suspend fun postCardsPaymentById(@PathVariable id: String, @RequestBody paymentBody: PaymentBody): Any {
        log.info("POST Response: /cards/${id}/payment")
        return Card(id).pay(paymentBody)
    }

    @ApiOperation(value = "Card transfer")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cards/{id}/transfer")
    suspend fun postCardsTransferById(@PathVariable id: String, @RequestBody transferBody: TransferBody): Any {
        log.info("POST Response: /cards/${id}/transfer")
        return Card(id).transfer(transferBody)
    }

    @ApiOperation(value = "Delete card")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @DeleteMapping("/cards/{id}")
    suspend fun deleteCardsById(@PathVariable id: String): String {
        log.info("DELETE Response: /cards/${id}")
        return Card(id).delete()
    }
}
