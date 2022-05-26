package server.queries.userInfo

import server.queries.Query
import server.secondary.NULL

data class UserInfoQuery(val id: Long=NULL): Query {
    override fun toMap() = mapOf("id" to id)
}
