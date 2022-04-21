package client.api.register

import client.api.abstractions.RegisterBody
import client.api.abstractions.UserProfileBody
import client.domain.Register
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@Api(description = "for register users")
class RegisterController {
    private val log: Logger = LoggerFactory.getLogger(RegisterController::class.java)

    @ApiOperation(value = "Show user by login")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/register/{login}")
    suspend fun getRegisterLogin(@PathVariable login: String): UserProfileBody {
        log.info("GET Response: /register/${login}")
        return Register(login).data
    }

    @ApiOperation(value = "Register user by login and password")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Ok"),
        ApiResponse(code = 409, message = "Login already exist")])
    @PostMapping("/register/{login}")
    suspend fun postRegisterLogin(@PathVariable login: String, @RequestBody registerBody: RegisterBody): Int {
        log.info("POST Response: /register/${login}")
        return Register(login).post(registerBody)
    }

    @ApiOperation(value = "Delete user by login")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Ok"),
        ApiResponse(code = 409, message = "Login not exist")])
    @DeleteMapping("/register/{login}")
    suspend fun deleteRegisterLogin(@PathVariable login: String) {
        log.info("DELETE Response: /register/${login}")
        return Register(login).delete()
    }
}
