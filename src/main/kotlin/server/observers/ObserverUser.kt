package server.observers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.annotation.Id
import org.springframework.stereotype.Component
import server.abstractions.card.AnyCardEventRes
import server.abstractions.card.CardDeleteEventRes
import server.abstractions.card.CardEventRes
import server.abstractions.user.AnyUserEventRes
import server.abstractions.user.UserDeleteEventRes
import server.abstractions.user.UserEventRes
import server.db.mongo.CardRepository
import server.db.mongo.UserRepository
import server.db.mongo.entities.CardEntity
import server.db.mongo.entities.UserEntity

@Component
class ObserverUser @Autowired constructor(private val userRepository: UserRepository): AnyObserver<AnyUserEventRes> {
    override fun update(res: AnyUserEventRes): Unit = when(res) {
        is UserEventRes -> {
            userRepository.save(UserEntity(res.id!!, res.name!!, res.login!!, res.password!!, res.phone, res.email))
            Unit
        }
        is UserDeleteEventRes -> {
            userRepository.deleteById(res.id!!)
        }
        else -> throw Exception("wrong event")
    }
}
