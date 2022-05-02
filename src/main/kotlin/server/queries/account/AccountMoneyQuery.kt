package server.queries.account

import server.queries.Query
import server.secondary.NULL

data class AccountMoneyQuery(val id: Long=NULL): Query {
    override fun toMap() = mapOf("id" to id)
}
