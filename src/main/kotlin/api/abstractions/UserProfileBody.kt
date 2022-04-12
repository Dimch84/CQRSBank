package api.abstractions

data class UserProfileBody(val name: String? = null, val login: String? = null, val phone: String? = null, val email: String? = null)
