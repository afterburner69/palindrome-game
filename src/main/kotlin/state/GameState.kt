package state

import game.model.Player
import state.repository.GameStateRepository

/**
 * Абстрактный класс, являющийся менеджером игрового состояния
 * Не хранит данные, делегирует это репозиторию
 *
 * Не проверяет слова на корректность, знает только о правилах начисления очков
 */
abstract class GameState(
    protected val leadersCapacity: Int,
    protected val repository: GameStateRepository,
) : LeaderBoardUpdatePublisher {

    open fun getAllPlayers() = repository.getAllPlayers()

    open fun getLeaders() = repository.getLeaders(leadersCapacity)

    open fun getPlayerResultById(id: Long) = repository.getPlayerResultById(id)

    open fun registerPlayer(player: Player) = repository.registerPlayer(player)

    open fun removePlayer(playerId: Long) = repository.removePlayer(playerId)

    open fun clear() = repository.clear()

    abstract fun addWordToPlayer(playerId: Long, word: String): Long
}