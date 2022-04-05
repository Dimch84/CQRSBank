package domain

import api.abstractions.CardBody
import api.abstractions.PaymentBody
import api.abstractions.TransferBody

// TODO(CardBody -> Card in DB)
class Card(val id: String? = null) {
    private val userLogin = "admin"     //: String by lazy { SecurityContextHolder.getContext().authentication.name }

    val data: CardBody
        get() = TODO()

    val history: Any
        get() = TODO()

    fun post(cardBody: CardBody): String = TODO()

    fun pay(paymentBody: PaymentBody): Any = TODO()

    fun transfer(transferBody: TransferBody): Any = TODO()

    fun delete(): String = TODO()
}
