package api

import api.abstractions.CardBody
import api.abstractions.PaymentBody
import api.abstractions.TransferBody
import api.config.Application
import db.postgresql.*
import domain.Card
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Application::class])
class CardTest {
    private lateinit var simplePlan: Plan
    private lateinit var user1: User
    private lateinit var account1: Account
    private val db = DbSettings.db
    private var first = true

    companion object {
        @BeforeAll
        @JvmStatic
        internal fun beforeAll() {}  // TODO(not work)
    }

    private fun standardInit() {
        if (first) {
            first = false
            transaction(db) {
                addLogger(StdOutSqlLogger)

                SchemaUtils.drop(Plans, Users, Cards, Accounts)
                SchemaUtils.create(Plans, Users, Cards, Accounts)
                simplePlan = Plan.new { name = "Simple"; description = "6% per year" }
                user1 = User.new { name = "A"; login = "A"; password = "12345678"; phone = "2224412" }
                account1 = Account.new { user_id = user1; plan = simplePlan; money = 1000 }
            }
        }
    }

    private fun getMoney(id: Int) = transaction(db) { Account.findById(id)!!.money }    // TODO(Account fun later)

    @BeforeEach
    fun clearDB() {
        standardInit()
        transaction(db) {
            SchemaUtils.drop(Cards)
            SchemaUtils.create(Cards)
        }
    }

    @Test
    fun addCheckTest() {
        val card = Card()
        val cardBody = CardBody("my_card", "Credit card", account1.id.value)
        val id = card.post(cardBody)
        assert(id == 1)
        assert(card.data == cardBody)
        assert(Card(id).data == cardBody)
        card.delete()
        assertThrows<Exception> { Card(id).data }
    }

    @Test
    fun cardOperationsTest() {
        val accId = account1.id.value
        val card = Card()
        card.post(CardBody("my_card", "Credit card", account1.id.value))
        assert(account1.money == 1000)
        card.pay(PaymentBody(500UL))
        assert(getMoney(accId) == 500)
        assertThrows<Exception> { card.pay(PaymentBody(501UL)) }
        assert(getMoney(accId) == 500)
        card.receipt(TransferBody(200UL))
        assert(getMoney(accId) == 700)
        card.pay(PaymentBody(501UL))
        assert(getMoney(accId) == 199)
        assertThrows<Exception> { card.transfer(TransferBody(250UL)) }
        card.transfer(TransferBody(199UL))
        assert(getMoney(accId) == 0)
        card.receipt(TransferBody(1000UL))
        assert(getMoney(accId) == 1000)
    }
}
