package game

/**
 * Наивная проверка за O(n), не чувствительна к регистру, чувствительна к пробелам
 *
 * Правильно:
 * * топот
 * * а Роза упала на лапу Азора
 *
 * Неправильно:
 * * улыбок тебе дед Макар
 * * а Розаупала на лапу Азора
 */
class BasicPalindromeChecker: PalindromeChecker {

    override fun checkWord(word: String): Boolean {
        return word.lowercase() == word.reversed().lowercase()
    }
}