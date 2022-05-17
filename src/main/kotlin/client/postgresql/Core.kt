package client.postgresql

import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction

object Plans : LongIdTable() {
    val name = varchar("name", 80)
    val description = varchar("description", 256)
}

class Plan(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Plan>(Plans)

    var name by Plans.name
    var description by Plans.description
}

object Users: LongIdTable() {
    val name = varchar("name", 80)
    val login = varchar("login", 40)
    val password = varchar("password", 40)
    val phone = varchar("phone", 40).nullable()
    val email = varchar("email", 40).nullable()
}

class User(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<User>(Users)

    var name by Users.name
    var login by Users.login
    var password by Users.password
    var phone by Users.phone
    var email by Users.email
}

object Accounts: LongIdTable() {
    val name = varchar("name", 80)
    val userId = reference("userId", Users)
    val planId = reference("planId", Plans)
    val money = long("money")
}

class Account(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Account>(Accounts)

    var name by Accounts.name
    var userId by User referencedOn Accounts.userId
    var planId by Plan referencedOn Accounts.planId
    var money by Accounts.money
}

object Cards: LongIdTable() {
    val name = varchar("name", 80)
    val type = varchar("type", 80)
    val cardNumber = varchar("cardNumber", 19)
    val expDate = varchar("expDate", 5)
    val cvv = varchar("cvv", 3)
    val accountId = reference("accountId", Accounts)
}

class Card(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<Card>(Cards)

    var name by Cards.name
    var type by Cards.type
    var cardNumber by Cards.cardNumber
    var expDate by Cards.expDate
    var cvv by Cards.cvv
    var accountId by Account referencedOn Cards.accountId
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

        val account1 = Account.new { name = "account1"; userId = user1; planId = simplePlan; money = 1000 }
        val account2 = Account.new { name = "account2"; userId = user1; planId = vipPlan; money = 100000 }
        val account3 = Account.new { name = "account3"; userId = user2; planId = simplePlan; money = 0 }
        val account4 = Account.new { name = "account4"; userId = user3; planId = simplePlan; money = 1 }

        Card.new { name =  "1234"; type = "Credit card"; cardNumber = "0000 0000 0000 0000"; expDate = "01/30"; cvv = "000"; accountId = account1 }
        Card.new { name =  "1235"; type = "Credit card"; cardNumber = "0000 0000 0000 0001"; expDate = "01/30"; cvv = "001"; accountId = account2 }
        Card.new { name =  "1236"; type = "Credit card"; cardNumber = "0000 0000 0000 0002"; expDate = "01/30"; cvv = "002"; accountId = account2 }
        Card.new { name =  "1237"; type = "Credit card"; cardNumber = "0000 0000 0000 0003"; expDate = "01/30"; cvv = "003"; accountId = account3 }
        Card.new { name =  "1238"; type = "Credit card"; cardNumber = "0000 0000 0000 0004"; expDate = "01/30"; cvv = "004"; accountId = account4 }
    }
}














