package game

/**
 * Интерфейс с логикой проверки введенного палиндрома на корректность
 */
interface PalindromeChecker {

    fun checkWord(word: String): Boolean
}