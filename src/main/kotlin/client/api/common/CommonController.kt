package client.api.common

import client.api.abstractions.AccountBody
import client.api.abstractions.CardBody
import client.api.abstractions.UserProfileBody
import client.domain.Account
import client.domain.Card
import client.domain.User
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@CrossOrigin(origins = ["http://localhost:8081"])
@Api(description = "common debug operations")
class CommonController {
    private val log: Logger = LoggerFactory.getLogger(CommonController::class.java)

    @ApiOperation(value = "Return all register users")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/common/register/all")
    suspend fun getCommonRegisterAll(): List<UserProfileBody> {
        log.info("GET Response: /common/register/all")
        return User().getAll()
    }

    @ApiOperation(value = "Return all accounts for user")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/common/accounts/all")
    suspend fun getCommonAccountsAll(): List<AccountBody> {
        log.info("GET Response: /common/accounts/all")
        return Account().getAll()
    }

    @ApiOperation(value = "Return all cards for user")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/common/cards/all")
    suspend fun getCommonCardsAll(): List<CardBody> {
        log.info("GET Response: /common/cards/all")
        return Card().getAll()
    }
}
