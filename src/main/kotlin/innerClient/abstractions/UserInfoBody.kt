package innerClient.abstractions

import client.secondary.NULL

data class UserInfoBody(val id: Long=NULL, val data: List<String> = listOf()) {
    fun toMap() = mapOf("id" to id, "data" to data)
}
