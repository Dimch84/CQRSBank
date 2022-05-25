package client.api.abstractions

import client.secondary.NULL

data class AccountBody(val id: Long = 0, val name: String="", val money: Long=0, val userId: Long=NULL, val planId: Long=NULL) {
    fun toMap() = mapOf("id" to id, "name" to name, "money" to money, "userId" to userId, "planId" to planId)
}
