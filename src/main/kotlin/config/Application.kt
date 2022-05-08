package config

import client.postgresql.DbSettings
import client.postgresql.InitDatabase
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import server.db.mongo.AccountRepository
import server.db.mongo.CardRepository
import server.db.mongo.UserRepository
import server.db.postgresql.AccountEventsRepository
import server.db.postgresql.CardEventsRepository
import server.db.postgresql.TempEventsRepository
import server.db.postgresql.UserEventsRepository


@SpringBootApplication
@ComponentScan("client.api", "server")
@EnableMongoRepositories(basePackageClasses=[CardRepository::class, UserRepository::class, AccountRepository::class])
@EnableJpaRepositories(basePackageClasses=[TempEventsRepository::class, CardEventsRepository::class,
    UserEventsRepository::class, AccountEventsRepository::class])
@EntityScan("server.db")
class Application

fun main(args: Array<String>) {
    InitDatabase()


//    TransactionManager.defaultDatabase = DbSettings.db
    runApplication<Application>(*args)
}
