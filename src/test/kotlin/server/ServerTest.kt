package server

import config.Application
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import server.commands.account.AccountCreateCommand
import server.commands.card.CardCreateCommand
import server.commands.card.CardUpdateNameCommand
import server.commands.user.UserCreateCommand
import server.db.mongo.AccountRepository
import server.db.mongo.CardRepository
import server.db.mongo.UserRepository
import server.db.postgresql.AccountEventsRepository
import server.db.postgresql.CardEventsRepository
import server.db.postgresql.UserEventsRepository
import server.db.postgresql.entities.CardEvents
import server.events.card.CardCreateEvent
import server.service.CommandService

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [Application::class])
class ServerTest @Autowired constructor(private val cardEventsRepository: CardEventsRepository,
                                        private val accountEventsRepository: AccountEventsRepository,
                                        private val userEventsRepository: UserEventsRepository,
                                        private val cardRepository: CardRepository,
                                        private val accountRepository: AccountRepository,
                                        private val userRepository: UserRepository,
                                        private val service: CommandService) {
    @BeforeEach
    fun clearDB() {
        cardEventsRepository.deleteAll()
        accountEventsRepository.deleteAll()
        userEventsRepository.deleteAll()
        cardRepository.deleteAll()
        accountRepository.deleteAll()
        userRepository.deleteAll()
        val userId = service.send(UserCreateCommand("name", login, "password")) as Long
        accountIdCur = service.send(AccountCreateCommand(0, userId, 1)) as Long
    }

    private var accountIdCur: Long = 0
    private val login = "login"

    @Test
    fun some1Test() {
        cardEventsRepository.save(CardEvents().also { it.update(CardCreateEvent("name", "type", accountIdCur, login, "0000 0000 0000 0000", "03/30", "000")) })
        val result = cardEventsRepository.findAll().first()
        result.initEvents()
        assert(result.update().run { mapOf("name" to name, "type" to type, "accountId" to accountId, "cardNumber" to cardNumber, "expDate" to expDate, "cvv" to cvv) } ==
                mapOf("name" to "name", "type" to "type", "accountId" to accountIdCur, "cardNumber" to "0000 0000 0000 0000", "expDate" to "03/30", "cvv" to "000"))
    }

    @Test
    fun some2Test() {
        val command = CardCreateCommand("name", "type", accountIdCur, login)
        println("command1 reply ${service.send(command)}")
        assert(cardRepository.findAll().first().run { mapOf("name" to name, "type" to type, "accountId" to accountId, "cardNumber" to cardNumber, "expDate" to expDate, "cvv" to cvv) } ==
                mapOf("name" to "name", "type" to "type", "accountId" to accountIdCur, "cardNumber" to "0000 0000 0000 0000", "expDate" to "03/30", "cvv" to "000"))
    }

    @Test
    fun some3Test() {
        val command1 = CardCreateCommand("name", "type", accountIdCur, login)
        val id = service.send(command1) as Long
        println("command1 reply: $id")
        val command2 = CardUpdateNameCommand("name2", id, login)
        println("command2 reply: ${service.send(command2)}")
        assert(cardRepository.findAll().first().run { mapOf("name" to name, "type" to type, "accountId" to accountId, "cardNumber" to cardNumber, "expDate" to expDate, "cvv" to cvv) } ==
                mapOf("name" to "name2", "type" to "type", "accountId" to accountIdCur, "cardNumber" to "0000 0000 0000 0000", "expDate" to "03/30", "cvv" to "000"))
    }
}
