package client.api.accounts

import client.api.abstractions.AccountBody
import client.api.abstractions.CardBody
import client.api.abstractions.PlanBody
import client.api.requests.sendToUrl
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import server.commands.account.AccountCreateCommand
import server.commands.account.AccountDeleteCommand
import server.commands.account.AccountUpdatePlanCommand
import server.queries.account.AccountCardsQuery
import server.queries.account.AccountMoneyQuery
import server.queries.account.AccountQuery

@RestController
@Api(description = "accounts operations")
class AccountsControllerCQRS {
    companion object {
        private val GSON = Gson()
    }
    private val log: Logger = LoggerFactory.getLogger(AccountsControllerCQRS::class.java)

    @ApiOperation(value = "Return account")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/accounts/{id}")
    suspend fun getAccountsById(@PathVariable id: Long): String {   // AccountBody
        log.info("GET Response: /cqrs/accounts/${id}")
        val query = AccountQuery(id)
        val accountJson = sendToUrl("http://localhost:8080/accountsCommands/byId", query.toMap())
        return try {
            val account: AccountBody = GSON.fromJson(accountJson, object : TypeToken<AccountBody>() {}.type)
            account.toString()
        } catch (ex: Exception) {
            accountJson
        }
    }

    @ApiOperation(value = "Return account money")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/accounts/{id}/money")
    suspend fun getAccountsMoneyById(@PathVariable id: Long): String {
        log.info("GET Response: /cqrs/accounts/${id}/money")
        val query = AccountMoneyQuery(id)
        return sendToUrl("http://localhost:8080/accountsCommands/byIdMoney", query.toMap())
    }

    @ApiOperation(value = "Return account cards")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/accounts/{id}/cards")
    suspend fun getAccountsCardsById(@PathVariable id: Long): String {  // List<CardBody>
        log.info("GET Response: /cqrs/accounts/${id}/cards")
        val query = AccountCardsQuery(id)
        val cardsJson = sendToUrl("http://localhost:8080/accountsCommands/byIdCards", query.toMap())
        return try {
            val cards: List<CardBody> = GSON.fromJson(cardsJson, object : TypeToken<List<CardBody>>() {}.type)
            cards.toString()
        } catch (ex: Exception) {
            cardsJson
        }
    }

    //

    @ApiOperation(value = "Add account")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/accounts")
    suspend fun postAccounts(@RequestBody accountBody: AccountBody): String {
        log.info("POST Response: /cqrs/accounts")
        val command = AccountCreateCommand(accountBody.money, accountBody.userId, accountBody.planId)
        return sendToUrl("http://localhost:8080/accountsCommands/create", command.toMap())
    }

    @ApiOperation(value = "Update account plan")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/accounts/{id}")
    suspend fun postAccountsById(@PathVariable id: Long, @RequestBody planBody: PlanBody): String {
        log.info("POST Response: /cqrs/accounts/${id}")
        val command = AccountUpdatePlanCommand(planBody.planId, id)
        return sendToUrl("http://localhost:8080/accountsCommands/updatePlan", command.toMap())
    }

    @ApiOperation(value = "Delete account")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @DeleteMapping("/cqrs/accounts/{id}")
    suspend fun deleteAccountsById(@PathVariable id: Long): String {
        log.info("DELETE Response: /cqrs/accounts/${id}")
        val command = AccountDeleteCommand(id)
        return sendToUrl("http://localhost:8080/accountsCommands/delete", command.toMap())
    }
}
