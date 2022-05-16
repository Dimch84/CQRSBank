package server.events.card

data class CardPayEvent(val money: Long, val id: Long, val login: String) : CardEvent
