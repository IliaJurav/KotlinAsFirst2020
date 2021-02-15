@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import java.lang.IllegalArgumentException
import java.lang.Integer.max
import java.lang.StrictMath.abs

/**
 * Класс "вещественное число с фиксированной точкой"
 *
 * Общая сложность задания - сложная, общая ценность в баллах -- 20.
 * Объект класса - вещественное число с заданным числом десятичных цифр после запятой (precision, точность).
 * Например, для ограничения в три знака это может быть число 1.234 или -987654.321.
 * Числа можно складывать, вычитать, умножать, делить
 * (при этом точность результата выбирается как наибольшая точность аргументов),
 * а также сравнить на равенство и больше/меньше, преобразовывать в строку и тип Double.
 *
 * Вы можете сами выбрать, как хранить число в памяти
 * (в виде строки, целого числа, двух целых чисел и т.д.).
 * Представление числа должно позволять хранить числа с общим числом десятичных цифр не менее 9.
 */
class FixedPointNumber(private val integer: Int, private val fractional: Int) :
    Comparable<FixedPointNumber> {

    /**
     * Точность - число десятичных цифр после запятой.
     */
    val precision: Int
        get() : Int {
            var k = 0
            var d = fractional
            while (d > 1) {
                d /= 10
                k++
            }
            return k
        }

    /**
     * Вспомогательная статическая функция вызываемая из конструктора
     */
    companion object {
        private fun pow10(p: Int): Int {
            var k = 1
            for (i in 1..p) k *= 10
            return k
        }
    }

    /**
     * Конструктор из строки, точность выбирается в соответствии
     * с числом цифр после десятичной точки.
     * Если строка некорректна или цифр слишком много,
     * бросить NumberFormatException.
     *
     * Внимание: этот или другой конструктор можно сделать основным
     */
    constructor(s: String) : this(s.toDouble(), if ('.' in s) s.length - s.indexOf('.') - 1 else 0)

    /**
     * Конструктор из вещественного числа с заданной точностью
     */
    constructor(d: Double, p: Int) : this(
        ((d * pow10(p + 1)).toInt() + if (d >= 0.0) 5 else -5) / 10,
        pow10(p)
    )

    /**
     * Конструктор из целого числа (предполагает нулевую точность)
     */
    constructor(i: Int) : this(i, 1)

    /**
     * Сложение.
     *
     * Здесь и в других бинарных операциях
     * точность результата выбирается как наибольшая точность аргументов.
     * Лишние знаки отрбрасываются, число округляется по правилам арифметики.
     */
    operator fun plus(other: FixedPointNumber) = if (fractional >= other.fractional)
        FixedPointNumber(
            integer + other.integer * (fractional / other.fractional), fractional
        )
    else
        FixedPointNumber(
            other.integer + integer * (other.fractional / fractional),
            other.fractional
        )


    /**
     * Смена знака
     */
    operator fun unaryMinus() = FixedPointNumber(-integer, fractional)

    /**
     * Вычитание
     */
    operator fun minus(other: FixedPointNumber) = if (fractional >= other.fractional)
        FixedPointNumber(
            integer - other.integer * (fractional / other.fractional),
            fractional
        )
    else
        FixedPointNumber(
            integer * (other.fractional / fractional) - other.integer,
            other.fractional
        )

    /**
     * Умножение
     */
    operator fun times(other: FixedPointNumber): FixedPointNumber {
        val pMax = max(fractional, other.fractional)
        val pMin = fractional.coerceAtMost(other.fractional)
        return FixedPointNumber((other.integer * integer + pMin / 2) / pMin, pMax)
    }

    /**
     * Деление
     */
    operator fun div(other: FixedPointNumber): FixedPointNumber {
        if (integer == 0) throw IllegalArgumentException(" div by 0")
        if (fractional >= other.fractional)
            return FixedPointNumber(
                ((integer * other.fractional * 10) / other.integer + 5) / 10,
                fractional
            )
        else
            return FixedPointNumber(
                (((integer * other.fractional * 10) *
                        (other.fractional / fractional)) / other.integer + 5) / 10,
                other.fractional
            )
    }

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?) =
        when {
            this === other -> true
            other is FixedPointNumber -> compareTo(other) == 0
            else -> false
        }

    /**
     * Сравнение на больше/меньше
     */
    override fun compareTo(other: FixedPointNumber): Int {
        val diff = this - other
        return diff.integer
    }

    /**
     * Преобразование в строку
     */
    override fun toString(): String =
        "${integer / fractional}" + if (fractional == 1) "" else String.format(
            ".%0${precision}d",
            abs(integer) % fractional
        )

    /**
     * Преобразование к вещественному числу
     */
    fun toDouble(): Double = integer.toDouble() / fractional.toDouble()

    override fun hashCode(): Int {
        var result = integer.hashCode()
        result = 31 * result + fractional.hashCode()
        return result
    }

}


