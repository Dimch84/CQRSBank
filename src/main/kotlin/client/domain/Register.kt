package client.domain

import client.api.abstractions.RegisterBody
import client.api.abstractions.UserProfileBody
import client.postgresql.Users
import org.jetbrains.exposed.sql.transactions.transaction

class Register(private val login: String) {
    val data: UserProfileBody
        get() = transaction { getUser() }.run { UserProfileBody(name, login, phone, email) }

    private fun getUser() = client.postgresql.User.find { Users.login eq login }.also { assert(it.count() == 1L) }.first()

    fun post(registerBody: RegisterBody) = transaction {
        if (client.postgresql.User.find { Users.login eq login }.count() != 0L)
            throw Exception("user with login=${Users.login} already exist")
        val regLogin = login
        client.postgresql.User.new { name = registerBody.name; login = regLogin; password = registerBody.password }
    }.id.value

    fun delete(): Unit = transaction { getUser().delete() }

    fun getAll(): List<RegisterBody> = transaction {
        client.postgresql.User.all().map { user -> RegisterBody(name = user.name, password = user.password) }
    }
}
