package server.events.user

data class UserCreateEvent(val name: String, val login: String, val password: String, val phone: String?,
                           val email: String?, val id: Long?=null) : UserEvent
