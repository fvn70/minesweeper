package minesweeper

import kotlin.random.Random


class MineField(val rows: Int, val cols: Int, val n: Int) {
    val arr = List(rows) { MutableList(cols) { '.' } }
    val marks = List(rows) { MutableList(cols) { false } }
    var find = 0
    var stars = 0

    fun setMines(r0: Int, c0: Int) {
        var cnt = 0
        while (cnt < n) {
            val r = Random.nextInt(rows)
            val c = Random.nextInt(cols)
            if (r != r0 || c != c0 && arr[r][c] != 'X') {
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

    fun draw(showMine: Boolean) {
        println("\n │123456789│")
        println("—│—————————│")
        for (i in 0 until rows) {
            print("${i + 1}│")
            for (j in 0 until cols) {
                print(if (marks[i][j]) '*' else if (arr[i][j] == 'X' && !showMine) '.' else arr[i][j])
            }
            println("│")
        }
        println("—│—————————│")
    }

    fun setMark(r: Int, c: Int): Boolean {
        if (arr[r][c] in '1'..'8') {
            println("There is a number here!")
            return false
        } else {
            marks[r][c] = !marks[r][c]
            stars += if (marks[r][c]) 1 else -1
            if (arr[r][c] == 'X') {
                find += if (marks[r][c]) 1 else -1
            }
        }
        return true
    }

    fun freeCell(r: Int, c: Int) {
        if (arr[r][c] in "./" && cntNeighbors(r, c) == 0) {
            arr[r][c] = '/'
            for (i in r - 1..r + 1)
                for (j in c - 1..c + 1) {
                    if (i in 0..rows-1 && j in 0..cols-1
                        && (arr[i][j] == '.' || marks[i][j])) {
                        if (arr[i][j] == '.') {
                            arr[i][j] = '/'
                        }
                        if (marks[i][j]) {
                            marks[i][j] = false
                            stars--
                        }
                        freeCell(i, j)
                    }
                }
        } else {
            if (marks[r][c]) {
                marks[r][c] = false
                stars--
            }
        }
    }

    fun game() {
        var startGame = true
        var openMines = false
        draw(openMines)
        while (true) {
            print("Set/unset mines marks or claim a cell as free: ")
            val (x, y, cmd) = readLine()!!.split(" ")
            val row = y.toInt() - 1
            val col = x.toInt() - 1
            when (cmd) {
                "mine", "m" -> {
                    setMark(row, col)
                }
                "free", "f" -> {
                    if (startGame) setMines(row, col)
                    startGame = false
                    if (arr[row][col] == 'X') {
                        openMines = true
                        draw(openMines)
                        println("You stepped on a mine and failed!")
                        break
                    }
                    freeCell(row, col)
                }
            }
            draw(openMines)
            if (find == n && stars == n) {
                println("Congratulations! You found all the mines!")
                break
            }
        }
    }
}

fun main() {
    val rows = 9
    val cols = 9

    print("How many mines do you want on the field? ")
    val mines = readLine()!!.toInt()

    MineField(rows, cols, mines).game()
}
