package state.repository.cache

import game.model.PlayerResult
import state.LeaderBoardUpdatePublisher
import state.LeaderBoardUpdateSubscriber

/**
 * Интерфейс кешированной таблицы лидеров
 * Хранит снапшот таблицы лидеров и реагирует на изменения в очках и составе игроков
 */
interface LeadersCache : LeaderBoardUpdatePublisher {

    /**
     * @return true если кеш обновился и триггерит [LeaderBoardUpdateSubscriber.handleUpdate]
     */
    fun handlePlayerResultUpdate(playerResult: PlayerResult): Boolean

    /**
     * @return true если игрок был в кеше и триггерит [LeaderBoardUpdateSubscriber.handleUpdate]
     */
    fun handleRemovePlayer(playerId: Long): Boolean

    fun getLeaders(): List<PlayerResult>

    fun clear()
}