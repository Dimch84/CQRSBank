package server.db.postgresql

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import server.db.postgresql.entities.*


@Repository
interface TempEventsRepository : JpaRepository<SimpleCommand, Long>

@Repository
interface CardEventsRepository : JpaRepository<CardEvents, Long>

@Repository
interface UserEventsRepository : JpaRepository<UserEvents, Long> {
    fun findByLoginEnd(login: String?): UserEvents?
}

@Repository
interface AccountEventsRepository : JpaRepository<AccountEvents, Long>

@Repository
interface UserInfoEventsRepository : JpaRepository<UserInfoEvents, Long>
