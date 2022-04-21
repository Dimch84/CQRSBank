package client.domain

import client.api.abstractions.AccountBody
import client.api.abstractions.UserProfileBody
import client.postgresql.Account
import client.postgresql.Accounts
import client.postgresql.Users
import org.jetbrains.exposed.sql.transactions.transaction

class User {
    private val userLogin = "admin"     //: String by lazy { SecurityContextHolder.getContext().authentication.name }

    val data: UserProfileBody
        get() = transaction { getUser() }.run { UserProfileBody(name, login, phone, email) }

    val accounts: List<AccountBody>
        get() = transaction { getUser().run { Account.find { Accounts.user_id eq id.value } }
            .map { acc -> AccountBody(acc.user_id.id.value, acc.plan.id.value, acc.money) }
        }

    private fun getUser() = client.postgresql.User.find { Users.login eq userLogin }.also { assert(it.count() == 1L) }.first()

    fun post(userProfileBody: UserProfileBody) = transaction {
        getUser().run {
            userProfileBody.name?.let { name=userProfileBody.name }
            userProfileBody.login?.let { login=userProfileBody.login }
            userProfileBody.phone?.let { phone=userProfileBody.phone }
            userProfileBody.email?.let { email=userProfileBody.email }
        }
        Unit
    }

    fun getAll(): List<UserProfileBody> = transaction {
        client.postgresql.User.all().map { user -> UserProfileBody(name = user.name, login = user.login, phone = user.phone, email = user.email) }
    }
}
