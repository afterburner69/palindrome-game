package state

/**
 * Издатель, триггерящийся на изменения таблицы лидеров
 *
 * Может понадобиться, чтобы уведомлять клиентскую часть о том, что пора забрать новую таблицу с сервера
 */
interface LeaderBoardUpdatePublisher {

    fun registerSubscriber(subscriber: LeaderBoardUpdateSubscriber)
}