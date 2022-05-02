package server

import config.Application
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import server.commands.account.AccountCreateCommand
import server.commands.card.*
import server.service.CommandService
import server.db.mongo.AccountRepository
import server.db.mongo.CardRepository
import server.db.postgresql.AccountEventsRepository
import server.db.postgresql.CardEventsRepository
import server.events.card.CardCreateEvent
import server.db.postgresql.entities.CardEvents
import server.queries.account.AccountMoneyQuery


@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [Application::class])
class PayTest @Autowired constructor(private val cardEventsRepository: CardEventsRepository,
                                     private val accountEventsRepository: AccountEventsRepository,
                                     private val cardRepository: CardRepository,
                                     private val accountRepository: AccountRepository,
                                     private val service: CommandService) {
    @BeforeEach
    fun clearDB() {
        cardEventsRepository.deleteAll()
        accountEventsRepository.deleteAll()
        cardRepository.deleteAll()
        accountRepository.deleteAll()
    }

    fun createCard(name: String, accountId: Long, type: String="type") =
        CardCreateCommand(name, type, accountId).run {
            println("create card: ${toMap()}")
            service.send(this) as Long
        }

    fun createAccount(money: Long, userId: Long=1, planId: Long=1) =
        AccountCreateCommand(money, userId, planId).run {
            println("create account: ${toMap()}")
            service.send(this) as Long
        }

    fun getMoney(accountId: Long) =
        AccountMoneyQuery(accountId).run {
            service.send(this)!!.toLong()
                .also { println("account with id=$accountId, money: $it") }
        }

    fun pay(cardId: Long, money: Long) =
        CardPayCommand(money, cardId).run {
            println("card with id=$cardId: pay $money")
            service.send(this)
        }

    fun receipt(cardId: Long, money: Long) =
        CardReceiptCommand(money, cardId).run {
            println("card with id=$cardId: receipt $money")
            service.send(this)
        }

    fun transfer(cardId: Long, money: Long) =
        CardTransferCommand(money, cardId).run {
            println("card with id=$cardId: transfer $money")
            service.send(this)
        }

    @Test
    fun simpleTest() {
        val accountId = createAccount(100)
        val cardId1 = createCard("card1", accountId)
        val cardId2 = createCard("card2", accountId)
        var money = getMoney(accountId)
        assert(money == 100L)
        assert(pay(cardId1, 10) == "ok")
        assert(pay(cardId1, 10) == "ok")
        assert(pay(cardId2, 30) == "ok")
        money = getMoney(accountId)
        assert(money == 50L)
    }

    @Test
    fun payMoreTest() {
        val accountId = createAccount(100)
        val cardId1 = createCard("card1", accountId)
        var money = getMoney(accountId)
        assert(money == 100L)
        assert(pay(cardId1, 100) == "ok")
        money = getMoney(accountId)
        assert(money == 0L)
        assert(pay(cardId1, 1) != "ok")
        money = getMoney(accountId)
        assert(money == 0L)
    }

    @Test
    fun operationsTest() {
        val accountId = createAccount(100)
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
    }
}
