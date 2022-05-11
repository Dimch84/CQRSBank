package server.observers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import server.abstractions.userInfo.AnyUserInfoEventRes
import server.abstractions.userInfo.UserInfoEventRes
import server.db.elasticsearch.UserInfoRepository
import server.db.elasticsearch.entities.user_info_entity

@Component
class ObserverUserInfo @Autowired constructor(private val userInfoRepository: UserInfoRepository): AnyObserver<AnyUserInfoEventRes> {
    override fun update(res: AnyUserInfoEventRes): Unit = when(res) {
        is UserInfoEventRes -> {
            userInfoRepository.save(user_info_entity(res.id!!, res.data))
            Unit
        }
        else -> throw Exception("wrong event")
    }
}
