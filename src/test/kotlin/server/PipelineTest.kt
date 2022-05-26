package server

import client.api.abstractions.CardBody
import client.api.requests.RequestType
import client.api.requests.sendToUrl
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import config.Application
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import server.db.mongo.AccountRepository
import server.db.mongo.CardRepository
import server.db.mongo.UserRepository
import server.db.postgresql.AccountEventsRepository
import server.db.postgresql.CardEventsRepository
import server.db.postgresql.UserEventsRepository
import java.lang.Thread.sleep

// start it with main application
@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [Application::class])
class PipelineTest @Autowired constructor(private val cardEventsRepository: CardEventsRepository,
                                          private val accountEventsRepository: AccountEventsRepository,
                                          private val userEventsRepository: UserEventsRepository,
                                          private val cardRepository: CardRepository,
                                          private val accountRepository: AccountRepository,
                                          private val userRepository: UserRepository) {
    companion object {
        private val GSON = Gson()
    }

    @BeforeEach
    fun clearDB() {
        cardEventsRepository.deleteAll()
        accountEventsRepository.deleteAll()
        userEventsRepository.deleteAll()
        cardRepository.deleteAll()
        accountRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun someTest() {
        val login = "login"
        val password = "pass"
        val userId = sendToUrl("http://localhost:8080/cqrs/register/$login", mapOf("name" to "name", "password" to password)).toLong()
        val accountIdCur = sendToUrl("http://localhost:8080/cqrs/accounts",
            mapOf("money" to 0L, "userId" to userId, "planId" to 1L), login=login, password=password).toLong()
        val id = sendToUrl("http://localhost:8080/cqrs/cards",
            mapOf("name" to "name", "type" to "type", "accountId" to accountIdCur), login=login, password=password).toLong()
        println("command1 reply: $id")
        val card: CardBody = sendToUrl("http://localhost:8080/cqrs/cards/${id}", mapOf(),
            RequestType.GET, login=login, password=password).run {
            println(this)
            GSON.fromJson(this, object : TypeToken<CardBody>() {}.type)
        }
        println("query reply: $card")
        assert(card.toMap() == mapOf("name" to "name", "type" to "type", "accountId" to accountIdCur))
        val command2Reply = sendToUrl("http://localhost:8080/cqrs/cards/${id}/updateName",
            mapOf("name" to "name2"), login=login, password=password)
        println("command2 reply: $command2Reply")
        assert(cardRepository.findAll().first().run { mapOf("name" to name, "type" to type, "accountId" to accountId) } ==
                mapOf("name" to "name2", "type" to "type", "accountId" to accountIdCur))
    }
}
