package client.api.register

import client.api.abstractions.RegisterBody
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
import org.springframework.web.bind.annotation.*
import server.commands.user.UserCreateCommand
import server.commands.user.UserDeleteCommand
import server.db.mongo.UserRepository
import server.db.mongo.entities.UserEntity
import server.queries.user.UserQuery

@RestController
@Api(description = "for register users")
class RegisterControllerCQRS @Autowired constructor(private val userRepository: UserRepository) {
    companion object {
        private val GSON = Gson()
    }

    private val log: Logger = LoggerFactory.getLogger(RegisterControllerCQRS::class.java)
    private val userLogin: String by lazy { SecurityContextHolder.getContext().authentication.name }
    private val userEntity: UserEntity
        get() = userRepository.findByLogin(userLogin) ?: throw Exception("wrong login")

    @ApiOperation(value = "Show user by login")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/register/{login}")
    suspend fun getRegisterLogin(@PathVariable login: String): String {     // UserProfileBody
        if (userLogin != "admin" && userLogin != login)
            return "No access"
        log.info("GET Response: /cqrs/register/${login}")
        val query = UserQuery(login)
        val userJson = sendToUrl("http://localhost:8080/userCommands/byLogin", query.toMap(), login=login, password=userEntity.password)
        return try {
            val user: UserProfileBody = GSON.fromJson(userJson, object : TypeToken<UserProfileBody>() {}.type)
            user.toString()
        } catch (ex: Exception) {
            userJson
        }
    }

    @ApiOperation(value = "Register user by login and password")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Ok"),
        ApiResponse(code = 409, message = "Login already exist")])
    @PostMapping("/cqrs/register/{login}")
    suspend fun postRegisterLogin(@PathVariable login: String, @RequestBody registerBody: RegisterBody): String {
        log.info("POST Response: /cqrs/register/${login}")
        val command = UserCreateCommand(registerBody.name, login, registerBody.password)
        return sendToUrl("http://localhost:8080/userCommands/create", command.toMap())
    }

    @ApiOperation(value = "Delete user by login")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Ok"),
        ApiResponse(code = 409, message = "Login not exist")])
    @DeleteMapping("/cqrs/register/{login}")
    suspend fun deleteRegisterLogin(@PathVariable login: String): String {
        if (userLogin != "admin" && userLogin != login)
            return "No access"
        log.info("DELETE Response: /cqrs/register/${login}")
        val command = UserDeleteCommand(login)
        return sendToUrl("http://localhost:8080/userCommands/delete", command.toMap(), login=login, password=userEntity.password)
    }
}
