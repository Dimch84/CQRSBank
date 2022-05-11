package server.handlers.userInfo.query

import innerClient.abstractions.UserInfoBody
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.db.elasticsearch.UserInfoRepository
import server.handlers.AnyQueryHandler
import server.queries.userInfo.UserInfoQuery

@Component
class UserInfoQueryHandler @Autowired constructor(private val userInfoRepository: UserInfoRepository)
        : AnyQueryHandler<UserInfoQuery>() {
    override fun handle(query: UserInfoQuery): String {
        val userInfo = userInfoRepository.findById(query.id).get().run { UserInfoBody(id, data) }
        return GSON.toJson(userInfo)
    }
}
