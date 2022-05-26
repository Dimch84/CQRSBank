package server.queries.account

import server.queries.Query
import server.secondary.NULL

data class AccountCardsQuery(val id: Long=NULL, val login: String?=null): Query {
    override fun toMap() = mapOf("id" to id, "login" to login.toString())
}
