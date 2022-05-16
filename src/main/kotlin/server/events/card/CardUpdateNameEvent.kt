package server.events.card

data class CardUpdateNameEvent(val name: String, val id: Long, val login: String) : CardEvent
