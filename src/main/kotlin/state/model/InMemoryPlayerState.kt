package state.model

import game.model.Player
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicLong

/**
 * Thread-safe [PlayerState] представление, хранящееся в памяти
 */
class InMemoryPlayerState(
    player: Player
) : PlayerState(player) {

    private val usedWords: ConcurrentHashMap<String, Any> = ConcurrentHashMap()

    private val score = AtomicLong(0)

    override fun getScore(): Long = score.get()

    override fun addWord(word: String, cost: Int): Long {
        val wordCost = if (usedWords.putIfAbsent(word, Any()) == null) cost else 0
        return score.addAndGet(wordCost.toLong())
    }

    override fun isWordUsed(word: String): Boolean = usedWords[word] != null
}