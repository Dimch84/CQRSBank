package api

import api.abstractions.AccountBody
import api.config.Application
import db.postgresql.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Application::class])
class AccountTest {
    private lateinit var card1: Card
    private lateinit var card2: Card
    private lateinit var simplePlan: Plan
    private lateinit var vipPlan: Plan
    private lateinit var user1: User
    private lateinit var account1: Account
    private val db = TestDbSettings.db

    @BeforeEach
    fun clearDB() {
        transaction(db) {
            addLogger(StdOutSqlLogger)

            SchemaUtils.drop(Cards, Accounts, Users, Plans)
            SchemaUtils.create(Cards, Accounts, Users, Plans)
            simplePlan = Plan.new { name = "Simple"; description = "6% per year" }
            vipPlan = Plan.new { name = "Vip"; description = "256% per year" }
            user1 = User.new { name = "A"; login = "A"; password = "12345678"; phone = "2224412" }
            account1 = Account.new { user_id = user1; plan = simplePlan; money = 1000 }
            card1 = Card.new { name =  "1234"; type = "Credit card"; account_id = account1 }
            card2 = Card.new { name =  "1235"; type = "Credit card"; account_id = account1 }
        }
    }

    @Test
    fun addUpdateCheckTest() {
        assert(domain.Account().getAll().size == 1)
        val account = domain.Account()
        val accountBody = AccountBody(userId = user1.id.value, plan = simplePlan.id.value, money = 1000)
        val id = account.post(accountBody)
        assert(domain.Account().getAll().size == 2)
        assert(id == 2)
        assert(account.data.money == accountBody.money && account.data.userId == accountBody.userId && account.data.plan == accountBody.plan)
        assert(domain.Account(id).data.money == accountBody.money && domain.Account(id).data.userId == accountBody.userId && domain.Account(id).data.plan == accountBody.plan)
        account.delete()
        assertThrows<Exception> { domain.Account(id).data }
    }

    @Test
    fun cardsCheckTest() {
        val cardBody1 = domain.Card(card1.id.value).data
        val cardBody2 = domain.Card(card2.id.value).data

        val account = domain.Account(account1.id.value)

        val cards = account.cards
        assert(cards.size == 2)
        assert(cards[0] == cardBody1 && cards[1] == cardBody2)
    }

    @Test
    fun changePlanTest() {
        val account = domain.Account(account1.id.value)
        account.updatePlan(vipPlan.id.value)
        assert(account.data.plan == vipPlan.id.value)
    }
}
