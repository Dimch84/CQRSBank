package client.domain

import client.api.abstractions.AccountBody
import client.api.abstractions.CardBody
import client.postgresql.Cards
import org.jetbrains.exposed.sql.transactions.transaction

class Account(private var id: Int? = null) {
    val data: AccountBody
        get() = id?.let { id -> transaction { client.postgresql.Account.findById(id)?.run {
            AccountBody(user_id.id.value.toLong(), plan.id.value.toLong(), money.toLong()) } }
        } ?: throw Exception("wrong account id")

    val money : Long
        get() = data.money

    val cards : List<CardBody>
        get() = id?.let { id ->
            transaction {
                client.postgresql.Card.find { Cards.account_id eq id }.map { card -> Card(card.id.value).data }
            }
        } ?: throw Exception("wrong account id")

    fun post(accountBody: AccountBody): Int = transaction {
        client.postgresql.Plan.findById(accountBody.planId.toInt())?.let { planId ->
            client.postgresql.User.findById(accountBody.userId.toInt())?.let { userId ->
                client.postgresql.Account.new { user_id = userId; plan = planId; money = accountBody.money.toInt() }
            }
        }?.id?.value ?: throw Exception("wrong plan id")
    }.also { id = it }

    fun updatePlan(plan: Long) = id?.let { id ->
        transaction {
            client.postgresql.Plan.findById(plan.toInt())?.let { planId ->
                client.postgresql.Account.findById(id)?.also { it.plan = planId }
            }
        }
    }?.id?.value ?: throw Exception("wrong plan id")

    fun delete(): Unit = id?.let { id -> transaction { client.postgresql.Account.findById(id)?.run { delete() } } }
        ?: throw Exception("wrong account id")

    fun getAll(): List<AccountBody> = transaction {
        client.postgresql.Account.all().map { account -> Account(account.id.value).data }
    }
}
