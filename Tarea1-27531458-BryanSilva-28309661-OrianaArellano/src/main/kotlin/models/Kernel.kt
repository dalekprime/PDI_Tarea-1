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
        this.height = ele.nextLine().toInt()
        this.width = ele.nextLine().toInt()
        matrix = Array(height) { Array(width) { 0.0 } }
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (ele.hasNext()) {
                    matrix[y][x] = ele.next().toDouble()
                }
            }
        }
        ele.close()
    }
}