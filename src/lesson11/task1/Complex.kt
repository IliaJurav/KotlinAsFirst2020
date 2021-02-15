@file:Suppress("UNUSED_PARAMETER")

package lesson11.task1

import lesson1.task1.sqr

/**
 * Класс "комплексное число".
 *
 * Общая сложность задания -- лёгкая, общая ценность в баллах -- 8.
 * Объект класса -- комплексное число вида x+yi.
 * Про принципы работы с комплексными числами см. статью Википедии "Комплексное число".
 *
 * Аргументы конструктора -- вещественная и мнимая часть числа.
 */
class Complex(val re: Double, val im: Double) {

    /**
     * Конструктор из вещественного числа
     */
    constructor(x: Double) : this(x, 0.0)

    /**
     * Конструктор из строки вида x+yi
     */
    companion object {
        operator fun invoke(s: String): Complex {
            var dig = 0
            var comma = 0
            var sign = 1
            var data = false
            var imNew = 0.0
            var reNew = 0.0
            for (ch in s) {
                when (ch) {
                    in '0'..'9' -> {
                        dig = dig * 10 + ch.toInt() - 48; comma *= 10; data = true
                    }
                    '.' -> if (comma == 0) comma = 1
                    else throw IllegalArgumentException("Syntax error")
                    'i' -> {
                        imNew =
                            if (data) (sign * dig).toDouble() /
                                    if (comma == 0) 1.0 else comma.toDouble()
                            else sign.toDouble(); return Complex(reNew, imNew)
                    }
                    '+', '-' -> {
                        if (data) {
                            reNew =
                                (sign * dig).toDouble() / if (comma == 0) 1.0 else comma.toDouble()
                            comma = 0
                            dig = 0
                            data = false
                        }
                        sign = if (ch == '-') -1 else 1
                    }
                    else -> throw IllegalArgumentException("Syntax error")
                }
            }
            if (!data) throw IllegalArgumentException("Syntax error")
            reNew =
                (sign * dig).toDouble() / if (comma == 0) 1.0 else comma.toDouble()
            return Complex(reNew, imNew)
        }
    }

    /**
     * Сложение.
     */
    operator fun plus(other: Complex) = Complex(re + other.re, im + other.im)

    /**
     * Смена знака (у обеих частей числа)
     */
    operator fun unaryMinus() = Complex(-re, -im)

    /**
     * Вычитание
     */
    operator fun minus(other: Complex) = Complex(re - other.re, im - other.im)

    /**
     * Умножение (ac – bd) + (ad + bc)i
     */
    operator fun times(other: Complex) =
        Complex(re * other.re - im * other.im, im * other.re + other.im * re)

    /**
     * Деление
     */
    operator fun div(other: Complex): Complex {
        val cd = sqr(other.re) + sqr(other.im)
        if (cd == 0.0) throw IllegalArgumentException("divide by zero")
        return Complex(
            (re * other.re + im * other.im) / cd,
            (im * other.re - other.im * re) / cd
        )
    }

    /**
     * Сравнение на равенство
     */
    override fun equals(other: Any?): Boolean {
        if (other is Complex) return re == other.re && im == other.im
        if (other is Double) return re == other && im == 0.0
        if (other is Int) return re == other.toDouble() && im == 0.0
        throw IllegalArgumentException("Incompatible types")
    }

    /**
     * Преобразование в строку
     */
    override fun toString(): String = "$re" + if (im < 0.0) "$im" + "i" else "+$im" + "i"


    override fun hashCode(): Int {
        var result = re.hashCode()
        result = 31 * result + im.hashCode()
        return result
    }
}
