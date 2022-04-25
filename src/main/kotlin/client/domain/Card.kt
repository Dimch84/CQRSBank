package client.domain

import client.api.abstractions.CardBody
import client.api.abstractions.PaymentBody
import client.api.abstractions.TransferBody
import org.jetbrains.exposed.sql.transactions.transaction

class Card(private var id: Int? = null) {
    private val userLogin = "admin"     //: String by lazy { SecurityContextHolder.getContext().authentication.name }

    val data: CardBody
        get() = id?.let { id -> transaction { client.postgresql.Card.findById(id)?.run { CardBody(name, type, account_id.id.value) } } }
            ?: throw Exception("wrong card id")

    val history: Any
        get() = TODO("create history")

    fun post(cardBody: CardBody): Int = transaction {
            client.postgresql.Account.findById(cardBody.account_id)?.let { accountId ->
                client.postgresql.Card.new { name = cardBody.name; type = cardBody.type; account_id = accountId }
            }?.id?.value ?: throw Exception("wrong account id")
        }.also { id = it }

    private fun updateMoney(money: Int) = id?.let { id -> transaction { client.postgresql.Card.findById(id)?.also {
        if (it.account_id.money + money >= 0) it.account_id.money += money  // TODO(move it to Account)
        else throw Exception("too low money on card for operation")
    } } }?.id?.value ?: throw Exception("wrong account id")

    fun pay(paymentBody: PaymentBody): Any = updateMoney(-paymentBody.money.toInt())

    fun transfer(transferBody: TransferBody): Any = updateMoney(-transferBody.money.toInt())

    fun receipt(transferBody: TransferBody): Any = updateMoney(transferBody.money.toInt())

    fun delete(): Unit = id?.let { id -> transaction { client.postgresql.Card.findById(id)?.run { delete() } } }
        ?: throw Exception("wrong account id")

    fun getAll(): List<CardBody> = transaction {
        client.postgresql.Card.all().map { card -> Card(card.id.value).data }
    }
}