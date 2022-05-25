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
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import server.db.mongo.UserRepository
import server.db.mongo.entities.UserEntity
import server.queries.user.UserAllQuery


@RestController
@Api(description = "common debug operations")
class CommonControllerCQRS @Autowired constructor(private val userRepository: UserRepository) {
    companion object {
        private val GSON = Gson()
    }

    private val log: Logger = LoggerFactory.getLogger(CommonControllerCQRS::class.java)
    private val userLogin: String by lazy { SecurityContextHolder.getContext().authentication.name }
    private val userEntity: UserEntity
        get() = userRepository.findByLogin(userLogin) ?: throw Exception("wrong login")

    @ApiOperation(value = "Return all register users")
    @ApiResponses(value = [ApiResponse(code = 200, message = "Ok")])
    @GetMapping("/cqrs/common/register/all")
    suspend fun getCommonRegisterAll(): List<UserProfileBody> {
        log.info("GET Response: /cqrs/common/register/all")
        val (login, password) = userEntity.run { login to password }
        val query = UserAllQuery()
        val usersJson = sendToUrl("http://localhost:8080/userCommands/all", query.toMap(), login=login, password=password)
        return try {
            GSON.fromJson(usersJson, object : TypeToken<List<UserProfileBody>>() {}.type)
        } catch (ex: Exception) {
            listOf()
        }
    }
}
