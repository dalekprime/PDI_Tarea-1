package controllers

import javafx.scene.chart.AreaChart
import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import jdk.internal.org.commonmark.internal.Bracket.image
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
}