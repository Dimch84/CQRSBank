package server

import config.Application
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import server.abstractions.card.HistoryMode
import server.commands.account.AccountCreateCommand
import server.commands.card.*
import server.commands.user.UserCreateCommand
import server.db.mongo.AccountRepository
import server.db.mongo.CardRepository
import server.db.mongo.UserRepository
import server.db.postgresql.AccountEventsRepository
import server.db.postgresql.CardEventsRepository
import server.db.postgresql.UserEventsRepository
import server.queries.account.AccountMoneyQuery
import server.queries.card.CardHistoryQuery
import server.queries.card.CardMoneyQuery
import server.service.CommandService


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [Application::class])
class PayTest @Autowired constructor(private val cardEventsRepository: CardEventsRepository,
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
    }

    fun createUser(name: String, login: String="login", password: String="pass") =
        UserCreateCommand(name, login, password).run {
            println("create user: ${toMap()}")
            service.send(this) as Long
        }

    fun createAccount(money: Long, userId: Long, planId: Long=1) =
        AccountCreateCommand(money, userId, planId).run {
            println("create account: ${toMap()}")
            service.send(this) as Long
        }

    fun createCard(name: String, accountId: Long, type: String="type", login: String="login") =
        CardCreateCommand(name, type, accountId, login).run {
            println("create card: ${toMap()}")
            service.send(this) as Long
        }

    fun getMoney(accountId: Long, login: String="login") =
        AccountMoneyQuery(accountId, login).run {
            service.send(this)!!
                .also { println("account with id=$accountId, money: $it") }
        }

    fun getMoneyCard(cardId: Long, login: String="login") =
        CardMoneyQuery(cardId, login).run {
            service.send(this)!!
                .also { println("card with id=$cardId, money: $it") }
        }

    fun pay(cardId: Long, money: Long, login: String="login") =
        CardPayCommand(money, cardId, login).run {
            println("card with id=$cardId: pay $money")
            service.send(this)
        }

    fun receipt(cardId: Long, money: Long, login: String="login") =
        CardReceiptCommand(money, cardId, login).run {
            println("card with id=$cardId: receipt $money")
            service.send(this)
        }

    fun transfer(cardId: Long, money: Long, login: String="login") =
        CardTransferCommand(money, cardId, login).run {
            println("card with id=$cardId: transfer $money")
            service.send(this)
        }

    fun localTransfer(cardId: Long, cardIdTo: Long, money: Long, login: String="login") =
        CardLocalTransferCommand(money, cardId, cardIdTo, login).run {
            println("card with id=$cardId: transfer $money to card with id=$cardIdTo")
            service.send(this)
        }

    fun history(cardId: Long, login: String="login") =
        CardHistoryQuery(cardId, login=login).run {
            println("card with id=$cardId history: ${
                (service.send(this) as List<*>).joinToString(prefix = "\n", separator = ",\n")}")
        }

    fun historyPays(cardId: Long, login: String="login") =
        CardHistoryQuery(cardId, HistoryMode.PAYS, login).run {
            println("card with id=$cardId history pays: ${
                (service.send(this) as List<*>).joinToString(prefix = "\n", separator = ",\n")}")
        }

    @Test
    fun simpleTest() {
        val userId = createUser("user")
        val accountId = createAccount(100, userId)
        val cardId1 = createCard("card1", accountId)
        val cardId2 = createCard("card2", accountId)
        var money = getMoney(accountId)
        assert(getMoneyCard(cardId1) == money)
        assert(getMoneyCard(cardId2) == money)
        assert(money == 100L)
        assert(pay(cardId1, 10) == "ok")
        assert(pay(cardId1, 10) == "ok")
        assert(pay(cardId2, 30) == "ok")
        money = getMoney(accountId)
        assert(getMoneyCard(cardId1) == money)
        assert(getMoneyCard(cardId2) == money)
        assert(money == 50L)
    }

    @Test
    fun payMoreTest() {
        val userId = createUser("user")
        val accountId = createAccount(100, userId)
        val cardId1 = createCard("card1", accountId)
        var money = getMoney(accountId)
        assert(getMoneyCard(cardId1) == money)
        assert(money == 100L)
        assert(pay(cardId1, 100) == "ok")
        money = getMoney(accountId)
        assert(getMoneyCard(cardId1) == money)
        assert(money == 0L)
        assert(pay(cardId1, 1) != "ok")
        money = getMoney(accountId)
        assert(getMoneyCard(cardId1) == money)
        assert(money == 0L)
    }

    @Test
    fun operationsAndHistoryTest() {
        val userId = createUser("user")
        val accountId = createAccount(100, userId)
        val cardId1 = createCard("card1", accountId)
        val cardId2 = createCard("card2", accountId)
        assert(getMoney(accountId) == 100L)
        assert(pay(cardId1, 40) == "ok")
        assert(getMoney(accountId) == 60L)
        assert(receipt(cardId2, 61) != "ok")
        assert(getMoney(accountId) == 60L)
        assert(transfer(cardId1, 5) == "ok")
        assert(getMoney(accountId) == 65L)
        assert(receipt(cardId2, 61) == "ok")
        assert(getMoney(accountId) == 4L)
        assert(pay(cardId1, 5) != "ok")
        assert(pay(cardId1, 4) == "ok")
        assert(getMoney(accountId) == 0L)

        history(cardId1)
        historyPays(cardId1)
        history(cardId2)
        historyPays(cardId2)
    }

    @Test
    fun localTransferTest() {
        val userId = createUser("user")
        val accountId1 = createAccount(100, userId)
        val accountId2 = createAccount(100, userId)
        val cardId1 = createCard("card1", accountId1)
        val cardId2 = createCard("card2", accountId2)
        assert(getMoneyCard(cardId1) == 100L)
        assert(getMoneyCard(cardId2) == 100L)
        assert(localTransfer(cardId1, cardId2, 60) == "ok")
        assert(getMoneyCard(cardId1) == 40L)
        assert(getMoneyCard(cardId2) == 160L)
        assert(localTransfer(cardId1, cardId2, 50) != "ok")
        assert(getMoneyCard(cardId1) == 40L)
        assert(getMoneyCard(cardId2) == 160L)
    }
}
