package server

import config.Application
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import server.db.elasticsearch.UserInfoRepository
import server.db.mongo.AccountRepository
import server.db.mongo.CardRepository
import server.db.mongo.UserRepository
import server.db.postgresql.*

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [Application::class])
class Cleaner  @Autowired constructor(private val tempEventsRepository: TempEventsRepository,
                                      private val cardEventsRepository: CardEventsRepository,
                                      private val userEventsRepository: UserEventsRepository,
                                      private val accountEventsRepository: AccountEventsRepository,
                                      private val userInfoEventsRepository: UserInfoEventsRepository,
                                      private val cardRepository: CardRepository,
                                      private val userRepository: UserRepository,
                                      private val accountRepository: AccountRepository,
                                      private val userInfoRepository: UserInfoRepository) {
    @BeforeEach
    fun clearDB() {
        tempEventsRepository.deleteAll()
        cardRepository.deleteAll()
        userEventsRepository.deleteAll()
        accountEventsRepository.deleteAll()
        userInfoEventsRepository.deleteAll()
        cardEventsRepository.deleteAll()
        userRepository.deleteAll()
        accountRepository.deleteAll()
//        userInfoRepository.deleteAll()
    }

    @Test
    fun nothing() {
        println("all db cleaned")
    }
}
