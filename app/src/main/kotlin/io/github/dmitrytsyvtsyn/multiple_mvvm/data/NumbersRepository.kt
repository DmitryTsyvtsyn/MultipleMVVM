package io.github.dmitrytsyvtsyn.multiple_mvvm.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class NumbersRepository {

    private val data = mutableListOf<Int>()

    init {
        val initialSize = Random.nextInt(0, 5)
        val initialData = List(initialSize) {
            Random.nextInt(1_000, 9_999)
        }
        data.addAll(initialData)
    }

    suspend fun fetchAll() = withContext(Dispatchers.Default) {
        data.toList()
    }

    suspend fun addRandomNumber() = withContext(Dispatchers.Default) {
        data.add(Random.nextInt(1_000, 9_999))
    }

    suspend fun removeLastNumber() = withContext(Dispatchers.Default) {
        if (data.isEmpty()) return@withContext false
        data.removeAt(data.lastIndex)
        true
    }

}