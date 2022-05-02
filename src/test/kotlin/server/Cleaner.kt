package server

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
import server.db.postgresql.TempEventsRepository
import server.db.postgresql.UserEventsRepository

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [Application::class])
class Cleaner  @Autowired constructor(private val tempEventsRepository: TempEventsRepository,
                                      private val cardEventsRepository: CardEventsRepository,
                                      private val userEventsRepository: UserEventsRepository,
                                      private val accountEventsRepository: AccountEventsRepository,
                                      private val cardRepository: CardRepository,
                                      private val userRepository: UserRepository,
                                      private val accountRepository: AccountRepository) {
    @BeforeEach
    fun clearDB() {
        tempEventsRepository.deleteAll()
        cardRepository.deleteAll()
        userEventsRepository.deleteAll()
        accountEventsRepository.deleteAll()
        cardEventsRepository.deleteAll()
        userRepository.deleteAll()
        accountRepository.deleteAll()
    }

    @Test
    fun nothing() {}
}
