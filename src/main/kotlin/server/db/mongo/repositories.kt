package server.db.mongo

import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import server.db.mongo.entities.AccountEntity
import server.db.mongo.entities.CardEntity
import server.db.mongo.entities.UserEntity


@Repository
interface CardRepository : MongoRepository<CardEntity, Long> {
    fun findByAccountId(id: Long): List<CardEntity>
    fun findByCardNumber(number: String): List<CardEntity>
}

@Repository
interface UserRepository : MongoRepository<UserEntity, Long> {
    fun findByLogin(login: String): UserEntity?

    fun findByLoginAndId(login: String, id: Long): UserEntity?
}

@Repository
interface AccountRepository : MongoRepository<AccountEntity, Long> {
    fun findAllByUserId(userId: Long): List<AccountEntity>

    fun findByUserIdAndId(userId: Long, id: Long): AccountEntity?

    fun findByUserId(userId: Long): List<AccountEntity>
}
