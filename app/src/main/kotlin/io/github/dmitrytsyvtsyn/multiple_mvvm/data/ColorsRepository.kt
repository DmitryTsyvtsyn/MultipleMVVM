package io.github.dmitrytsyvtsyn.multiple_mvvm.data

import android.graphics.Color
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.random.Random

class ColorsRepository {

    private val data = List(5) {
        Color.rgb(
            Random.nextInt(0, 200),
            Random.nextInt(0, 200),
            Random.nextInt(0, 200)
        )
    }

    suspend fun fetchAll() = withContext(Dispatchers.Default) {
        data.toList()
    }

}