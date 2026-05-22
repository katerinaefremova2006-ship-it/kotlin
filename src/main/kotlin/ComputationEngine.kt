import java.util.concurrent.ConcurrentHashMap
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ComputationEngine {
    private val results = ConcurrentHashMap<String, ResultResponse>()

    // Функция теперь suspend, так как мы используем внутри withContext
    suspend fun startComputation(matrices: List<List<List<Int>>>): String = withContext(Dispatchers.Default) {
        val id = UUID.randomUUID().toString()

        if (matrices.isEmpty()) {
            results[id] = ResultResponse(emptyList())
            return@withContext id
        }

        var currentResult = matrices[0]
        for (m in 1 until matrices.size) {
            currentResult = multiplyTwoMatrices(currentResult, matrices[m])
        }

        results[id] = ResultResponse(currentResult)
        id
    }

    private fun multiplyTwoMatrices(a: List<List<Int>>, b: List<List<Int>>): List<List<Int>> {
        val rowsA = a.size
        val colsA = a[0].size
        val colsB = b[0].size

        if (colsA != b.size) {
            throw IllegalArgumentException("Несовместимые размеры матриц")
        }

        val resultMatrix = List(rowsA) { MutableList(colsB) { 0 } }
        for (i in 0 until rowsA) {
            for (j in 0 until colsB) {
                for (k in 0 until colsA) {
                    resultMatrix[i][j] += a[i][k] * b[k][j]
                }
            }
        }
        return resultMatrix
    }

    fun getResult(id: String): ResultResponse? = results[id]
}