@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson9.task1

import lesson11.task1.Rational

// Урок 9: проектирование классов
// Максимальное количество баллов = 40 (без очень трудных задач = 15)

/**
 * Ячейка матрицы: row = ряд, column = колонка
 */
data class Cell(val row: Int, val column: Int)

/**
 * Интерфейс, описывающий возможности матрицы. E = тип элемента матрицы
 */
interface Matrix<E> {
    /** Высота */
    val height: Int

    /** Ширина */
    val width: Int

    /**
     * Доступ к ячейке.
     * Методы могут бросить исключение, если ячейка не существует или пуста
     */
    operator fun get(row: Int, column: Int): E

    operator fun get(cell: Cell): E

    /**
     * Запись в ячейку.
     * Методы могут бросить исключение, если ячейка не существует
     */
    operator fun set(row: Int, column: Int, value: E)

    operator fun set(cell: Cell, value: E)
}

/**
 * Простая (2 балла)
 *
 * Метод для создания матрицы, должен вернуть РЕАЛИЗАЦИЮ Matrix<E>.
 * height = высота, width = ширина, e = чем заполнить элементы.
 * Бросить исключение IllegalArgumentException, если height или width <= 0.
 */
fun <E> createMatrix(height: Int, width: Int, e: E): Matrix<E> {
    if (height <= 0 || width <= 0) throw IllegalArgumentException("error")
    val m = MatrixImpl<E>(height, width)
    for (j in 0 until width)
        for (i in 0 until height)
            m[i, j] = e
    return m
}


/**
 * Средняя сложность (считается двумя задачами в 3 балла каждая)
 *
 * Реализация интерфейса "матрица"
 */
class MatrixImpl<E>(h: Int, w: Int) : Matrix<E> {
    private val table = mutableMapOf<Cell, E>()

    override val height: Int = h

    override val width: Int = w

    override fun get(row: Int, column: Int): E = get(Cell(row, column))

    override fun get(cell: Cell): E {
        if (cell !in table.keys || table[cell] == null) throw IllegalArgumentException("index error")
        return table.getValue(cell)
    }

    override fun set(row: Int, column: Int, value: E) {
        set(Cell(row, column), value)
    }

    override fun set(cell: Cell, value: E) {
        if (cell.row !in 0 until height || cell.column !in 0 until width)
            throw IllegalArgumentException("index error")
        table[cell] = value
    }

    override fun equals(other: Any?) =
        when {
            this === other -> true
            other is MatrixImpl<*> -> table == other.table
            else -> false
        }

    override fun toString(): String = table.toString()

    fun toSqr(w: Int = 3) {
        for (i in 0 until height) {
            for (j in 0 until width) print(
                table[Cell(i, j)].toString().padStart(w)
            )
            println()
        }
    }

    override fun hashCode(): Int {
        var result = table.hashCode()
        result = 31 * result + height
        result = 31 * result + width
        return result
    }
}

