package client.domain

import client.api.abstractions.AccountBody
import client.api.abstractions.CardBody
import client.postgresql.Cards
import org.jetbrains.exposed.sql.transactions.transaction

class Account(private var id: Int? = null) {
    val data: AccountBody
        get() = id?.let { id -> transaction { client.postgresql.Account.findById(id)?.run { AccountBody(user_id.id.value, plan.id.value, money) } } }
            ?: throw Exception("wrong account id")

    val money : Int
        get() = data.money

    val cards : List<CardBody>
        get() = id?.let { id ->
            transaction {
                client.postgresql.Card.find { Cards.account_id eq id }?.map { card -> Card(card.id.value).data }
            }
        } ?: throw Exception("wrong account id")

    fun post(accountBody: AccountBody): Int = transaction {
        client.postgresql.Plan.findById(accountBody.plan)?.let { planId ->
            client.postgresql.User.findById(accountBody.userId)?.let { userId ->
                client.postgresql.Account.new { user_id = userId; plan = planId; money =  accountBody.money }
            }
        }?.id?.value ?: throw Exception("wrong plan id")
    }.also { id = it }

    fun updatePlan(plan: Int) = id?.let { id ->
        transaction {
            client.postgresql.Plan.findById(plan)?.let { planId ->
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
