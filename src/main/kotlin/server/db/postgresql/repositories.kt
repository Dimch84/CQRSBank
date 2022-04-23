package server.db.postgresql

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import server.db.postgresql.entities.CardEvents
import server.db.postgresql.entities.SimpleCommand


@Repository
interface TempEventsRepository : JpaRepository<SimpleCommand, Long>

@Repository
interface CardEventsRepository : JpaRepository<CardEvents, Long>
