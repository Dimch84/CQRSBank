package client.api.accounts

import client.api.abstractions.AccountBody
import client.api.abstractions.CardBody
import client.api.abstractions.PlanBody
import client.domain.Account
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@Api(description = "accounts operations")
class AccountsController {
    private val log: Logger = LoggerFactory.getLogger(AccountsController::class.java)

    @ApiOperation(value = "Return account")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/accounts/{id}")
    suspend fun getAccountsById(@PathVariable id: Int): AccountBody {
        log.info("GET Response: /accounts/${id}")
        return Account(id).data
    }

    @ApiOperation(value = "Return account money")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/accounts/{id}/money")
    suspend fun getAccountsMoneyById(@PathVariable id: Int): Long {
        log.info("GET Response: /accounts/${id}/money")
        return Account(id).data.money
    }

    @ApiOperation(value = "Return account cards")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/accounts/{id}/cards")
    suspend fun getAccountsCardsById(@PathVariable id: Int): List<CardBody> {
        log.info("GET Response: /accounts/${id}/cards")
        return Account(id).cards
    }

    @ApiOperation(value = "Add account")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/accounts")
    suspend fun postAccounts(@RequestBody accountBody: AccountBody): Int {
        log.info("POST Response: /accounts")
        return Account().post(accountBody)
    }

    @ApiOperation(value = "Update account plan")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/accounts/{id}")
    suspend fun postAccountsById(@PathVariable id: Int, @RequestBody planBody: PlanBody): Int {
        log.info("POST Response: /accounts/${id}")
        return Account(id).updatePlan(planBody.planId)
    }

    @ApiOperation(value = "Delete account")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @DeleteMapping("/accounts/{id}")
    suspend fun deleteAccountsById(@PathVariable id: Int) {
        log.info("DELETE Response: /accounts/${id}")
        return Account(id).delete()
    }
}
