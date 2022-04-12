package domain

import api.abstractions.AccountBody
import api.abstractions.UserProfileBody
import db.postgresql.Account
import db.postgresql.Accounts
import db.postgresql.Users
import db.postgresql.User as DBUser
import org.jetbrains.exposed.sql.transactions.transaction

class User {
    private val userLogin = "admin"     //: String by lazy { SecurityContextHolder.getContext().authentication.name }

    val data: UserProfileBody
        get() = transaction { getUser() }.run { UserProfileBody(name, login, phone, email) }

    val accounts: List<AccountBody>
        get() = transaction { getUser().run { Account.find { Accounts.user_id eq id.value } }
            .map { acc -> AccountBody(acc.plan.name, acc.money) }
        }

    private fun getUser() = DBUser.find { Users.login eq userLogin }.also { assert(it.count() == 1L) }.first()

    fun post(userProfileBody: UserProfileBody) = transaction {
        getUser().run {
            userProfileBody.name?.let { name=userProfileBody.name }
            userProfileBody.login?.let { login=userProfileBody.login }
            userProfileBody.phone?.let { phone=userProfileBody.phone }
            userProfileBody.email?.let { email=userProfileBody.email }
        }
        Unit
    }
}
