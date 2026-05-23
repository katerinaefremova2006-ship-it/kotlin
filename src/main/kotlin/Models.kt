import kotlinx.serialization.Serializable

@Serializable
data class ComputeRequest(
    val matrices: List<List<List<Int>>> // Неограниченное количество матриц
)

@Serializable
data class ComputeResponse(val id: String)

@Serializable
data class ResultResponse(val result: List<List<Int>>)