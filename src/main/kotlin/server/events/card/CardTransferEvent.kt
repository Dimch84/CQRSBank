package server.events.card

data class CardTransferEvent(val money: Long, val id: Long, val login: String) : CardEvent
