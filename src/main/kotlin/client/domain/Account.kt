package client.domain

import client.api.abstractions.AccountBody
import client.api.abstractions.CardBody
import client.postgresql.Cards
import org.jetbrains.exposed.sql.transactions.transaction

class Account(private var id: Long? = null) {
    val data: AccountBody
        get() = id?.let { id -> transaction { client.postgresql.Account.findById(id)?.run {
            AccountBody(userId.id.value, planId.id.value, money) } }
        } ?: throw Exception("wrong account id")

    val money : Long
        get() = data.money

    val cards : List<CardBody>
        get() = id?.let { id ->
            transaction {
                client.postgresql.Card.find { Cards.accountId eq id }.map { card -> Card(card.id.value).data }
            }
        } ?: throw Exception("wrong account id")

    fun post(accountBody: AccountBody): Long = transaction {
        client.postgresql.Plan.findById(accountBody.planId)?.let { planId ->
            client.postgresql.User.findById(accountBody.userId)?.let { userId ->
                client.postgresql.Account.new { this.userId = userId; this.planId = planId; money = accountBody.money }
            }
        }?.id?.value ?: throw Exception("wrong plan id")
    }.also { id = it }

    fun updatePlan(planId: Long) = id?.let { id ->
        transaction {
            client.postgresql.Plan.findById(planId)?.let { planId ->
                client.postgresql.Account.findById(id)?.also { it.planId = planId }
            }
        }
    }?.id?.value ?: throw Exception("wrong plan id")

    fun delete(): Unit = id?.let { id -> transaction { client.postgresql.Account.findById(id)?.run { delete() } } }
        ?: throw Exception("wrong account id")

    fun getAll(): List<AccountBody> = transaction {
        client.postgresql.Account.all().map { account -> Account(account.id.value).data }
    }
}
