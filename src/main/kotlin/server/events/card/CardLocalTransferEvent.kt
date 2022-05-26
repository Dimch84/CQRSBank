package server.events.card

data class CardLocalTransferEvent(val money: Long, val idFrom: Long, val idTo: Long, val login: String) : CardEvent
