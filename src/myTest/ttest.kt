@file:Suppress("UNUSED_PARAMETER", "ConvertCallChainIntoSequence")

package myTest

import junit.framework.Assert.assertEquals
import lesson6.task1.plusMinus
import org.junit.Test
import sun.security.jgss.GSSToken.readInt
import java.io.File
import java.lang.Integer.max
import kotlin.math.sqrt

/**
Сколько времени идёт поезд?
 *
 *	В строках from и to заданы названия станций железной дороги:
 *	from -- отправления, to -- назначения, например:
 *	from = "Купчино", to = "Вырица"
 *	В строке route задано расписание движения поезда по станциям
 *	этой же железной дороги в формате вида:
 *	"С-П 09:32; Купчино 09:46; Ц. Село 10:34; Павловск 10:39;
 *	Вырица 11:21; Ор 12:10"
 *	Указано время отправления поезда с каждой станции
 *	в формате ЧЧ:ММ (ровно 5 символов) .
 *	Записи для различных станций отделены друг от друга
 *	точкой с запятой и пробелом(ами).
 *	Названия станций отделены от времени отправления пробелом (ами) .
 *
 *	Рассчитать время в пути между указанными станциями,
 *	в примере Купчино-Вырица = 11:21 - 09:46 = 1 час 35 минут.
 *
 *	Имя функции и тип результата функции предложить самостоятельно;
 *	в задании указан тип Алу, то есть результат произвольного типа,
 *	можно (и нужно) изменить тип результата.
 *
 *	При нарушении формата входной строки,
 *	бросить IllegalArgumentException
 *	При отсутствии во входной строке станции from или to,
 *	бросить IllegalArgumentException
 *
 *	Кроме функции, следует написать тесты,
 *	подтверждающие её работоспособность.
 */
//fun myFun(from: String, to: String, route: String): Any = TODO ()

fun timeBetweenStations(from: String, to: String, route: String): String {
    if (Regex("""(\s*[^\d;:][^\s:;]+)+\s\d{2}:\d{2}(;\s)?""").split(route).any { it.isNotBlank() }
    )
        throw IllegalArgumentException("wrong route")
    val timeFrom =
        Regex("(?<=${Regex.escape(from)}\\s)(\\d{2}:\\d{2})").find(route)?.value
            ?: throw IllegalArgumentException("wrong from")
    val timeTo = Regex("(?<=${Regex.escape(to)}\\s)(\\d{2}:\\d{2})").find(route)?.value
        ?: throw IllegalArgumentException("wrong to")
    val intFrom = timeFrom.substring(0..1).toInt() * 60 + timeFrom.substring(3..4).toInt()
    val intTo = timeTo.substring(0..1).toInt() * 60 + timeTo.substring(3..4).toInt()
    val rez = intTo - intFrom
    if (rez < 0) throw IllegalArgumentException("wrong station sequence")
    return "%2d:%02d".format(rez / 60, rez % 60)
}

fun SquareRoots(a: Double, b: Double, c: Double): List<Double> {
    val d: Double = b * b - 4.0 * a * c
    val rez = mutableListOf<Double>()
    return if (d < 0.0) emptyList()
    else {
        if (d == 0.0) {
            rez.add(-b / (2 * a))
            rez
        } else {
            rez.add((-b + sqrt(d)) / (2 * a))
            rez.add((-b - sqrt(d)) / (2 * a))
            rez.sorted()
        }
    }
}

fun main() {
//    println(SquareRoots(1.0, -1.0, -12.0))

    print("вводи a -> ")
    val a = readLine()?.toDouble() ?: 0.0
    println(a)
    print("вводи b -> ")
    val b = readLine()?.toDouble() ?: 0.0
    println(b)
    print("вводи c -> ")
    val c = readLine()?.toDouble() ?: 0.0
    println(c)
    println(SquareRoots(a, b, c))

//    println(
//        timeBetweenStations(
//            "С-П",
//            "Купчино",
//            "С-П 09:32; Купчино 09:46; Ц. Село 10:34; Павловск 10:39; Вырица 11:21; Ор 12:10"
//        )
//    )
}
/*
class Tests {
    @Test
    //@Tag("Example")
    fun timeBetweenStation() {
        assertEquals(
            "0 часов 00 минут", timeBetweenStations(
                "Купчино",
                "Купчино",
                "С-П 09:32; Купчино 09:46; Ц. Село 10:34; Павловск 10:39; Вырица 11:21; Ор 12:10"
            )
        )
        assertEquals(
            "0 часов 14 минут", timeBetweenStations(
                "С-П",
                "Купчино",
                "С-П 09:32; Купчино 09:46; Ц. Село 10:34; Павловск 10:39; Вырица 11:21; Ор 12:10"
            )
        )
    }

    @Test
//    @Tag("2")
    fun specialHairColor() {
        assertEquals(
            mapOf("#C0C0C1" to "Миша"),
            specialHairColor(listOf("Анна #C0C0C0", "Ваня #C0C0C0", "Миша #C0C0C1"))
        )
        assertEquals(
            mapOf<String, String>(),
            specialHairColor(listOf<String>())
        )
        assertEquals(
            mapOf("#C0C0C1" to "Миша", "#C0C0C2" to "Илья"),
            specialHairColor(listOf("Анна #C0C0C0", "Ваня #C0C0C0", "Миша #C0C0C1", "Илья #C0C0C2"))
        )
    }
    @Test
//    @Tag("2")
    fun prefixToPhone() {
        assertEquals(

            listOf("Иванов", "Петров"),
            prefixToPhone(
                listOf(
                    "4628091 Иванов",
                    "4631794 Петров",
                    "6409045 Волкова",
                    "7081356 Кошкина"
                ), "46"
            )
        )
        assertEquals(
            listOf<String>(),
            prefixToPhone(listOf<String>(), "46")
        )
        assertEquals(
            listOf("Петров", "Кошкина"),
            prefixToPhone(
                listOf(
                    "4628091 Иванов",
                    "7081356 Петров",
                    "6409045 Волкова",
                    "7081356 Кошкина"
                ), "70"
            )
        )
    }
}


/**
 * Уникальный цвет волос
 *
 *	В параметре people задан список людей с их именами
 *	и цветом волос в формате:
 *
 *	"Имя человека #С0С0С0"
 *
 *	где ' #С0С0С0' — строковое представление цвета в формате 'RGB'
 *	(3 группы по 2 цифры в шестнадцатеричной системе счисления) .
 *
 *	Необходимо найти в данном списке всех людей
 *	с уникальным цветом волос
 *	(цветом волос, которого больше нет ни у кого в данном списке)
 *	и вернуть в виде отображения "цвет волос -> имя".
 *
 *	Имя функции и тип результата функции предложить самостоятельно
 *	в задании указан тип Collection<Any>,
 *	то есть коллекция объектов произвольного типа,
 *	можно (и нужно) изменить как вид коллекции,
 *	так и тип её элементов.
 *
 *	Кроме функции следует написать тесты,
 *	подтверждающие её работоспособность.
 *
 */
fun specialHairColor(people: List<String>) =
    people.map { it.split(" ") }.groupBy { it[1] }.filter { it.value.count() == 1 }
        .map { it.key to it.value.first().first() }.toMap()

fun main2() {
    val a = specialHairColor(listOf("Анна #C0C0C0", "Ваня #C0C0C0", "Миша #C0C0C1"))
    println(a)
}

/**
 * Валентин и Настя женятся! Они отослали всем друзьям приглашения
 * на свадьбу. На каждом приглашении можно отметить +num,
 * где num - количество родственников, которые придут с
 * приглашенным человеком.
 *
 * К сожалению, Валентин и Настя не знают арабских и римских чисел,
 * и поэтому просят гостей писать количество родственников прописью.
 * При этом количество родственников не может быть больше 10.
 * Список отметок на приглашениях указан параметром marks,
 * например listOf("Даниил+два", "Катя+десять")
 *
 * Сколько же потратит молодая пара на праздничный обед, если
 * обслуживание одного человека указано параметром cost?
 *
 * Пример входных данных: listOf("Даниил+два", "Катя+десять")
 *
 * Имя функции и тип результата функции предложить
 * самостоятельно; в задании указан тип Any.
 *
 * При нарушении формата входной строки
 * бросить IllegalArgumentException
 *
 * Кроме функции, следует написать тесты,
 * подтверждающие её работоспособность.
 */
fun costParty(marks: List<String>, cost: Int): Int {
//    val numbers = listOf(
//        "ноль",
//        "один",
//        "два",
//        "три",
//        "четыре",
//        "пять",
//        "шесть",
//        "семь",
//        "восемь",
//        "девять",
//        "десять"
//    )
    val numbers = mapOf(
        "ноль" to 0,
        "один" to 1,
        "два" to 2,
        "три" to 3,
        "четыре" to 4,
        "пять" to 5,
        "шесть" to 6,
        "семь" to 7,
        "восемь" to 8,
        "девять" to 9,
        "десять" to 10
    )

    return marks.sumBy {
        if ('+' !in it) {
            1
        } else {
            val numText = it.substring(it.indexOf('+') + 1)
            if (numText !in numbers) throw IllegalArgumentException("Illegal digit \"$numText\"")
            numbers.getValue(numText) + 1
        }
    }
}

//class Tests {
//    @Test
//    //@Tag("Example")
//    fun costParty() {
//        assertEquals(14,
//            "0 часов 00 минут", costParty(listOf("Даниил+два", "Катя+десять"), 1)
//            )
//    }

fun m2ain() {
    val a = costParty(listOf("Даниил+два", "Катя+десять"), 1)
    println(a)
}

fun prefixToPhone(phones: List<String>, prefix: String): List<String> {
    val allNames = mutableListOf<String>()
    val alf = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя"
    if (prefix.contains(Regex("""\D"""))) throw IllegalArgumentException()
    for (i in phones.indices) {
        if (phones[i].split(" ")[0].contains(Regex("""\D""")) || phones[i].split(" ").size != 2) throw IllegalArgumentException()
        else {
            val names = phones[i].split(" ")[1]
            for (j in names.indices) {
                if (names[j].toLowerCase() !in alf) throw IllegalArgumentException()
            }
            if (phones[i].split(" ")[0].startsWith(prefix)) allNames.add(names)
        }
    }
    return allNames
}


@Test
//    @Tag("2")
fun prefixToPhone() {
    assertEquals(

        listOf("Иванов, Петров"),
        prefixToPhone(
            listOf(
                "4628091 Иванов",
                "4631794 Петров",
                "6409045 Волкова",
                "7081356 Кошкина"
            ), "46"
        )
    )
    assertEquals(
        listOf<String>(),
        prefixToPhone(listOf<String>(), "46")
    )
    assertEquals(
        listOf("Иванов, Петров"),
        prefixToPhone(
            listOf(
                "4628091 Иванов",
                "7081356 Петров",
                "6409045 Волкова",
                "7081356 Кошкина"
            ), "708"
        )
    )
}
*/