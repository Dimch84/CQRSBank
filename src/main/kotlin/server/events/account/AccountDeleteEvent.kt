package server.events.account

data class AccountDeleteEvent(val id: Long, val login: String) : AccountEvent
