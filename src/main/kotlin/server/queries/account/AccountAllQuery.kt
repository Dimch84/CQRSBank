package server.queries.account

import server.queries.Query

class AccountAllQuery(val login: String?=null): Query {
    override fun toMap() = mapOf("login" to login.toString())
}
