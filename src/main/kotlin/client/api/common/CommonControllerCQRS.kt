package client.api.common

import client.api.abstractions.AccountBody
import client.api.abstractions.CardBody
import client.api.abstractions.UserProfileBody
import client.api.requests.sendToUrl
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import server.queries.account.AccountAllQuery
import server.queries.card.CardAllQuery
import server.queries.user.UserAllQuery

@RestController
@Api(description = "common debug operations")
class CommonControllerCQRS {
    companion object {
        private val GSON = Gson()
    }

    private val log: Logger = LoggerFactory.getLogger(CommonControllerCQRS::class.java)

    @ApiOperation(value = "Return all register users")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/common/register/all")
    suspend fun getCommonRegisterAll(): List<UserProfileBody> {
        log.info("GET Response: /cqrs/common/register/all")
        val query = UserAllQuery()
        val usersJson = sendToUrl("http://localhost:8080/userCommands/all", query.toMap())
        return try {
            GSON.fromJson(usersJson, object : TypeToken<List<UserProfileBody>>() {}.type)
        } catch (ex: Exception) {
            listOf()
        }
    }

    @ApiOperation(value = "Return all accounts for user")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/common/accounts/all")
    suspend fun getCommonAccountsAll(): List<AccountBody> {
        log.info("GET Response: /cqrs/common/accounts/all")
        val query = AccountAllQuery()
        val accountsJson = sendToUrl("http://localhost:8080/accountsCommands/all", query.toMap())
        return try {
            GSON.fromJson(accountsJson, object : TypeToken<List<AccountBody>>() {}.type)
        } catch (ex: Exception) {
            listOf()
        }
    }

    @ApiOperation(value = "Return all cards for user")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/common/cards/all")
    suspend fun getCommonCardsAll(): List<CardBody> {
        log.info("GET Response: /cqrs/common/cards/all")
        val query = CardAllQuery()
        val cardsJson = sendToUrl("http://localhost:8080/cardsCommands/all", query.toMap())
        return try {
            GSON.fromJson(cardsJson, object : TypeToken<List<CardBody>>() {}.type)
        } catch (ex: Exception) {
            listOf()
        }
    }
}
