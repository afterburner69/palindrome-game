package state

/**
 * Подписчик на событие обновления таблицы лидеров
 *
 * @see LeaderBoardUpdatePublisher
 */
interface LeaderBoardUpdateSubscriber {

    fun handleUpdate()
}