package server.db.postgresql.events.card

data class CardCreateEvent(val name: String, val type: String, val account_id: Int, val id: Long? = null) : CardEvent
