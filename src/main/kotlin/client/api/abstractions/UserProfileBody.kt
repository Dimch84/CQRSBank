package client.api.abstractions

data class UserProfileBody(val id: Long = 0, val name: String="", val login: String="", val phone: String? = null, val email: String? = null) {
    fun toMap() = mapOf("id" to id, "name" to name, "login" to login, "phone" to phone, "email" to email)
}
