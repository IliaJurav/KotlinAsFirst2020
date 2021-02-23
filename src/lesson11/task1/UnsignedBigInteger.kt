package lesson11.task1

import kotlin.math.max

/**
 * Класс "беззнаковое большое целое число".
 *
 * Общая сложность задания -- очень сложная, общая ценность в баллах -- 32.
 * Объект класса содержит целое число без знака произвольного размера
 * и поддерживает основные операции над такими числами, а именно:
 * сложение, вычитание (при вычитании большего числа из меньшего бросается исключение),
 * умножение, деление, остаток от деления,
 * преобразование в строку/из строки, преобразование в целое/из целого,
 * сравнение на равенство и неравенство
 */
class UnsignedBigInteger : Comparable<UnsignedBigInteger> {
    private val digit: ArrayList<Int> = ArrayList()

    /**
     * Конструктор из строки
     */
    constructor(s: String) {
        if (!Regex("""\d+""").matches(s)) throw IllegalArgumentException("Illegal char")
        s.reversed().mapTo(digit) { it - '0' }
    }

    /**
     * Конструктор из целого
     */
    constructor(i: Int) {
        if (i < 0) throw IllegalArgumentException("Illegal value")
        digit.add(i)
        normalize()
    }

    /**
     * Конструктор из UnsignedBigInteger
     */
    constructor(k: UnsignedBigInteger?) {
        if (k is UnsignedBigInteger) digit.addAll(k.digit)
    }

    /**
     * Нормализация представления -> каждый элемент in 0..9
     */
    private fun normalize(): Boolean {
        var i = 0
        var over = 0
        do {
            over += digit[i]
            val rem = over % 10
            over /= 10
            if (rem < 0) {
                over--
                digit[i] = rem + 10
            } else {
                digit[i] = rem
            }
            if (i == digit.lastIndex)
                if (over > 0) digit.add(0)
                else if (over < 0) throw ArithmeticException("Error")

            i++
        } while (i < digit.size)
        return true
    }

    /**
     * Удаление лидирующих незначащих нулей
     */
    private fun trimToSize() {
        for (i in digit.lastIndex downTo 1)
            if (digit[i] == 0) digit.removeAt(i)
            else break
    }

    /**
     * Сложение
     */
    operator fun plus(other: UnsignedBigInteger): UnsignedBigInteger {
        val f = UnsignedBigInteger(null)
        val g = max(digit.size, other.digit.size)
        for (i in 0 until g) f.digit.add(
            (if (i < digit.size) digit[i] else 0) +
                    (if (i < other.digit.size) other.digit[i] else 0)
        )
        f.normalize()
        return f
    }

    /**
     * Вычитание (бросить ArithmeticException, если this < other)
     */
    operator fun minus(other: UnsignedBigInteger): UnsignedBigInteger {
        val f = UnsignedBigInteger(null)
        val g = digit.size
        for (i in 0 until g) f.digit.add(
            digit[i] - if (i < other.digit.size) other.digit[i] else 0
        )
        f.normalize()
        f.trimToSize()
        return f
    }

    /**
     * Умножение
     */
    operator fun times(other: UnsignedBigInteger): UnsignedBigInteger {
        val f = UnsignedBigInteger(null)
        for (i in 2..digit.size + other.digit.size)
            f.digit.add(0)
        for (i in 0..digit.lastIndex)
            for (j in 0..other.digit.lastIndex)
                f.digit[i + j] += digit[i] * other.digit[j]
        f.normalize()
        return f
    }

    /**
     * Деление и остаток
     */
    private fun divMod(other: UnsignedBigInteger): Pair<UnsignedBigInteger, UnsignedBigInteger> {
        if (other.digit[0] == 0 && other.digit.lastIndex == 0) throw ArithmeticException("divide by zero")
        when (compareTo(other)) {
            -1 -> return Pair(UnsignedBigInteger(0), UnsignedBigInteger(this))
            0 -> return Pair(UnsignedBigInteger(1), UnsignedBigInteger(0))
        }
        val rem = UnsignedBigInteger(this)
        val rez = UnsignedBigInteger(null)
        for (r in rem.digit.size - other.digit.size downTo 0) {
            rez.digit.add(0, 0)
            var a = rem.digit[other.digit.lastIndex + r]
            if (rem.digit.lastIndex > other.digit.lastIndex + r) {
                a += rem.digit[other.digit.lastIndex + r + 1] * 10
                rem.digit[other.digit.lastIndex + r + 1] = 0
                rem.digit[other.digit.lastIndex + r] = a
            }
            a /= other.digit[other.digit.lastIndex]
            if (a == 0) continue
            rez.digit[0] = a
            var d = 0
            for (i in 0..other.digit.lastIndex) {
                d -= other.digit[i] * a
                d += rem.digit[i + r]
                if (d >= 0) {
                    rem.digit[i + r] = d
                    d = 0
                    continue
                }
                val w = d % 10
                d /= 10
                if (w < 0) {
                    d--
                    rem.digit[i + r] = w + 10
                } else
                    rem.digit[i + r] = 0
            }
            if (d == 0) continue
            rem.digit[other.digit.lastIndex + r] += 10 * d
            while (rem.digit[other.digit.lastIndex + r] < 0) {
                rez.digit[0]--
                d = 0
                for (i in 0..other.digit.lastIndex) {
                    d += other.digit[i] + rem.digit[i + r]
                    rem.digit[i + r] = d % 10
                    d /= 10
                }
                if (d != 0) rem.digit[other.digit.lastIndex + r] += d * 10
            }
        }
        rez.trimToSize()
        rem.trimToSize()
        return Pair(rez, rem)
    }

    /**
     * Деление
     */
    operator fun div(other: UnsignedBigInteger) = divMod(other).first

    /**
     * Взятие остатка
     */
    operator fun rem(other: UnsignedBigInteger) = divMod(other).second

    /**
     * Сравнение на равенство (по контракту Any.equals)
     */
    override fun equals(other: Any?) =
        when {
            this === other -> true
            other is UnsignedBigInteger -> compareTo(other) == 0
            else -> false
        }

    /**
     * Сравнение на больше/меньше (по контракту Comparable.compareTo)
     */
    override fun compareTo(other: UnsignedBigInteger): Int {
        if (digit.size > other.digit.size) return 1
        if (digit.size < other.digit.size) return -1
        for (i in digit.lastIndex downTo 0)
            if (digit[i] != other.digit[i])
                return if (digit[i] > other.digit[i]) 1 else -1
        return 0
    }

    /**
     * Преобразование в строку
     */
    override fun toString(): String = this.digit.reversed().joinToString("")

    /**
     * Преобразование в целое
     * Если число не влезает в диапазон Int, бросить ArithmeticException
     */
    fun toInt(): Int {
        if (digit.size > 10) throw ArithmeticException("very large")
        var res = 0L
        for (i in digit.lastIndex downTo 0) {
            res = res * 10 + digit[i]
        }
        if (res > Int.MAX_VALUE) throw ArithmeticException("very large")
        return res.toInt()
    }

    override fun hashCode(): Int = digit.hashCode()

}