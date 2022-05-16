package server.queries.card

import server.queries.Query

class CardAllQuery(val login: String?=null): Query {
    override fun toMap() = mapOf("login" to login.toString())
}
