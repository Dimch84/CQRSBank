package server.events.card

data class CardCreateEvent(val name: String, val type: String, val accountId: Long, val login: String, val id: Long?=null) : CardEvent
