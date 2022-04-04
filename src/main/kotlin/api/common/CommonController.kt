package api.common

import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiResponse
import io.swagger.annotations.ApiResponses
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.bind.annotation.*

@RestController
@Api(description = "common debug operations")
class CommonController {
    private val log: Logger = LoggerFactory.getLogger(CommonController::class.java)

    @ApiOperation(value = "Return all register users")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/common/register/all")
    suspend fun getCommonRegisterAll(): String {
        log.info("GET Response: /common/register/all")
        return "Ok"
    }

    @ApiOperation(value = "Return all accounts for user")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/common/accounts/all")
    suspend fun getCommonAccountsAll(): String {
        log.info("GET Response: /common/accounts/all")
        return "Ok"
    }

    @ApiOperation(value = "Return all cards for user")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/common/cards/all")
    suspend fun getCommonCardsAll(): String {
        log.info("GET Response: /common/cards/all")
        return "Ok"
    }
}
