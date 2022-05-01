package server.queries.account

import server.queries.Query

data class AccountMoneyQuery(val id: Long=-1): Query {
    override fun toMap() = mapOf("id" to id)
}
