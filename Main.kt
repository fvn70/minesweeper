package minesweeper

import kotlin.random.Random


class MineField(val rows: Int, val cols: Int) {
    val arr = List(rows) { MutableList(cols) { '.' } }

    fun setMines(n: Int) {
        var cnt = 0
        while (cnt < n) {
            val r = Random.nextInt(rows)
            val c = Random.nextInt(cols)
            if (arr[r][c] != 'X') {
                arr[r][c] = 'X'
                cnt++
            }
        }
    }

    fun draw() {
        for (i in 0..rows - 1) {
            for (j in 0..cols - 1) {
                print(arr[i][j])
            }
            println()
        }
    }
}

fun main() {
    val rows = 9
    val cols = 9
    val game = MineField(rows, cols)

    print("How many mines do you want on the field? ")
    val mines = readLine()!!.toInt()

    game.setMines(mines)
    game.draw()
}
