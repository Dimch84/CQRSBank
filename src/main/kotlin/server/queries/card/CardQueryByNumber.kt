package server.queries.card

import server.queries.Query
import server.secondary.NULL

data class CardQueryByNumber(val number: String=""): Query {
    override fun toMap() = mapOf("number" to number)
}
