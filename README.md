# palindrome-game

### Запуск

`Main` отсутствует, можно запустить тесты:

```shell
./gradlew test
```

### Компоненты

К большинству классов оставлены комментарии

Общая система компонентов разбита на три слоя:

1. #### Palindrome Game

Внешний интерфейс игры. Ничего не знает про то как хранятся данные, 
про таблицу лидеров и использованные игроками слова,
не знает про стоимость слов. Должен уметь проверять палиндром на корректность

Делегирует задачи по управлению игрой к `GameState`

2. #### GameState

Управляет непосредственно игрой. Тоже ничего не знает о способе хренения данных.
Знает о "правилах игры", о стоимости слов (в нашем случае стоимость равна количеству символов)

Делегирует хранение данных к `GameStateRepository`

3. #### GameStateRepository

Отвечает за хранение данных. Сейчас реализовано in-memory, можно заменить на
работу с БД
