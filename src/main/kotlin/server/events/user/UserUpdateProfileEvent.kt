package server.events.user

data class UserUpdateProfileEvent(val name: String?, val login: String?, val phone: String?, val email: String?,
                                  val id: Long?=null) : UserEvent
