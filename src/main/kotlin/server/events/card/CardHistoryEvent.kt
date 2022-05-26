package server.events.card

import server.abstractions.card.HistoryMode

data class CardHistoryEvent(val id: Long, val mode: HistoryMode) : CardEvent
