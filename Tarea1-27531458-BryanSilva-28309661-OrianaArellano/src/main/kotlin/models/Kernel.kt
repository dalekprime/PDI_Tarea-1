package models

import java.io.File
import java.util.Scanner
import kotlin.Double

class Kernel {

    var height: Int = 0
    var width: Int = 0
    var matrix: Array<Array<Double>>

    constructor(height: Int, width: Int) {
        this.height = height
        this.width = width
        matrix = Array(height) { Array(width) { 0.0 } }
    }
    constructor(file: File){
        var ele = Scanner(file)
        this.height = ele.nextInt()
        this.width = ele.nextInt()
        matrix = Array(height) { Array(width) { 0.0 } }
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (ele.hasNext()) {
                    val value = ele.nextDouble()
                    matrix[y][x] = value
                }
            }
        }
        ele.close()

    }

    fun perfilado4(): Kernel {
        val k = Kernel(3,3)
        k.matrix = arrayOf(
            arrayOf(0.0, -1.0, 0.0),
            arrayOf(-1.0, 4.0, -1.0),
            arrayOf(0.0, -1.0, 0.0)
        )
        return k
    }

    fun perfilado8(): Kernel {
        val k = Kernel(3,3)
        k.matrix = arrayOf(
            arrayOf(-1.0, -1.0, -1.0),
            arrayOf(-1.0, 8.0, -1.0),
            arrayOf(-1.0, -1.0, -1.0)
        )
        return k
    }
}