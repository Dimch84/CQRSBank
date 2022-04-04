package api.accounts

import api.abstractions.AccountBody
import api.abstractions.PlanBody
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
    suspend fun getAccountsById(@PathVariable id: String): String {
        log.info("GET Response: /accounts/${id}")
        return "Ok"
    }

    @ApiOperation(value = "Return account money")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/accounts/{id}/money")
    suspend fun getAccountsMoneyById(@PathVariable id: String): String {
        log.info("GET Response: /accounts/${id}/money")
        return "Ok"
    }

    @ApiOperation(value = "Return account cards")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/accounts/{id}/cards")
    suspend fun getAccountsCardsById(@PathVariable id: String): String {
        log.info("GET Response: /accounts/${id}/cards")
        return "Ok"
    }

    @ApiOperation(value = "Add account")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/accounts")
    suspend fun postAccounts(@RequestBody accountBody: AccountBody): String {
        log.info("POST Response: /accounts")
        return "Ok"
    }

    @ApiOperation(value = "Update account plan")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/accounts/{id}")
    suspend fun postAccountsById(@PathVariable id: String, @RequestBody planBody: PlanBody): String {
        log.info("POST Response: /accounts/${id}")
        return "Ok"
    }

    @ApiOperation(value = "Delete account")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @DeleteMapping("/accounts/{id}")
    suspend fun deleteAccountsById(@PathVariable id: String): String {
        log.info("DELETE Response: /accounts/${id}")
        return "Ok"
    }
}
