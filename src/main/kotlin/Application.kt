import io.ktor.server.application.*

// Точка входа, которая читает application.yaml и запускает сервер на 8080 порту
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

// Тот самый module, который прописан в application.yaml
fun Application.module() {
    configureSerialization()

    configureRouting()
}