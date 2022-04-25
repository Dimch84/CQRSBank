package server.db.postgresql

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import server.db.mongo.entities.UserEntity
import server.db.postgresql.entities.CardEvents
import server.db.postgresql.entities.SimpleCommand
import server.db.postgresql.entities.UserEvents


@Repository
interface TempEventsRepository : JpaRepository<SimpleCommand, Long>

@Repository
interface CardEventsRepository : JpaRepository<CardEvents, Long>

@Repository
interface UserEventsRepository : JpaRepository<UserEvents, Long> {
    fun findByLoginEnd(login: String): UserEvents?
}
