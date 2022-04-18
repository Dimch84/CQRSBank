package api.config

import db.postgresql.DbSettings
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.jetbrains.exposed.sql.transactions.TransactionManager

@SpringBootApplication
@ComponentScan("api")
class Application

fun main(args: Array<String>) {
    TransactionManager.defaultDatabase = DbSettings.db
    runApplication<Application>(*args)
}
