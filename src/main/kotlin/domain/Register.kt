package domain

import api.abstractions.RegisterBody
import api.abstractions.UserProfileBody
import db.postgresql.Users
import db.postgresql.User as DBUser
import org.jetbrains.exposed.sql.transactions.transaction

class Register(private val login: String) {
    val data: UserProfileBody
        get() = transaction { getUser() }.run { UserProfileBody(name, login, phone, email) }

    private fun getUser() = DBUser.find { Users.login eq login }.also { assert(it.count() == 1L) }.first()

    fun post(registerBody: RegisterBody) = transaction {
        if (DBUser.find { Users.login eq login }.count() != 0L)
            throw Exception("user with login=${Users.login} already exist")
        val regLogin = login
        DBUser.new { name = registerBody.name; login = regLogin; password = registerBody.password }
    }.id.value

    fun delete(): Unit = transaction { getUser().delete() }

    fun getAll(): List<RegisterBody> = transaction {
        DBUser.all().map { user -> RegisterBody(name = user.name, password = user.password) }
    }
}
