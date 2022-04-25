package server.events.user

data class UserDeleteEvent(val login: String) : UserEvent
