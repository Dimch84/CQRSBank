package client.api.abstractions

data class UserProfileBody(val name: String="", val login: String="", val phone: String? = null, val email: String? = null) {
    fun toMap() = mapOf("name" to name, "login" to login, "phone" to phone, "email" to email)
}
