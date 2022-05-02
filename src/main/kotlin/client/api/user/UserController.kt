package client.api.user

import client.api.abstractions.AccountBody
import client.api.abstractions.UserProfileBody
import client.domain.User
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
@Api(description = "for profile user")
class UserController {
    private val log: Logger = LoggerFactory.getLogger(UserController::class.java)

    @ApiOperation(value = "Return user profile")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/user")
    suspend fun getUser(): UserProfileBody {
        log.info("GET Response: /user")
        return User().data
    }

    @ApiOperation(value = "Return user accounts")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/user/accounts")
    suspend fun getUserAccounts(): List<AccountBody> {
        log.info("GET Response: /user/accounts")
        return User().accounts
    }

    @ApiOperation(value = "Update user profile")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @PostMapping("/user")
    suspend fun postUser(@RequestBody userProfileBody: UserProfileBody) {
        log.info("POST Response: /user")
        return User().post(userProfileBody)
    }
}
