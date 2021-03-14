@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import java.lang.IllegalArgumentException
import java.lang.Integer.*
import java.lang.StrictMath.abs
import java.lang.StrictMath.pow
import kotlin.math.roundToInt

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
class FixedPointNumber(private val integer: Int, val precision: Int) :
    Comparable<FixedPointNumber> {

    private val frac10 = pow10(precision)

    /**
     * Точность - число десятичных цифр после запятой.
     */
    //val precision = fractional

    /**
     * Вспомогательная статическая функция вызываемая из конструктора
     */
    companion object {
        private fun pow10(p: Int): Int {
            if(p !in 0..9) throw IllegalArgumentException("Overflow")
            var k = 1
            for (i in 1..p) k *= 10
            return k
        }
    }

    private fun toPrec(newPrec:Int):Long{
        if (newPrec == precision) return integer.toLong()
        if(newPrec>9 || newPrec<0) throw IllegalArgumentException("Overflow")
        if (newPrec < precision) return (integer / pow10(precision-newPrec)).toLong()
        val newInt = integer.toLong()*pow10(newPrec-precision)
        if (abs(newInt)> 1_000_000_000) throw IllegalArgumentException("Overflow")
        return newInt
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
        with((d * pow10(p))) {
            if (abs(this) > (MAX_VALUE-1)) throw IllegalArgumentException("Very big")
            else this.roundToInt()
        },
        p
    )

    constructor(d: Long, p: Int) : this(
            if (abs(d) > 1_000_000_000) throw IllegalArgumentException("Very big")
            else d.toInt(),
        p
    )
    /**
     * Конструктор из целого числа (предполагает нулевую точность)
     */
    constructor(i: Int) : this(i, 0)

    /**
     * Сложение.
     *
     * Здесь и в других бинарных операциях
     * точность результата выбирается как наибольшая точность аргументов.
     * Лишние знаки отбрасываются, число округляется по правилам арифметики.
     */
    operator fun plus(other: FixedPointNumber) =
        with(max(precision, other.precision)){
            FixedPointNumber(
                toPrec(this) + other.toPrec(this),
                this)
        }

    /**
     * Смена знака
     */
    operator fun unaryMinus() = FixedPointNumber(-integer, precision)

    /**
     * Вычитание
     */
    operator fun minus(other: FixedPointNumber) =
        with(max(precision, other.precision)){
            FixedPointNumber(
                toPrec(this) - other.toPrec(this),
                this)
        }

    /**
     * Умножение
     */
    operator fun times(other: FixedPointNumber) =
        with(pow10(min(precision, other.precision))){
        FixedPointNumber(
            (integer.toLong() * other.integer.toLong() + this / 2)
                    /this,
            max(precision, other.precision)
        )}

    /**
     * Деление
     */
    operator fun div(other: FixedPointNumber): FixedPointNumber {
        if (integer == 0) throw IllegalArgumentException(" div by 0")
        return with(max(precision, other.precision)){
            FixedPointNumber(
                toPrec(this + other.precision) / other.integer.toLong(),
                this)
        }
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
        "${integer / frac10}" + if (precision == 0) "" else String.format(
            ".%0${precision}d",
            abs(integer) % frac10
        )

    /**
     * Преобразование к вещественному числу
     */
    fun toDouble(): Double = integer.toDouble() / frac10.toDouble()

    override fun hashCode(): Int {
        var result = integer.hashCode()
        result = 31 * result + precision.hashCode()
        return result
    }

}

