package state.repository.cache

import game.model.PlayerResult
import state.LeaderBoardUpdateSubscriber
import java.util.*

/**
 * Thread-safe in-memory [LeadersCache]
 * Подразумевается, что количество очков у игрока может только возрастать
 *
 * Хранит только [leadersCapacity] записей с резульатами лидеров
 * При начислении очков запрос к данным репозитория не требуется, обновляется только локальный кеш
 * При удалении игрока выполняет запрос [retrieveResults] на получение данных из репозитория,
 * если удаленный игрок был одним из лидеров
 */
class InMemoryLeadersCache(
    private val leadersCapacity: Int,
    private val retrieveResults: (limit: Int) -> Iterable<PlayerResult>,
) : LeadersCache {
    private val subscribers = mutableListOf<LeaderBoardUpdateSubscriber>()
    private var minScore = Long.MIN_VALUE
    private val leaders = TreeSet<PlayerResult> { r1, r2 ->
        val res = r2.score.compareTo(r1.score)
        if (res != 0) res else r1.player.id.compareTo(r2.player.id)
    }

    init {
        if (leadersCapacity <= 0) throw IllegalArgumentException(
            "Leaders capacity must be positive (given '$leadersCapacity')"
        )
    }

    override fun handlePlayerResultUpdate(playerResult: PlayerResult): Boolean {
        var changed = false
        synchronized(leaders) {
            if (leaders.size < leadersCapacity || playerResult.score >= minScore) {
                changed = true
                removeByPlayerId(playerResult.player.id)
                leaders.add(playerResult)
                if (leaders.size > leadersCapacity) leaders.pollLast()
                minScore = leaders.last().score
            }
        }
        if (changed) noticeAllSubscribers()
        return changed
    }

    override fun handleRemovePlayer(playerId: Long): Boolean =
        (findByPlayerId(playerId) != null) && synchronized(leaders) {
            removeByPlayerId(playerId)
            retrieveResults(leadersCapacity).lastOrNull()?.let {
                handlePlayerResultUpdate(it)
            } ?: false
        }


    override fun getLeaders(): List<PlayerResult> = synchronized(leaders) {
        leaders.toList()
    }

    override fun clear() = synchronized(leaders) {
        leaders.clear()
    }

    override fun registerSubscriber(subscriber: LeaderBoardUpdateSubscriber) {
        subscribers += subscriber
    }

    private fun noticeAllSubscribers() {
        subscribers.forEach { it.handleUpdate() }
    }

    private fun findByPlayerId(playerId: Long): PlayerResult? = leaders.find { it.player.id == playerId }

    private fun removeByPlayerId(playerId: Long): Boolean {
        val element = findByPlayerId(playerId)
        return element != null && leaders.remove(element)
    }
}