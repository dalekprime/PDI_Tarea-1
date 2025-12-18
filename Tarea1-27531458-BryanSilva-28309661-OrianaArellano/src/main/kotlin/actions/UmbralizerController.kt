package actions

import models.ImageMatrix

class UmbralizerController {
    //Umbral Simple
    fun simpleUmbral(imageMatrix: ImageMatrix, threshold: Number): ImageMatrix {
        val width = imageMatrix.width
        val height = imageMatrix.height
        for (y in 0 until height) {
            for (x in 0 until width) {
                val grey = (0.299*imageMatrix.pixels[y][x].r + 0.587*imageMatrix.pixels[y][x].g + 0.114*imageMatrix.pixels[y][x].b)
                if( grey < threshold.toInt()) {
                    imageMatrix.pixels[y][x].r = 0
                    imageMatrix.pixels[y][x].g = 0
                    imageMatrix.pixels[y][x].b = 0
                }else{
                    imageMatrix.pixels[y][x].r = 255
                    imageMatrix.pixels[y][x].g = 255
                    imageMatrix.pixels[y][x].b = 255
                }
            }
        }
        return imageMatrix
    }
    //Umbral Multiple
    fun multiUmbral(imageMatrix: ImageMatrix, thresholdInf: Number, thresholdSup: Number): ImageMatrix {
        val width = imageMatrix.width
        val height = imageMatrix.height
        for (y in 0 until height) {
            for (x in 0 until width) {
                val grey = (0.299*imageMatrix.pixels[y][x].r + 0.587*imageMatrix.pixels[y][x].g + 0.114*imageMatrix.pixels[y][x].b)
                if(thresholdInf.toInt() > grey && grey < thresholdSup.toInt()){
                    imageMatrix.pixels[y][x].r = 0
                    imageMatrix.pixels[y][x].g = 0
                    imageMatrix.pixels[y][x].b = 0
                }else{
                    imageMatrix.pixels[y][x].r = 255
                    imageMatrix.pixels[y][x].g = 255
                    imageMatrix.pixels[y][x].b = 255
                }
            }
        }
        return imageMatrix
    }
}