@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package lesson6.task1

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder

// Урок 6: разбор строк, исключения
// Максимальное количество баллов = 13
// Рекомендуемое количество баллов = 11
// Вместе с предыдущими уроками (пять лучших, 2-6) = 40/54

/**
 * Пример
 *
 * Время представлено строкой вида "11:34:45", содержащей часы, минуты и секунды, разделённые двоеточием.
 * Разобрать эту строку и рассчитать количество секунд, прошедшее с начала дня.
 */
fun timeStrToSeconds(str: String): Int {
    val parts = str.split(":")
    var result = 0
    for (part in parts) {
        val number = part.toInt()
        result = result * 60 + number
    }
    return result
}

/**
 * Пример
 *
 * Дано число n от 0 до 99.
 * Вернуть его же в виде двухсимвольной строки, от "00" до "99"
 */
fun twoDigitStr(n: Int) = if (n in 0..9) "0$n" else "$n"

/**
 * Пример
 *
 * Дано seconds -- время в секундах, прошедшее с начала дня.
 * Вернуть текущее время в виде строки в формате "ЧЧ:ММ:СС".
 */
fun timeSecondsToStr(seconds: Int): String {
    val hour = seconds / 3600
    val minute = (seconds % 3600) / 60
    val second = seconds % 60
    return String.format("%02d:%02d:%02d", hour, minute, second)
}

/**
 * Пример: консольный ввод
 */
fun main() {
    println("Введите время в формате ЧЧ:ММ:СС")
    val line = readLine()
    if (line != null) {
        val seconds = timeStrToSeconds(line)
        if (seconds == -1) {
            println("Введённая строка $line не соответствует формату ЧЧ:ММ:СС")
        } else {
            println("Прошло секунд с начала суток: $seconds")
        }
    } else {
        println("Достигнут <конец файла> в процессе чтения строки. Программа прервана")
    }
}

/**
 * Средняя (4 балла)
 *
 * Дата представлена строкой вида "15 июля 2016".
 * Перевести её в цифровой формат "15.07.2016".
 * День и месяц всегда представлять двумя цифрами, например: 03.04.2011.
 * При неверном формате входной строки вернуть пустую строку.
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30.02.2009) считается неверными
 * входными данными.
 */
fun dateStrToDigit(str: String): String {
    val m = listOf(
        "января", "февраля", "марта", "апреля", "мая",
        "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"
    )
    if (!Regex("""(^(?=\d)(\d+\s\S+\s\d+)${'$'}(?<=\d))""").containsMatchIn(str)) return ""
    val a = str.split(" ")
    val year = a[2].toInt()
    val day = a[0].toInt()
    val month = m.indexOf(a[1]) + 1
    if (month == 0 ||
        year !in 1700..2200 ||
        day !in 1..LocalDate.of(year, month, 1).lengthOfMonth()
    ) return ""
    return "%02d.%02d.%d".format(day, month, year)
}

/**
 * Средняя (4 балла)
 *
 * Дата представлена строкой вида "15.07.2016".
 * Перевести её в строковый формат вида "15 июля 2016".
 * При неверном формате входной строки вернуть пустую строку
 *
 * Обратите внимание: некорректная с точки зрения календаря дата (например, 30 февраля 2009) считается неверными
 * входными данными.
 */
fun dateDigitToStr(digital: String): String {
    val m = listOf(
        "января", "февраля", "марта", "апреля", "мая",
        "июня", "июля", "августа", "сентября", "октября", "ноября", "декабря"
    )
    if (!Regex("""(^(?=\d)(\d+\.\d+\.\d+)${'$'}(?<=\d))""").containsMatchIn(digital)) return ""
    val a = digital.split(".")
    val year = a[2].toInt()
    val day = a[0].toInt()
    val month = a[1].toInt()
    if (month !in 1..12 ||
        year !in 1700..2200 ||
        day !in 1..LocalDate.of(year, month, 1).lengthOfMonth()
    ) return ""
    return "%d %s %d".format(day, m[month - 1], year)
}

/**
 * Средняя (4 балла)
 *
 * Номер телефона задан строкой вида "+7 (921) 123-45-67".
 * Префикс (+7) может отсутствовать, код города (в скобках) также может отсутствовать.
 * Может присутствовать неограниченное количество пробелов и чёрточек,
 * например, номер 12 --  34- 5 -- 67 -89 тоже следует считать легальным.
 * Перевести номер в формат без скобок, пробелов и чёрточек (но с +), например,
 * "+79211234567" или "123456789" для приведённых примеров.
 * Все символы в номере, кроме цифр, пробелов и +-(), считать недопустимыми.
 * При неверном формате вернуть пустую строку.
 *
 * PS: Дополнительные примеры работы функции можно посмотреть в соответствующих тестах.
 */
fun flattenPhoneNumber(phone: String) =
    if (Regex("""(\(\D*\))|[^\d\+\-\s\(\)]|(\).*[\(\)])|(\(.*\+)|(\S+.*\+)""").containsMatchIn(phone)) ""
    else Regex("""[^\d\+]""").replace(phone, "")

/**
 * Средняя (5 баллов)
 *
 * Результаты спортсмена на соревнованиях в прыжках в длину представлены строкой вида
 * "706 - % 717 % 703".
 * В строке могут присутствовать числа, черточки - и знаки процента %, разделённые пробелами;
 * число соответствует удачному прыжку, - пропущенной попытке, % заступу.
 * Прочитать строку и вернуть максимальное присутствующее в ней число (717 в примере).
 * При нарушении формата входной строки или при отсутствии в ней чисел, вернуть -1.
 */
fun bestLongJump(jumps: String): Int {
    if (Regex("""[^\d%\-\s]""").containsMatchIn(jumps)) return -1
    return jumps.splitToSequence(" ").toList().filter { it.toIntOrNull() != null }
        .map { it.toInt() }.maxOrNull() ?: -1
}


/**
 * Сложная (6 баллов)
 *
 * Результаты спортсмена на соревнованиях в прыжках в высоту представлены строкой вида
 * "220 + 224 %+ 228 %- 230 + 232 %%- 234 %".
 * Здесь + соответствует удачной попытке, % неудачной, - пропущенной.
 * Высота и соответствующие ей попытки разделяются пробелом.
 * Прочитать строку и вернуть максимальную взятую высоту (230 в примере).
 * При нарушении формата входной строки, а также в случае отсутствия удачных попыток,
 * вернуть -1.
 */
fun bestHighJump(jumps: String): Int {
    if (Regex("""[^\d%\-\+\s]|(\d+\s+\d)|([\+\-]\D*[\+\-])""").containsMatchIn(jumps)) return -1
    return Regex("""\d+(?=[\s\%]*\+)""").findAll(jumps).map { it.value.toInt() }.maxOrNull() ?: -1
}

/**
 * Сложная (6 баллов)
 *
 * В строке представлено выражение вида "2 + 31 - 40 + 13",
 * использующее целые положительные числа, плюсы и минусы, разделённые пробелами.
 * Наличие двух знаков подряд "13 + + 10" или двух чисел подряд "1 2" не допускается.
 * Вернуть значение выражения (6 для примера).
 * Про нарушении формата входной строки бросить исключение IllegalArgumentException
 */
fun plusMinus(expression: String): Int {
    if (Regex("""(^\+)|[^\d\-\+\s]|(\d+\s+\d)|([\+\-]\D*[\+\-])|${'$'}(?<=[\-\+])""").containsMatchIn(
            expression.trim()
        ) || expression == ""
    )
        throw IllegalArgumentException("Error")

    val s = Regex("""\s+""").replace(expression, "")
    return Regex("""\-?\d+""").findAll(s).map { it.value.toInt() }.sum()
}

/**
 * Сложная (6 баллов)
 *
 * Строка состоит из набора слов, отделённых друг от друга одним пробелом.
 * Определить, имеются ли в строке повторяющиеся слова, идущие друг за другом.
 * Слова, отличающиеся только регистром, считать совпадающими.
 * Вернуть индекс начала первого повторяющегося слова, или -1, если повторов нет.
 * Пример: "Он пошёл в в школу" => результат 9 (индекс первого 'в')
 */
fun firstDuplicateIndex(str: String) =
    Regex("""(\S+)\s\1""", RegexOption.IGNORE_CASE).find(str)?.range?.first ?: -1


/**
 * Сложная (6 баллов)
 *
 * Строка содержит названия товаров и цены на них в формате вида
 * "Хлеб 39.9; Молоко 62; Курица 184.0; Конфеты 89.9".
 * То есть, название товара отделено от цены пробелом,
 * а цена отделена от названия следующего товара точкой с запятой и пробелом.
 * Вернуть название самого дорогого товара в списке (в примере это Курица),
 * или пустую строку при нарушении формата строки.
 * Все цены должны быть больше нуля либо равны нулю.
 */
fun mostExpensive(description: String): String {
    if (description == "") return ""
    if (Regex("""\S+\s\d+\.?\d*;?\s?""").replace(description, "") != "") return ""
    return description.split("; ")
        .map { with(it.split(" ")) { first() to last().toDouble() } }
        .maxByOrNull { it.second }?.first ?: ""

}

/**
 * Сложная (6 баллов)
 *
 * Перевести число roman, заданное в римской системе счисления,
 * в десятичную систему и вернуть как результат.
 * Римские цифры: 1 = I, 4 = IV, 5 = V, 9 = IX, 10 = X, 40 = XL, 50 = L,
 * 90 = XC, 100 = C, 400 = CD, 500 = D, 900 = CM, 1000 = M.
 * Например: XXIII = 23, XLIV = 44, C = 100
 * CDLXXXVI 486
 * Вернуть -1, если roman не является корректным римским числом
 */
fun fromRoman(roman: String): Int {
    if (!Regex("""(^(?=[MDCLXVI])M*(C[MD]|D?C{0,3})(X[CL]|L?X{0,3})(I[XV]|V?I{0,3})$(?<=[MDCLXVI]))""")
            .matches(roman)
    ) return -1
    val digValue = mapOf(
        "I" to 1, "IV" to 4, "V" to 5, "IX" to 9, "X" to 10, "XL" to 40, "L" to 50,
        "XC" to 90, "C" to 100, "CD" to 400, "D" to 500, "CM" to 900, "M" to 1000
    )
    return Regex("C[M|D]|I[V|X]|X[L|C]|[MDCLXVI]").findAll(roman)
        .sumOf { digValue.getValue(it.value) }
}

/**
 * Очень сложная (7 баллов)
 *
 * Имеется специальное устройство, представляющее собой
 * конвейер из cells ячеек (нумеруются от 0 до cells - 1 слева направо) и датчик, двигающийся над этим конвейером.
 * Строка commands содержит последовательность команд, выполняемых данным устройством, например +>+>+>+>+
 * Каждая команда кодируется одним специальным символом:
 *	> - сдвиг датчика вправо на 1 ячейку;
 *  < - сдвиг датчика влево на 1 ячейку;
 *	+ - увеличение значения в ячейке под датчиком на 1 ед.;
 *	- - уменьшение значения в ячейке под датчиком на 1 ед.;
 *	[ - если значение под датчиком равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей следующей командой ']' (с учётом вложенности);
 *	] - если значение под датчиком не равно 0, в качестве следующей команды следует воспринимать
 *  	не следующую по порядку, а идущую за соответствующей предыдущей командой '[' (с учётом вложенности);
 *      (комбинация [] имитирует цикл)
 *  пробел - пустая команда
 *
 * Изначально все ячейки заполнены значением 0 и датчик стоит на ячейке с номером N/2 (округлять вниз)
 *
 * После выполнения limit команд или всех команд из commands следует прекратить выполнение последовательности команд.
 * Учитываются все команды, в том числе несостоявшиеся переходы ("[" при значении под датчиком не равном 0 и "]" при
 * значении под датчиком равном 0) и пробелы.
 *
 * Вернуть список размера cells, содержащий элементы ячеек устройства после завершения выполнения последовательности.
 * Например, для 10 ячеек и командной строки +>+>+>+>+ результат должен быть 0,0,0,0,0,1,1,1,1,1
 *
 * Все прочие символы следует считать ошибочными и формировать исключение IllegalArgumentException.
 * То же исключение формируется, если у символов [ ] не оказывается пары.
 * Выход за границу конвейера также следует считать ошибкой и формировать исключение IllegalStateException.
 * Считать, что ошибочные символы и непарные скобки являются более приоритетной ошибкой чем выход за границу ленты,
 * то есть если в программе присутствует некорректный символ или непарная скобка, то должно быть выброшено
 * IllegalArgumentException.
 * IllegalArgumentException должен бросаться даже если ошибочная команда не была достигнута в ходе выполнения.
 *
 */
fun computeDeviceCells(cells: Int, commands: String, limit: Int): List<Int> {
    // проверка на неправильные команды
    if (Regex("""[^>+<\-\[\]\s]""").containsMatchIn(commands)) throw IllegalArgumentException("Unknown commands")
    // проверка на неправильное количество скобок
    with(Regex("""[\[\]]""").findAll(commands))
    {
        if (fold(0,
                { acc, e ->
                    if (acc == 0 && e.value == "]") throw IllegalArgumentException("Missing opening bracket")
                    acc + if (e.value == "[") 1 else -1
                }) != 0
        )
            throw IllegalArgumentException("Missing closing bracket")
    }
    // инициализируем переменные
    val lst = generateSequence { 0 }.take(cells).toMutableList()
    var pntLst = cells / 2
    var pntCmd = 0
    // запускаем программу
    for (countCmd in 1..limit) {
        when (commands[pntCmd]) {
            '>' -> if (++pntLst !in lst.indices) throw IllegalStateException("Conveyor pointer out of max bound")
            '<' -> if (--pntLst !in lst.indices) throw IllegalStateException("Conveyor pointer out of min bound")
            '+' -> lst[pntLst]++
            '-' -> lst[pntLst]--
            '[' -> if (lst[pntLst] == 0) {
                var lvl = 0
                while (++pntCmd in commands.indices) {
                    when (commands[pntCmd]) {
                        '[' -> lvl++
                        ']' -> if (lvl == 0) break else lvl--
                    }
                }
            }
            ']' -> if (lst[pntLst] != 0) {
                var lvl = 0
                while (--pntCmd in commands.indices) {
                    when (commands[pntCmd]) {
                        ']' -> lvl++
                        '[' -> if (lvl == 0) break else lvl--
                    }
                }
            }
        }
        if (++pntCmd > commands.lastIndex) break // конец
    }
    return lst
}
