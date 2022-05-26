package server.queries.userInfo

import server.queries.Query
import server.secondary.NULL

data class UserInfoRequestQuery(val id: Long=NULL, val query: String=""): Query {
    override fun toMap() = mapOf("id" to id, "query" to query)
}
