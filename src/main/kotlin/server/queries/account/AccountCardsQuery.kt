package server.queries.account

import server.queries.Query
import server.secondary.NULL

data class AccountCardsQuery(val id: Long=NULL): Query {
    override fun toMap() = mapOf("id" to id)
}
