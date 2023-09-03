package state

import game.model.PlayerResult
import state.exception.GameStateException
import state.model.toPlayerResult
import state.repository.GameStateRepository
import state.repository.cache.InMemoryLeadersCache

class CachedGameState(
    leadersCapacity: Int,
    repository: GameStateRepository,
) : GameState(leadersCapacity, repository) {

    private val cache = InMemoryLeadersCache(leadersCapacity) { limit ->
        repository.getLeaders(limit)
    }

    override fun getLeaders(): List<PlayerResult> = cache.getLeaders()

    override fun removePlayer(playerId: Long): Boolean {
        val res = super.removePlayer(playerId)
        cache.handleRemovePlayer(playerId)
        return res
    }

    override fun clear() {
        super.clear()
        cache.clear()
    }

    override fun addWordToPlayer(playerId: Long, word: String): Long = repository.getPlayerStateById(playerId)?.run {
        addWord(word, word.length).also { cache.handlePlayerResultUpdate(toPlayerResult()) }
    } ?: throw GameStateException(
        "Player with id '$playerId' doesn't exist"
    )

    override fun registerSubscriber(subscriber: LeaderBoardUpdateSubscriber) {
        cache.registerSubscriber(subscriber)
    }

}