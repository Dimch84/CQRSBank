package server.queries.card

import server.queries.Query

data class CardHistoryQuery(val id: Long=-1): Query {
    override fun toMap() = mapOf("id" to id)
}
