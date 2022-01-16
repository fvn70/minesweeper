package minesweeper

import kotlin.random.Random


class MineField(val rows: Int, val cols: Int, val n: Int) {
    val arr = List(rows) { MutableList(cols) { '.' } }
        init {
            var cnt = 0
            while (cnt < n) {
                val r = Random.nextInt(rows)
                val c = Random.nextInt(cols)
                if (arr[r][c] != 'X') {
                    arr[r][c] = 'X'
                    cnt++
                }
            }
            cntMines()
        }

    fun cntMines() {
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (arr[i][j] == '.') {
                    val n = cntNeighbors(i, j)
                    if (n > 0) {
                        arr[i][j] = n.digitToChar()
                    }
                }
            }
        }
    }

    fun cntNeighbors(r: Int, c: Int): Int {
        var cnt = 0
        for (i in r - 1..r + 1) {
            for (j in c - 1..c + 1) {
                if (i in 0 until rows && j in 0 until cols) {
                    cnt += if (arr[i][j] == 'X') 1 else 0
                }
            }
        }
        return cnt
    }

    fun draw() {
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                print(arr[i][j])
            }
            println()
        }
    }
}

fun main() {
    val rows = 9
    val cols = 9

    print("How many mines do you want on the field? ")
    val mines = readLine()!!.toInt()

    val game = MineField(rows, cols, mines)
    game.draw()
}
