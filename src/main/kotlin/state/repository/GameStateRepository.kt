package state.repository

import game.model.Player
import game.model.PlayerResult
import state.model.PlayerState

/**
 * Интерфейс хранилища (DAO) данных игры
 * Реализация может в том числе являться связанным БД репозиторием
 *
 * Ничего не знает о правилах игры и тд, просто предоставляет доступ к данным
 */
interface GameStateRepository {

    fun getAllPlayers(): List<Player>

    /**
     * @return [limit] игроков с наибольшим колчеством очков
     */
    fun getLeaders(limit: Int): List<PlayerResult>

    fun getPlayerResultById(id: Long): PlayerResult?

    fun getPlayerStateById(id: Long): PlayerState?

    /**
     * @return true если ранее такой игрок не был зарегистрирован
     */
    fun registerPlayer(player: Player): Boolean

    /**
     * @return true если ранее такой игрок был зарегистрирован
     */
    fun removePlayer(playerId: Long): Boolean

    fun clear()
}