import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*

val computationEngine = ComputationEngine()

fun Application.configureRouting() {
    routing {

        post("/compute") {
            try {
                val request = call.receive<ComputeRequest>()

                val id = computationEngine.startComputation(request.matrices)

                call.respond(ComputeResponse(id))
            } catch (_: Exception) {
                call.respond(HttpStatusCode.BadRequest)
            }
        }

        get("/result/{id}") {
            val id = call.parameters["id"] ?: return@get call.respond(HttpStatusCode.BadRequest)

            val result = computationEngine.getResult(id)

            if (result != null) {
                call.respond(result)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
    }
}