package server

import client.api.requests.sendToUrl
import config.Application
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import server.db.mongo.CardRepository
import server.db.postgresql.CardEventsRepository

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [Application::class])
class PipelineTest @Autowired constructor(private val cardEventsRepository: CardEventsRepository,
                                        private val cardRepository: CardRepository) {
    @BeforeEach
    fun clearDB() {
        cardRepository.deleteAll()
        cardEventsRepository.deleteAll()
    }

    @Test
    fun someTest() {
        val id = sendToUrl("http://localhost:8080/cqrs/cards", mapOf("name" to "name2", "type" to "type", "account_id" to 1)).toLong()
        println("command1 reply: $id")
        val command2Reply = sendToUrl("http://localhost:8080/cqrs/cards/2/updateName", mapOf("name" to "name2"))
        println("command2 reply: $command2Reply")
        assert(cardRepository.findAll().first().run { mapOf("name" to name, "type" to type, "id" to account_id) } ==
                mapOf("name" to "name2", "type" to "type", "id" to 1))
    }
}
