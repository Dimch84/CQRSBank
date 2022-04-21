package server.db.postgresql

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import server.eventsDomain.card.CardEvents


@Repository
interface CardEventsRepository : JpaRepository<CardEvents, Long>
