package client.api.abstractions

import client.secondary.NULL

data class CardBody(val id: Long = 0, val name: String="", val type: String="", val accountId: Long=NULL, val cardNumber: String="", val expDate: String="", val cvv: String = "") {
    fun toMap() = mapOf("id" to id,
        "name" to name,
        "type" to type,
        "accountId" to accountId,
        "cardNumber" to cardNumber,
        "expDate" to expDate,
        "cvv" to cvv
    )
}
