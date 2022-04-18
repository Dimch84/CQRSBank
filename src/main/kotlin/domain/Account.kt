package domain

import api.abstractions.AccountBody
import api.abstractions.CardBody
import db.postgresql.Cards
import org.jetbrains.exposed.sql.transactions.transaction
import db.postgresql.Plan as DBPlan
import db.postgresql.Card as DBCard
import db.postgresql.User as DBUser
import db.postgresql.Account as DBAccount

class Account(private var id: Int? = null) {
    val data: AccountBody
        get() = id?.let { id -> transaction { DBAccount.findById(id)?.run { AccountBody(user_id.id.value, plan.id.value, money) } } }
            ?: throw Exception("wrong account id")

    val money : Int
        get() = data.money

    val cards : List<CardBody>
        get() = id?.let { id ->
            transaction {
                DBCard.find { Cards.account_id eq id }?.map { card -> Card(card.id.value).data }
            }
        } ?: throw Exception("wrong account id")

    fun post(accountBody: AccountBody): Int = transaction {
        DBPlan.findById(accountBody.plan)?.let { planId ->
            DBUser.findById(accountBody.userId)?.let { userId ->
                DBAccount.new { user_id = userId; plan = planId; money =  accountBody.money }
            }
        }?.id?.value ?: throw Exception("wrong plan id")
    }.also { id = it }

    fun updatePlan(plan: Int) = id?.let { id ->
        transaction {
            DBPlan.findById(plan)?.let { planId ->
                DBAccount.findById(id)?.also { it.plan = planId }
            }
        }
    }?.id?.value ?: throw Exception("wrong plan id")

    fun delete(): Unit = id?.let { id -> transaction { DBAccount.findById(id)?.run { delete() } } }
        ?: throw Exception("wrong account id")

    fun getAll(): List<AccountBody> = transaction {
        DBAccount.all().map { account -> Account(account.id.value).data }
    }
}
