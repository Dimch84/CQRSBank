package server.queries.card

import server.queries.Query
import server.secondary.NULL

data class CardHistoryQuery(val id: Long=NULL): Query {
    override fun toMap() = mapOf("id" to id)
}
