package client.postgresql

import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.transactions.TransactionManager

object Plans : IntIdTable() {
    val name = varchar("name", 80)
    val description = varchar("description", 256)
}

class Plan(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Plan>(Plans)

    var name by Plans.name
    var description by Plans.description
}

object Users: IntIdTable() {
    val name = varchar("name", 80)
    val login = varchar("login", 40)
    val password = varchar("password", 40)
    val phone = varchar("phone", 40).nullable()
    val email = varchar("email", 40).nullable()
}

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var name by Users.name
    var login by Users.login
    var password by Users.password
    var phone by Users.phone
    var email by Users.email
}

object Accounts: IntIdTable() {
    val user_id = reference("user_id", Users)
    val plan = reference("plan", Plans)
    val money = integer("money")
}

class Account(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Account>(Accounts)

    var user_id by User referencedOn Accounts.user_id
    var plan by Plan referencedOn Accounts.plan
    var money by Accounts.money
}

object Cards: IntIdTable() {
    val name = varchar("name", 80)
    val type = varchar("type", 80)
    val account_id = reference("account_id", Accounts)
}

class Card(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<Card>(Cards)

    var name by Cards.name
    var type by Cards.type
    var account_id by Account referencedOn Cards.account_id
}

object DbSettings {
    // TODO: вынести эти константы куда-то (понять куда)
    val db by lazy {
        Database.connect(
            url = "jdbc:postgresql://127.0.0.1:5432/cqrs",
            driver = "org.postgresql.Driver",
            user = "cqrs",
            password = "cqrs"
        )
    }
}

object TestDbSettings {
    val db by lazy {
        Database.connect(
            url = "jdbc:postgresql://127.0.0.1:5431/cqrs",
            driver = "org.postgresql.Driver",
            user = "cqrs",
            password = "cqrs"
        )
    }
}

fun InitDatabase() {
    TransactionManager.defaultDatabase = DbSettings.db

    transaction(DbSettings.db) {
        addLogger(StdOutSqlLogger)

        SchemaUtils.drop(Plans, Users, Cards, Accounts)
        SchemaUtils.create(Plans, Users, Cards, Accounts)

        // TODO: Fake. Del later
        val simplePlan = Plan.new { name = "Simple"; description = "6% per year" }
        val vipPlan = Plan.new { name = "VIP"; description = "256% per year" }

        val user1 = User.new { name = "A"; login = "A"; password = "12345678"; phone = "2224412" }
        val user2 = User.new { name = "N"; login = "N"; password = "12345678"; phone = "3132131" }
        val user3 = User.new { name = "S"; login = "S"; password = "12345678"; phone = "2224412" }

        val account1 = Account.new { user_id = user1; plan = simplePlan; money = 1000 }
        val account2 = Account.new { user_id = user1; plan = vipPlan; money = 100000 }
        val account3 = Account.new { user_id = user2; plan = simplePlan; money = 0 }
        val account4 = Account.new { user_id = user3; plan = simplePlan; money = 1 }

        Card.new { name =  "1234"; type = "Credit card"; account_id = account1 }
        Card.new { name =  "1235"; type = "Credit card"; account_id = account2 }
        Card.new { name =  "1236"; type = "Credit card"; account_id = account2 }
        Card.new { name =  "1237"; type = "Credit card"; account_id = account3 }
        Card.new { name =  "1238"; type = "Credit card"; account_id = account4 }
    }
}














