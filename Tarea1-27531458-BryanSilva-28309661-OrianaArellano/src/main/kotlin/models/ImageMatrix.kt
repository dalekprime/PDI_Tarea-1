package models

import javafx.scene.image.Image
import javafx.scene.image.WritableImage
import javafx.scene.paint.Color
import java.io.File
import java.util.Scanner

class ImageMatrix {
    var width = 0
    var height = 0
    var maxVal = 0
    lateinit var pixels: Array<Array<Pixel>>

    constructor(width: Int, height: Int) {
        this.width = width
        this.height = height
        this.pixels = Array(height) { Array(width) { Pixel(0,0,0) } }
    }
    constructor(file: File){
        var ext = file.extension
        when(ext.lowercase()){
            "png" -> loadImageFromPNG(file)
            "pgm" -> loadImageFromPGM(file)
            "pbm" -> loadImageFromPBM(file)
            "ppm" -> loadImageFromPPM(file)
            else -> return
        }
    }
    private fun loadImageFromPNG(file: File){
        val image = Image(file.toURI().toString())
        width = image.width.toInt()
        height = image.height.toInt()
        val pixelReader = image.pixelReader
        val matrix = Array(height) { Array(width) { Pixel(0,0,0) } }
        for(y in 0 until height){
            for(x in 0 until width){
                val argb = pixelReader.getArgb(x, y)
                val pixelR = (argb shr 16) and 0xFF
                val pixelG = (argb shr 8) and 0xFF
                val pixelB = argb and 0xFF
                matrix[y][x] = Pixel(pixelR, pixelG, pixelB)
            }
        }
        pixels = matrix
    }
    private fun loadImageFromPGM(file: File){
        val scanner = Scanner(file)
        fun nextElement(): String{
            while (scanner.hasNext()){
                val pixel = scanner.next()
                if (pixel.startsWith("#")){
                    scanner.nextLine()
                    continue
                }
                return pixel
            }
            throw Exception("Archivo Corrupto")
        }
        val header = nextElement()
        if(header != "P2"){
            throw Exception("Codificacion no Soportada")
        }
        width = nextElement().toInt()
        height = nextElement().toInt()
        maxVal = nextElement().toInt()
        val matrix = Array(height) { Array(width) { Pixel(0,0,0) } }
        for(y in 0 until height){
            for(x in 0 until width){
                if(scanner.hasNext()){
                    val pixelR = nextElement().toInt()
                    matrix[y][x] = Pixel(pixelR, pixelR, pixelR)
                }
            }
        }
        scanner.close()
        pixels = matrix
    }
    private fun loadImageFromPBM(file: File){
        val scanner = Scanner(file)
        fun nextElement(): String{
            while (scanner.hasNext()){
                val pixel = scanner.next()
                if (pixel.startsWith("#")){
                    scanner.nextLine()
                    continue
                }
                return pixel
            }
            throw Exception("Archivo Corrupto")
        }
        val header = nextElement()
        if(header != "P1"){
            throw Exception("Codificacion no Soportada")
        }
        width = nextElement().toInt()
        height = nextElement().toInt()
        val matrix = Array(height) { Array(width) { Pixel(0,0,0) } }
        for(y in 0 until height){
            for(x in 0 until width){
                if(scanner.hasNext()){
                    val pixelR = nextElement().toInt()
                    if (pixelR == 0){
                        matrix[y][x] = Pixel(0,0,0)
                    }else{
                        matrix[y][x] = Pixel(255,255,255)
                    }

                }
            }
        }
        scanner.close()
        pixels = matrix
    }
    private fun loadImageFromPPM(file: File){
        val scanner = Scanner(file)
        fun nextElement(): String{
            while (scanner.hasNext()){
                val pixel = scanner.next()
                if (pixel.startsWith("#")){
                    scanner.nextLine()
                    continue
                }
                return pixel
            }
            throw Exception("Archivo Corrupto")
        }
        val header = nextElement()
        if(header != "P3"){
            throw Exception("Codificacion no Soportada")
        }
        width = nextElement().toInt()
        height = nextElement().toInt()
        maxVal = nextElement().toInt()
        val matrix = Array(height) { Array(width) { Pixel(0,0,0) } }
        for(y in 0 until height){
            for(x in 0 until width){
                if(scanner.hasNext()){
                    val pixelR = nextElement().toInt()
                    val pixelG = nextElement().toInt()
                    val pixelB = nextElement().toInt()
                    matrix[y][x] = Pixel(pixelR, pixelG, pixelB)
                }
            }
        }
        scanner.close()
        pixels = matrix
    }
    fun matrixToImage(): Image{
        val outputImage = WritableImage(width, height)
        val writer = outputImage.pixelWriter
        for(y in 0 until height){
            for(x in 0 until width){
                val pixel = pixels[y][x]
                val argb = (0xFF shl 24) or (pixel.r shl 16) or (pixel.g shl 8) or pixel.b
                writer.setArgb(x, y, argb)
            }
        }
        return outputImage
    };
    fun print(){
        println("${width}x${height}")
        println(maxVal)
        for (row in 0 until height) {
            for (col in 0 until width) {
                print("${pixels[row][col].r} ")
            }
            println()
        }
    }
}