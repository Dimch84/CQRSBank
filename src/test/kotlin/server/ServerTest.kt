package server

import config.Application
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import server.service.CommandService
import server.commands.card.CardCreateCommand
import server.commands.card.CardUpdateNameCommand
import server.db.mongo.CardRepository
import server.db.postgresql.CardEventsRepository
import server.events.card.CardCreateEvent
import server.db.postgresql.entities.CardEvents


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [Application::class])
class ServerTest @Autowired constructor(private val cardEventsRepository: CardEventsRepository,
                                        private val cardRepository: CardRepository,
                                        private val service: CommandService) {
    @BeforeEach
    fun clearDB() {
        cardRepository.deleteAll()
        cardEventsRepository.deleteAll()
    }

    @Test
    fun someTest() {
        cardEventsRepository.save(CardEvents().also { it.update(CardCreateEvent("name", "type", 1)) })
        val result = cardEventsRepository.findAll().first()
        result.initEvents()
        assert(result.update().run { mapOf("name" to name, "type" to type, "id" to account_id) } ==
                mapOf("name" to "name", "type" to "type", "id" to 1))
    }

    @Test
    fun some2Test() {
        val command = CardCreateCommand("name", "type", 1)
        println("command1 reply ${service.send(command)}")
        assert(cardRepository.findAll().first().run { mapOf("name" to name, "type" to type, "id" to accountId) } ==
                mapOf("name" to "name", "type" to "type", "id" to 1))
    }

    @Test
    fun some3Test() {
        val command1 = CardCreateCommand("name", "type", 1)
        val id = service.send(command1) as Long
        println("command1 reply: $id")
        val command2 = CardUpdateNameCommand("name2", id)
        println("command2 reply: ${service.send(command2)}")
        assert(cardRepository.findAll().first().run { mapOf("name" to name, "type" to type, "id" to accountId) } ==
                mapOf("name" to "name2", "type" to "type", "id" to 1))
    }
}
