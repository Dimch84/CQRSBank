package api

import api.abstractions.RegisterBody
import api.config.Application
import db.postgresql.*
import domain.Register
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Application::class])
class RegisterTest {
    private val db = DbSettings.db

    @BeforeEach
    fun clearDB() {
        transaction(db) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.drop(Plans, Users, Cards, Accounts)
            SchemaUtils.create(Plans, Users, Cards, Accounts)
        }
    }

    @Test
    fun addCheckTest() {
        val login = "my_login"
        val register = Register(login)
        val registerBody = RegisterBody("my_name", "my_pass")
        val id = register.post(registerBody)
        assert(id == 1)
        assert(register.data.name == registerBody.name)
        assert(Register(login).data.name == registerBody.name)
        assertThrows<Exception> { register.post(registerBody) }
        register.delete()
        register.post(registerBody)
        assert(register.data.name == registerBody.name)
    }
}
