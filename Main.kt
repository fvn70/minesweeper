package minesweeper

import kotlin.random.Random

fun main() {
    val rows = 9
    val cols = 9
    print("How many mines do you want on the field? >")
    val mines = readLine()!!.toInt()

    val mineField = MineField(rows, cols, mines)
    mineField.game()
}

