package server.queries.user

import server.queries.Query

data class UserQuery(val login: String=""): Query {
    override fun toMap() = mapOf("login" to login)
}
