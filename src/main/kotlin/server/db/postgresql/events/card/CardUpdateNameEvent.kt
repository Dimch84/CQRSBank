package server.db.postgresql.events.card

data class CardUpdateNameEvent(val name: String, val id: Long) : CardEvent
