import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

// Экземпляр движка
val CompEngine = ComputationEngine()

fun Application.configureRouting() {
    routing {
        post("/compute") {
            try {
                val request = call.receive<ComputeRequest>()

                // Теперь это асинхронный вызов (благодаря suspend в классе Engine)
                // Ktor будет ждать завершения, не блокируя основной поток сервера
                val id = CompEngine.startComputation(request.matrices)

                call.respond(ComputeResponse(id))
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Ошибка размеров матриц")))
            } catch (_: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Внутренняя ошибка сервера"))
            }
        }

        get("/result/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)

            // Метод getResult не является suspend, поэтому вызываем как обычно
            val result = CompEngine.getResult(id)

            if (result != null) {
                call.respond(result)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}