package server.abstractions.card

import server.events.card.CardEvent

data class CardHistoryRes(val history: List<CardEvent> = listOf()): AnyCardEventRes
