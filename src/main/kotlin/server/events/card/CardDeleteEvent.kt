package server.events.card

data class CardDeleteEvent(val id: Long, val login: String) : CardEvent
