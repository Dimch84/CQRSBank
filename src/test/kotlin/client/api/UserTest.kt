package client.api

import client.api.abstractions.UserProfileBody
import config.Application
import client.postgresql.*
import client.postgresql.User as DBUser
import client.domain.User
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Application::class])
class UserTest {
    private val db = DbSettings.db
    private lateinit var simplePlan: Plan
    private lateinit var dbUser: DBUser

    @BeforeEach
    fun clearDB() {
        transaction(db) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.drop(Plans, Users, Cards, Accounts)
            SchemaUtils.create(Plans, Users, Cards, Accounts)
            simplePlan = Plan.new { name = "Simple"; description = "6% per year" }
            dbUser = DBUser.new { name = "A"; login = "admin"; password = "12345678"; phone = "2224412" }
        }
    }

    private fun createAccount(userId: Int = dbUser.id.value, initPlan : Plan = simplePlan, initMoney : Int = 1000) {
        return transaction { Account.new { user_id = DBUser.findById(userId)!!; plan = initPlan; money = initMoney } }
    }

    @Test
    fun addCheckTest() {
        val user = User()
        val userProfileBody = UserProfileBody("name", "admin", "phone", "email")
        user.post(userProfileBody)
        assert(user.data == userProfileBody)
        assert(user.accounts.isEmpty())
        createAccount()
        assert(user.accounts.size == 1)
        assert(user.accounts.first().money == 1000L)
    }
}
