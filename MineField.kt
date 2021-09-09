package minesweeper

import kotlin.random.Random

class MineField(val rows: Int, val cols: Int, val n: Int) {
    private val arr = Array(rows) { IntArray(cols) }
    private val arr2 = Array(rows) { IntArray(cols) { 0 } }
    private val marks = Array(rows) { BooleanArray(cols) { false } }
    private var mines = 0
    private var stars = 0

    private fun setMines(r0: Int, c0: Int) {
        val row = arr.lastIndex
        val col = arr[0].lastIndex
        var cnt = 0
        while (cnt < n) {
            val r = Random.nextInt(row + 1)
            val c = Random.nextInt(col + 1)
            if (r != r0 || c != c0 && arr[r][c] == 0) {
                arr[r][c] = -1
                cnt++
            }
        }
        cntMines()
    }

    private fun cntMines() {
        for (i in 0 until rows) {
            for (j in 0 until cols) {
                if (arr[i][j] == 0) {
                    arr[i][j] = cntNeighbors(i, j)
                }
            }
        }
    }

    private fun cntNeighbors(r: Int, c: Int): Int {
        var cnt = 0
        for (i in r - 1..r + 1) {
            for (j in c - 1..c + 1) {
                if (i in 0 until rows && j in 0 until cols) {
                    cnt += if (arr[i][j] == -1) 1 else 0
                }
            }
        }
        return cnt
    }

    private fun free(r: Int, c: Int) {
        if (arr[r][c] == 0) {
            arr2[r][c] = -3
            for (i in r - 1..r + 1) {
                for (j in c - 1..c + 1) {
                    if (i in 0 until rows && j in 0 until cols) {
                        when {
                            arr[i][j] == 0 && (arr2[i][j] == 0 || arr2[i][j] == -2) -> {
                                arr2[i][j] = -3
                                free(i, j)
                            }
                            arr[i][j] > 0 -> arr2[i][j] = arr[i][j]
                        }
                    }
                }
            }
        } else {
            arr2[r][c] = arr[r][c]
        }
    }

    fun draw(showMine: Boolean) {
        println("  |123456789|")
        println(" -|---------|")
        for (i in 0 until rows) {
            print(" ${i + 1}|")
            for (j in 0 until cols) {
                print(when {
                    showMine && arr[i][j] == -1 -> 'X'
                    arr2[i][j] == 0 -> '.'
                    arr2[i][j] == -3 -> '/'
                    arr2[i][j] == -2 -> '*'
                    else -> arr2[i][j]
                })
            }
            println("|")
        }
        println(" -|---------|")
    }

    fun setMark(r: Int, c: Int) {
        if (arr2[r][c] == -2) {
            // reset mark
            arr2[r][c] = 0
            stars--
            if (arr[r][c] == -1 ) {
                mines--
            }
        } else {
            // set mark
            arr2[r][c] = -2
            stars++
            if (arr[r][c] == -1 ) {
                mines++
            }
        }
    }

    fun game() {
        var startGame = true
        var openMines = false
        draw(openMines)
        while (true) {
            print("Set/unset mines marks or claim a cell as free: >")
            val (x, y, cmd) = readLine()!!.split(" ")
            val row = y.toInt() - 1
            val col = x.toInt() - 1
            if (startGame && cmd == "free") {
                setMines(row, col)
            }
            when (cmd) {
                "mine" -> {
                    setMark(row, col)
                }
                "free" -> {
                    if (arr[row][col] == -1) {
                        openMines = true
                        draw(openMines)
                        println("You stepped on a mine and failed!")
                        break
                    }
                    startGame = false
                    free(row, col)
                }
            }
            draw(openMines)
            if (mines == n && stars == n) {
                println("Congratulations! You found all the mines!")
                break
            }
        }
    }
}