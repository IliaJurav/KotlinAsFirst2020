@file:Suppress("UNUSED_PARAMETER")

package lesson8.task1

import lesson1.task1.sqr
import java.sql.DatabaseMetaData
import kotlin.math.*

// Урок 8: простые классы
// Максимальное количество баллов = 40 (без очень трудных задач = 11)

/**
 * Точка на плоскости
 */
data class Point(val x: Double, val y: Double) {
    /**
     * Пример
     *
     * Рассчитать (по известной формуле) расстояние между двумя точками
     */
    fun distance(other: Point): Double = sqrt(sqr(x - other.x) + sqr(y - other.y))
}

/**
 * Треугольник, заданный тремя точками (a, b, c, см. constructor ниже).
 * Эти три точки хранятся в множестве points, их порядок не имеет значения.
 */
@Suppress("MemberVisibilityCanBePrivate")
class Triangle private constructor(private val points: Set<Point>) {

    private val pointList = points.toList()

    val a: Point get() = pointList[0]

    val b: Point get() = pointList[1]

    val c: Point get() = pointList[2]

    constructor(a: Point, b: Point, c: Point) : this(linkedSetOf(a, b, c))

    /**
     * Пример: полупериметр
     */
    fun halfPerimeter() = (a.distance(b) + b.distance(c) + c.distance(a)) / 2.0

    /**
     * Пример: площадь
     */
    fun area(): Double {
        val p = halfPerimeter()
        return sqrt(p * (p - a.distance(b)) * (p - b.distance(c)) * (p - c.distance(a)))
    }

    /**
     * Пример: треугольник содержит точку
     */
    fun contains(p: Point): Boolean {
        val abp = Triangle(a, b, p)
        val bcp = Triangle(b, c, p)
        val cap = Triangle(c, a, p)
        return abp.area() + bcp.area() + cap.area() <= area()
    }

    override fun equals(other: Any?) = other is Triangle && points == other.points

    override fun hashCode() = points.hashCode()

    override fun toString() = "Triangle(a = $a, b = $b, c = $c)"
}

/**
 * Окружность с заданным центром и радиусом
 */
data class Circle(val center: Point, val radius: Double) {
    /**
     * Простая (2 балла)
     *
     * Рассчитать расстояние между двумя окружностями.
     * Расстояние между непересекающимися окружностями рассчитывается как
     * расстояние между их центрами минус сумма их радиусов.
     * Расстояние между пересекающимися окружностями считать равным 0.0.
     */
    fun distance(other: Circle): Double {
        val dist = other.center.distance(center) - radius - other.radius
        return if (dist < 0.0) 0.0
        else dist
    }

    /**
     * Тривиальная (1 балл)
     *
     * Вернуть true, если и только если окружность содержит данную точку НА себе или ВНУТРИ себя
     */
    fun contains(p: Point) = p.distance(center) <= radius
}

/**
 * Отрезок между двумя точками
 */
data class Segment(val begin: Point, val end: Point) {
    override fun equals(other: Any?) =
        other is Segment && (begin == other.begin && end == other.end || end == other.begin && begin == other.end)

    override fun hashCode() =
        31 * begin.hashCode() + end.hashCode()
}

/**
 * Средняя (3 балла)
 *
 * Дано множество точек. Вернуть отрезок, соединяющий две наиболее удалённые из них.
 * Если в множестве менее двух точек, бросить IllegalArgumentException
 */
fun diameter(vararg points: Point): Segment {
    if (points.size < 2) throw IllegalArgumentException("Points < 2")
    var dis = 0.0
    var p1 = 0
    var p2 = 1
    for (i in 0..points.size - 2) {
        for (g in i + 1 until points.size) {
            val x = points[i].distance(points[g])
            if (x > dis) {
                dis = x
                p1 = i
                p2 = g
            }
        }
    }
    return Segment(points[p1], points[p2])
}

/**
 * Простая (2 балла)
 *
 * Построить окружность по её диаметру, заданному двумя точками
 * Центр её должен находиться посередине между точками, а радиус составлять половину расстояния между ними
 */
fun circleByDiameter(diameter: Segment): Circle {
    with(diameter) {
        return Circle(
            Point((begin.x + end.x) / 2.0, (begin.y + end.y) / 2.0),
            begin.distance(end) / 2.0
        )
    }
}

/**
 * Прямая, заданная точкой point и углом наклона angle (в радианах) по отношению к оси X.
 * Уравнение прямой: (y - point.y) * cos(angle) = (x - point.x) * sin(angle)
 * или: y * cos(angle) = x * sin(angle) + b, где b = point.y * cos(angle) - point.x * sin(angle).
 * Угол наклона обязан находиться в диапазоне от 0 (включительно) до PI (исключительно).
 */
class Line private constructor(val b: Double, val angle: Double) {
    init {
        require(angle >= 0.0 && angle < PI) { "Incorrect line angle: $angle" }
    }

    constructor(point: Point, angle: Double) : this(
        point.y * cos(angle) - point.x * sin(angle),
        angle
    )

    /**
     * Средняя (3 балла)
     *
     * Найти точку пересечения с другой линией.
     * Для этого необходимо составить и решить систему из двух уравнений (каждое для своей прямой)
     */
    fun crossPoint(other: Line): Point {
        if (angle == other.angle) throw IllegalArgumentException("Прямые параллельны")
        val x = (other.b / cos(other.angle) - b / cos(angle)) / (tan(angle) - tan(other.angle))
        var v = if (abs(cos(angle)) <= 1e-5) 1 else 0
        if (abs(cos(angle) - 1.0) <= 1e-5) v += 4
        if (abs(cos(other.angle)) <= 1e-5) v += 2
        if (abs(cos(other.angle) - 1.0) <= 1e-5) v += 8
        when (v) {
            6 -> return Point(-other.b, b)
            9 -> return Point(-b, other.b)
            1 -> return Point(-b, -b * tan(other.angle) + other.b / cos(other.angle))
            2 -> return Point(-other.b, -other.b * tan(angle) + b / cos(angle))
        }
        val y = x * tan(angle) + b / cos(angle)
        return Point(x, y)
    }

    override fun equals(other: Any?) = other is Line && angle == other.angle && b == other.b

    override fun hashCode(): Int {
        var result = b.hashCode()
        result = 31 * result + angle.hashCode()
        return result
    }

    override fun toString() = "Line(${cos(angle)} * y = ${sin(angle)} * x + $b)"
}

/**
 * Средняя (3 балла)
 *
 * Построить прямую по отрезку
 */
fun lineBySegment(s: Segment) = lineByPoints(s.begin, s.end)

/**
 * Средняя (3 балла)
 *
 * Построить прямую по двум точкам
 */
fun lineByPoints(a: Point, b: Point): Line {
    val alf = acos((if (b.y >= a.y) b.x - a.x else a.x - b.x) / a.distance(b))
    return Line(a, alf % PI)
}

/**
 * Сложная (5 баллов)
 *
 * Построить серединный перпендикуляр по отрезку или по двум точкам
 */
fun bisectorByPoints(a: Point, b: Point): Line {
    val p = Point((a.x + b.x) / 2.0, (a.y + b.y) / 2.0)
    val alfa = (lineByPoints(a, b).angle + PI / 2.0) % PI
    return Line(p, alfa)
}

/**
 * Средняя (3 балла)
 *
 * Задан список из n окружностей на плоскости.
 * Найти пару наименее удалённых из них; расстояние между окружностями
 * рассчитывать так, как указано в Circle.distance.
 *
 * При наличии нескольких наименее удалённых пар,
 * вернуть первую из них по порядку в списке circles.
 *
 * Если в списке менее двух окружностей, бросить IllegalArgumentException
 */
fun findNearestCirclePair(vararg circles: Circle): Pair<Circle, Circle> {
    if (circles.size < 2) throw IllegalArgumentException("Circles < 2")
    var dis = circles[0].distance(circles[1])
    var p1 = 0
    var p2 = 1
    for (i in 0..circles.size - 2) {
        for (g in i + 1 until circles.size) {
            val x = circles[i].distance(circles[g])
            if (x < dis) {
                dis = x
                p1 = i
                p2 = g
            }
        }
    }
    return Pair(circles[p1], circles[p2])
}

/**
 * Сложная (5 баллов)
 *
 * Дано три различные точки. Построить окружность, проходящую через них
 * (все три точки должны лежать НА, а не ВНУТРИ, окружности).
 * Описание алгоритмов см. в Интернете
 * (построить окружность по трём точкам, или
 * построить окружность, описанную вокруг треугольника - эквивалентная задача).
 */
fun circleByThreePoints(a: Point, b: Point, c: Point): Circle {
    val distAB = a.distance(b)
    val distAC = a.distance(c)
    val distBC = b.distance(c)
    val cen = if (distAB < distAC && distAB < distBC)
        bisectorByPoints(a, c).crossPoint(bisectorByPoints(b, c))
    else if (distAC < distAB && distAC < distBC)
        bisectorByPoints(a, b).crossPoint(bisectorByPoints(b, c))
    else bisectorByPoints(a, b).crossPoint(bisectorByPoints(a, c))
    return Circle(cen, max(cen.distance(a), max(cen.distance(b), cen.distance(c))))
}


/**
 * Очень сложная (10 баллов)
 *
 * Дано множество точек на плоскости. Найти круг минимального радиуса,
 * содержащий все эти точки. Если множество пустое, бросить IllegalArgumentException.
 * Если множество содержит одну точку, вернуть круг нулевого радиуса с центром в данной точке.
 *
 * Примечание: в зависимости от ситуации, такая окружность может либо проходить через какие-либо
 * три точки данного множества, либо иметь своим диаметром отрезок,
 * соединяющий две самые удалённые точки в данном множестве.
 */
fun minContainingCircle(vararg points: Point): Circle {
    when (points.size) {
        0 -> throw IllegalArgumentException("No points")
        1 -> return Circle(points[0], 0.0)
        2 -> return circleByDiameter(Segment(points[0], points[1]))
        3 -> return circleByThreePoints(points[0], points[1], points[2])
    }
    val dia = diameter(*points)
    var cr = circleByDiameter(dia)
    for (i in points.indices)
        if (!cr.contains(points[i]))
            if (points[i] != dia.begin && points[i] != dia.end)
                cr = circleByThreePoints(points[i], dia.begin, dia.end)
    return cr
}

