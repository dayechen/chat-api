package run.cfloat

import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlin.test.*
import io.ktor.server.testing.*
import run.cfloat.plugins.*

class ApplicationTest {
    @Test
    fun testRoot() = testApplication {
        application {
            configureAuth()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("Hello World!", bodyAsText())
        }
    }
}