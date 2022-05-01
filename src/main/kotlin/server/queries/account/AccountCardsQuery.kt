package server.queries.account

import server.queries.Query

data class AccountCardsQuery(val id: Long=-1): Query {
    override fun toMap() = mapOf("id" to id)
}
