package config

import client.postgresql.DbSettings
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import server.db.mongo.CardRepository
import server.db.postgresql.CardEventsRepository
import server.db.postgresql.TempEventsRepository


@SpringBootApplication
@ComponentScan("client.api", "server")
@EnableMongoRepositories(basePackageClasses=[CardRepository::class])
@EnableJpaRepositories(basePackageClasses=[CardEventsRepository::class, TempEventsRepository::class])
@EntityScan("server.db")
class Application

fun main(args: Array<String>) {
    TransactionManager.defaultDatabase = DbSettings.db
    runApplication<Application>(*args)
}
