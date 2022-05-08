package client.api.common

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
import server.queries.user.UserAllQuery


@RestController
@Api(description = "common debug operations")
@CrossOrigin(origins = ["http://localhost:8081", "http://172.20.10.13:8081"])
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
}
