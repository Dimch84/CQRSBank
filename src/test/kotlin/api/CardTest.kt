package api

import api.config.Application
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [Application::class])
class CardTest {
    @BeforeEach
    fun clearDB() {

    }

    @Test
    fun simpleTest() {
        assert(0 == 0)
    }
}
