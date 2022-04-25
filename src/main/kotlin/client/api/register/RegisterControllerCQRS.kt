package client.api.register

import client.api.abstractions.RegisterBody
import client.api.abstractions.UserProfileBody
import client.api.requests.sendToUrl
import client.domain.Register
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*
import server.commands.user.UserCreateCommand
import server.queries.card.CardHistoryQuery

@RestController
@Api(description = "for register users")
class RegisterControllerCQRS {
    private val log: Logger = LoggerFactory.getLogger(RegisterControllerCQRS::class.java)

//    @ApiOperation(value = "Show user by login")
//    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
//    @GetMapping("/cqrs/register/{login}")
//    suspend fun getRegisterLogin(@PathVariable login: String): UserProfileBody {
//        log.info("GET Response: /cqrs/register/${login}")
//        return Register(login).data
//    }

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

//    @ApiOperation(value = "Delete user by login")
//    @ApiResponses(value = [
//        ApiResponse(code = 200, message = "Ok"),
//        ApiResponse(code = 409, message = "Login not exist")])
//    @DeleteMapping("/cqrs/register/{login}")
//    suspend fun deleteRegisterLogin(@PathVariable login: String) {
//        log.info("DELETE Response: /cqrs/register/${login}")
//        return Register(login).delete()
//    }
}
