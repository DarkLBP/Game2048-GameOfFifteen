package board

import board.Direction.*
import java.lang.IllegalArgumentException

fun createSquareBoard(width: Int): SquareBoard = SquareBoardImpl(width)
fun <T> createGameBoard(width: Int): GameBoard<T> = GameBoardImpl(width)

open class SquareBoardImpl(final override val width: Int) : SquareBoard {
    private val board: MutableMap<Pair<Int, Int>, Cell> = mutableMapOf()

    init {
        for (i in 1..width) {
            for (j in 1..width) {
                board[Pair(i, j)] = Cell(i, j)
            }
        }
    }

    override fun getCellOrNull(i: Int, j: Int): Cell? = board[Pair(i, j)]

    override fun getCell(i: Int, j: Int): Cell =
            getCellOrNull(i, j) ?: throw IllegalArgumentException("Out of bounds $i $j")

    override fun getAllCells(): Collection<Cell> = board.values

    override fun getRow(i: Int, jRange: IntProgression): List<Cell> {
        val cells = mutableListOf<Cell>()
        for (j in jRange) {
            getCellOrNull(i, j)?.let { cells.add(it) }
        }
        return cells
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell> {
        val cells = mutableListOf<Cell>()
        for (i in iRange) {
            getCellOrNull(i, j)?.let { cells.add(it) }
        }
        return cells
    }

    override fun Cell.getNeighbour(direction: Direction): Cell? {
        return when (direction) {
            DOWN -> getCellOrNull(i + 1 , j)
            LEFT -> getCellOrNull(i, j - 1)
            RIGHT -> getCellOrNull(i, j + 1)
            UP -> getCellOrNull(i - 1, j)
        }
    }
}

class GameBoardImpl<T>(width: Int): SquareBoardImpl(width), GameBoard<T> {
    private val boardData: MutableMap<Cell, T?> = mutableMapOf()

    init {
        for (i in 1..width) {
            for (j in 1..width) {
                boardData[getCell(i, j)] = null
            }
        }
    }

    override fun get(cell: Cell): T? = boardData[cell]

    override fun set(cell: Cell, value: T?) {
        boardData[cell] = value
    }

    override fun filter(predicate: (T?) -> Boolean): Collection<Cell> = boardData.keys.filter { predicate(get(it)) }

    override fun find(predicate: (T?) -> Boolean): Cell? =  boardData.keys.first { predicate(get(it)) }

    override fun any(predicate: (T?) -> Boolean): Boolean = boardData.values.any(predicate)

    override fun all(predicate: (T?) -> Boolean): Boolean = boardData.values.all(predicate)

}