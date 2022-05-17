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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import server.commands.account.AccountCreateCommand
import server.commands.account.AccountDeleteCommand
import server.commands.account.AccountUpdatePlanCommand
import server.db.mongo.UserRepository
import server.db.mongo.entities.UserEntity
import server.queries.account.AccountAllQuery
import server.queries.account.AccountCardsQuery
import server.queries.account.AccountMoneyQuery
import server.queries.account.AccountQuery

@RestController
@Api(description = "accounts operations")
@CrossOrigin(origins = ["http://localhost:8081", "http://172.20.10.13:8081"])
class AccountsControllerCQRS @Autowired constructor(private val userRepository: UserRepository) {
    companion object {
        private val GSON = Gson()
    }
    private val log: Logger = LoggerFactory.getLogger(AccountsControllerCQRS::class.java)
    private val userLogin: String by lazy { SecurityContextHolder.getContext().authentication.name }
    private val userEntity: UserEntity
        get() = userRepository.findByLogin(userLogin) ?: throw Exception("wrong login")

    @ApiOperation(value = "Return account")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/accounts/{id}")
    suspend fun getAccountsById(@PathVariable id: Long): String {   // AccountBody
        log.info("GET Response: /cqrs/accounts/${id}")
        val query = AccountQuery(id, userLogin)
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
        val query = AccountMoneyQuery(id, userLogin)
        return sendToUrl("http://localhost:8080/accountsCommands/byIdMoney", query.toMap())
    }

    @ApiOperation(value = "Return account cards")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/accounts/{id}/cards")
    suspend fun getAccountsCardsById(@PathVariable id: Long): String {  // List<CardBody>
        log.info("GET Response: /cqrs/accounts/${id}/cards")
        val query = AccountCardsQuery(id, userLogin)
        val cardsJson = sendToUrl("http://localhost:8080/accountsCommands/byIdCards", query.toMap())
        return try {
            val cards: List<CardBody> = GSON.fromJson(cardsJson, object : TypeToken<List<CardBody>>() {}.type)
            cards.toString()
        } catch (ex: Exception) {
            cardsJson
        }
    }

    @ApiOperation(value = "Return all user accounts")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/accounts/all")
    suspend fun getAccountsAll(): List<AccountBody> {
        log.info("GET Response: /cqrs/accounts/all")
        val query = AccountAllQuery(userLogin)
        val accountsJson = sendToUrl("http://localhost:8080/accountsCommands/all", query.toMap())
        return try {
            GSON.fromJson(accountsJson, object : TypeToken<List<AccountBody>>() {}.type)
        } catch (ex: Exception) {
            listOf()
        }
    }

    //

    @ApiOperation(value = "Add account")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/accounts")
    suspend fun postAccounts(@RequestBody accountBody: AccountBody): String {
        log.info("POST Response: /cqrs/accounts")
        if (userEntity.id != accountBody.userId)
            return "wrong usr id"
        if (accountBody.money < 0)
            return "money must be at least 0"
        val command = AccountCreateCommand(accountBody.name, accountBody.money, accountBody.userId, accountBody.planId)
        return sendToUrl("http://localhost:8080/accountsCommands/create", command.toMap())
    }

    @ApiOperation(value = "Update account plan")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/accounts/{id}")
    suspend fun postAccountsById(@PathVariable id: Long, @RequestBody planBody: PlanBody): String {
        log.info("POST Response: /cqrs/accounts/${id}")
        val command = AccountUpdatePlanCommand(planBody.planId, id, userLogin)
        return sendToUrl("http://localhost:8080/accountsCommands/updatePlan", command.toMap())
    }

    @ApiOperation(value = "Delete account")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @DeleteMapping("/cqrs/accounts/{id}")
    suspend fun deleteAccountsById(@PathVariable id: Long): String {
        log.info("DELETE Response: /cqrs/accounts/${id}")
        val command = AccountDeleteCommand(id, userLogin)
        return sendToUrl("http://localhost:8080/accountsCommands/delete", command.toMap())
    }
}
