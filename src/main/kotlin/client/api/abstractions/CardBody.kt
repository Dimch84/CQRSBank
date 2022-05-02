package client.api.abstractions

import client.secondary.NULL

data class CardBody(val name: String="", val type: String="", val accountId: Long=NULL) {
    fun toMap() = mapOf("name" to name, "type" to type, "accountId" to accountId)
}
