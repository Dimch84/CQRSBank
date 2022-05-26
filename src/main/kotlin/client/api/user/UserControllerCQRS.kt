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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.*
import server.commands.user.UserUpdateProfileCommand
import server.db.mongo.UserRepository
import server.db.mongo.entities.UserEntity
import server.queries.user.UserAccountsQuery
import server.queries.user.UserQuery

@RestController
@Api(description = "for profile user")
@CrossOrigin(origins = ["http://localhost:8081/"])
class UserControllerCQRS @Autowired constructor(private val userRepository: UserRepository) {
    companion object {
        private val GSON = Gson()
    }

    private val log: Logger = LoggerFactory.getLogger(UserControllerCQRS::class.java)
    private val userLogin: String by lazy { SecurityContextHolder.getContext().authentication.name }
    private val userEntity: UserEntity
        get() = userRepository.findByLogin(userLogin) ?: throw Exception("wrong login")

    @ApiOperation(value = "Return user profile")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/user")
    suspend fun getUser(): String {     // UserProfileBody
        log.info("GET Response: /cqrs/user")
        val (login, password) = userEntity.run { login to password }
        val query = UserQuery(login)
        val userJson = sendToUrl("http://localhost:8080/userCommands/byLogin", query.toMap(), login=login, password=password)
        return try {
            GSON.fromJson(userJson, object : TypeToken<UserProfileBody>() {}.type)
        } catch (ex: Exception) {
            userJson
        }
    }

    @ApiOperation(value = "Return user accounts")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/user/accounts")
    suspend fun getUserAccounts(): List<AccountBody> {
        log.info("GET Response: /cqrs/user/accounts")
        val (login, password) = userEntity.run { login to password }
        val query = UserAccountsQuery(login)
        val accountsJson = sendToUrl("http://localhost:8080/userCommands/byLoginAccounts", query.toMap(), login=login, password=password)
        return try {
            GSON.fromJson(accountsJson, object : TypeToken<List<AccountBody>>() {}.type)
        } catch (ex: Exception) {
            listOf()
        }
    }

    @ApiOperation(value = "Update user profile")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/cqrs/user")
    suspend fun postUser(@RequestBody userProfileBody: UserProfileBody): String {   // ignore userProfileBody.login
        log.info("POST Response: /cqrs/user")
        val (login, password) = userEntity.run { login to password }
        val command = UserUpdateProfileCommand(userProfileBody.name, login, userProfileBody.phone,
            userProfileBody.email)
        return sendToUrl("http://localhost:8080/userCommands/updateProfile", command.toMap(), login=login, password=password)
    }
}
