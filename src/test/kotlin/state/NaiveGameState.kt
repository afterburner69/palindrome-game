package state

import state.exception.GameStateException
import state.repository.GameStateRepository

/**
 * Простая реализация, предназначенная для тестирования реализации с кешированием
 */
class NaiveGameState(
    leadersCapacity: Int,
    repository: GameStateRepository,
) : GameState(leadersCapacity, repository) {

    override fun addWordToPlayer(playerId: Long, word: String): Long = repository.getPlayerStateById(playerId)?.run {
        addWord(word, word.length)
    } ?: throw GameStateException(
        "Player with id '$playerId' doesn't exist"
    )

    override fun registerSubscriber(subscriber: LeaderBoardUpdateSubscriber) {
        TODO("Not yet implemented")
    }
}