package server.queries.card

import server.abstractions.card.HistoryMode
import server.abstractions.card.HistoryMode.FULL
import server.queries.Query
import server.secondary.NULL

data class CardHistoryQuery(val id: Long=NULL, val mode: HistoryMode=FULL, val login: String?=null): Query {
    override fun toMap() = mapOf("id" to id, "mode" to mode, "login" to login.toString())
}
