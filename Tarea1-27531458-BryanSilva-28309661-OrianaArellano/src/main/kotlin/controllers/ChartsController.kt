package controllers

import javafx.scene.chart.AreaChart
import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import models.ImageMatrix

class ChartsController {

    private var histogramChart: AreaChart<Number, Number>
    private var toneCurveChat: LineChart<Number, Number>

    constructor(histogramChart: AreaChart<Number, Number>, toneCurveChat: LineChart<Number, Number>) {
        this.histogramChart = histogramChart
        this.toneCurveChat = toneCurveChat
    }
    fun update(imageMatrix: ImageMatrix?, channel: String) {
        imageMatrix?:return
        val frequency = IntArray(256)
        val width = imageMatrix.width
        val height = imageMatrix.height
        for (y in 0 until height) {
            for (x in 0 until width) {
                val color: Int = when (channel) {
                    "R" -> imageMatrix.pixels[y][x].r
                    "G" -> imageMatrix.pixels[y][x].g
                    "B" -> imageMatrix.pixels[y][x].b
                    else -> -1
                }
                frequency[color] += 1
            }
        }
        val series = XYChart.Series<Number, Number>()
        for (i in 0 until 256) {
            series.data.add(XYChart.Data(i, frequency[i]))
        }
        histogramChart.data.clear()
        histogramChart.data.add(series)
    }
    fun updateCurve(originalImage: ImageMatrix?, actualImage: ImageMatrix?, channel: String) {
        originalImage?:return
        actualImage?:return
        val frequency1 = IntArray(256)
        val frequency2 = IntArray(256)
        val width = originalImage.width
        val height = originalImage.height
        for (y in 0 until height) {
            for (x in 0 until width) {
                val color1: Int = when (channel) {
                    "R" -> originalImage.pixels[y][x].r
                    "G" -> originalImage.pixels[y][x].g
                    "B" -> originalImage.pixels[y][x].b
                    else -> -1
                }
                val color2: Int = when (channel) {
                    "R" -> actualImage.pixels[y][x].r
                    "G" -> actualImage.pixels[y][x].g
                    "B" -> actualImage.pixels[y][x].b
                    else -> -1
                }
                frequency1[color1] += 1
                frequency2[color2] += 1
            }
        }
        val series = XYChart.Series<Number, Number>()
        for (i in 0 until 256) {
            series.data.add(XYChart.Data(frequency1[i], frequency2[i]))
        }
        toneCurveChat.data.clear()
        toneCurveChat.data.add(series)
    }
}