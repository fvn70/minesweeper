package minesweeper

import kotlin.random.Random


class MineField(val rows: Int, val cols: Int, val n: Int) {
    val arr = List(rows) { MutableList(cols) { '.' } }
    val marks = List(rows) { MutableList(cols) { false } }
    var find = 0
    var stars = 0
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
        println("\n │123456789│")
        println("—│—————————│")
        for (i in 0 until rows) {
            print("${i + 1}│")
            for (j in 0 until cols) {
                print(if (marks[i][j]) '*' else if (arr[i][j] == 'X') '.' else arr[i][j])
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

    fun game() {
        draw()
        while (true) {
            print("Set/delete mines marks (x and y coordinates): ")
            val (x, y) = readLine()!!.split(" ").map { it.toInt() }
            if (!setMark(y - 1, x - 1)) continue
            draw()
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
