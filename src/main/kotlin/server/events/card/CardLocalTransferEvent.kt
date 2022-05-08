package server.events.card

data class CardLocalTransferEvent(val money: Long, val idFrom: Long, val idTo: Long) : CardEvent
