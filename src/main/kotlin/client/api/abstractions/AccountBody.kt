package client.api.abstractions

import client.secondary.NULL

data class AccountBody(val money: Long=0, val userId: Long=NULL, val planId: Long=NULL) {
    fun toMap() = mapOf("money" to money, "userId" to userId, "planId" to planId)
}
