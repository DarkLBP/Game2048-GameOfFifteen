package games.gameOfFifteen

import board.Direction
import board.GameBoard
import board.GameBoardImpl
import games.game.Game

/*
 * Implement the Game of Fifteen (https://en.wikipedia.org/wiki/15_puzzle).
 * When you finish, you can play the game by executing 'PlayGameOfFifteen'.
 */
fun newGameOfFifteen(initializer: GameOfFifteenInitializer = RandomGameInitializer()): Game =
        GameOfFifteen(initializer)

class GameOfFifteen(private val initializer: GameOfFifteenInitializer) : Game {
    private lateinit var board: GameBoard<Int?>

    override fun initialize() {
        board = GameBoardImpl(4)
        val elements = initializer.initialPermutation.iterator()
        filler@ for (i in 1..4) {
            for (j in 1..4) {
                if (elements.hasNext()) {
                    board[board.getCell(i, j)] = elements.next()
                } else {
                    break@filler
                }
            }
        }
    }

    override fun canMove(): Boolean = true

    override fun hasWon(): Boolean =
            board.getAllCells()
                    .take(15)
                    .withIndex()
                    .all { (index, cell) -> board[cell] != null && board[cell] == index + 1 }

    override fun processMove(direction: Direction) {
        val invertedDirection = when(direction) {
            Direction.LEFT -> Direction.RIGHT
            Direction.RIGHT -> Direction.LEFT
            Direction.UP -> Direction.DOWN
            Direction.DOWN -> Direction.UP
        }
        with(board) {
            val emptyCell = find { it == null }!!
            val neighbourCell = emptyCell.getNeighbour(invertedDirection)
            if (neighbourCell != null) {
                this[emptyCell] = this[neighbourCell]
                this[neighbourCell] = null
            }
        }
    }

    override fun get(i: Int, j: Int): Int? = board[board.getCell(i, j)]
}