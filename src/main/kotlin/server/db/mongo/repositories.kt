package server.db.mongo

import org.springframework.stereotype.Repository
import server.db.mongo.entities.CardEntity
import org.springframework.data.mongodb.repository.MongoRepository
import server.db.mongo.entities.UserEntity


@Repository
interface CardRepository : MongoRepository<CardEntity, Long>

@Repository
interface UserRepository : MongoRepository<UserEntity, Long>
