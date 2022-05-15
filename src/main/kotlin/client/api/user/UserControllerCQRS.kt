package client.api.user

import client.api.abstractions.AccountBody
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
import org.springframework.web.bind.annotation.*
import server.commands.user.UserUpdateProfileCommand
import server.queries.user.UserAccountsQuery
import server.queries.user.UserQuery

@RestController
@Api(description = "for profile user")
@CrossOrigin(origins = ["http://localhost:8081", "http://172.20.10.13:8081"])
class UserControllerCQRS {
    companion object {
        private val GSON = Gson()
    }

    private val log: Logger = LoggerFactory.getLogger(UserControllerCQRS::class.java)
    private val userLogin = "admin"     //: String by lazy { SecurityContextHolder.getContext().authentication.name }

    @ApiOperation(value = "Return user profile")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/user/{login}")
    suspend fun getUser(@PathVariable login: String): String {     // UserProfileBody
        log.info("GET Response: /cqrs/user/${login}")
        val query = UserQuery(login)
        val userJson = sendToUrl("http://localhost:8080/userCommands/byLogin", query.toMap())
        return try {
            val user: UserProfileBody = GSON.fromJson(userJson, object : TypeToken<UserProfileBody>() {}.type)
            user.toString()
        } catch (ex: Exception) {
            userJson
        }
    }

    @ApiOperation(value = "Return user accounts")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/user/{login}/accounts")
    suspend fun getUserAccounts(@PathVariable login: String): List<AccountBody> {
        log.info("GET Response: /cqrs/user/${login}/accounts")
        val query = UserAccountsQuery(login)
        val accountsJson = sendToUrl("http://localhost:8080/userCommands/byLoginAccounts", query.toMap())
        return try {
            GSON.fromJson(accountsJson, object : TypeToken<List<AccountBody>>() {}.type)
        } catch (ex: Exception) {
            listOf()
        }
    }

    @ApiOperation(value = "Update user profile")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/user/{login}")
    suspend fun postUser(@PathVariable login: String, @RequestBody userProfileBody: UserProfileBody): String {   // ignore userProfileBody.login
        log.info("POST Response: /cqrs/user/${login}")
        val command = UserUpdateProfileCommand(userProfileBody.name, login, userProfileBody.phone,
            userProfileBody.email)
        return sendToUrl("http://localhost:8080/userCommands/updateProfile", command.toMap())
    }
}
