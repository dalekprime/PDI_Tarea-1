package controllers

import javafx.scene.chart.AreaChart
import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import models.ImageMatrix

class ChartStateController {

    //Referencias a los gráficos
    private var histogramChart: AreaChart<Number, Number>
    private var toneCurveChart: LineChart<Number, Number>
    private var perfilerChart: AreaChart<Number, Number>

    constructor(histogramChart: AreaChart<Number, Number>,
                toneCurveChart: LineChart<Number, Number>,
                perfilerChart: AreaChart<Number, Number>) {
        this.histogramChart = histogramChart
        this.toneCurveChart = toneCurveChart
        this.perfilerChart = perfilerChart
    }
    //Actualiza el Histograma
    fun updateHistogram(imageMatrix: ImageMatrix?, channel: String) {
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
                frequency[color.coerceIn(0,255)] += 1
            }
        }
        val series = XYChart.Series<Number, Number>()
        for (i in 0 until 256) {
            series.data.add(XYChart.Data(i, frequency[i]))
        }
        histogramChart.data.clear()
        histogramChart.data.add(series)
    }
    //Actualiza la Curva Tonal
    fun updateCurve(originalImage: ImageMatrix?, actualImage: ImageMatrix?, channel: String) {
        originalImage ?: return
        actualImage ?: return
        if (originalImage.width != actualImage.width || originalImage.height != actualImage.height) {
            toneCurveChart.data.clear()
            return
        }
        val lookupTable = IntArray(256) { -1 }
        val width = originalImage.width
        val height = originalImage.height
        for (y in 0 until height) {
            for (x in 0 until width) {
                val valOriginal: Int = when (channel) {
                    "R" -> originalImage.pixels[y][x].r
                    "G" -> originalImage.pixels[y][x].g
                    "B" -> originalImage.pixels[y][x].b
                    else -> -1
                }
                val valNuevo: Int = when (channel) {
                    "R" -> actualImage.pixels[y][x].r
                    "G" -> actualImage.pixels[y][x].g
                    "B" -> actualImage.pixels[y][x].b
                    else -> -1
                }
                if (valOriginal in 0..255 && valNuevo != -1) {
                    val safeNuevo = valNuevo.coerceIn(0, 255)
                    lookupTable[valOriginal] = safeNuevo
                }
            }
        }
        val series = XYChart.Series<Number, Number>()
        for (inputVal in 0 until 256) {
            val outputVal = lookupTable[inputVal]
            if (outputVal != -1) {
                series.data.add(XYChart.Data(inputVal, outputVal))
            }
        }
        toneCurveChart.data.clear()
        toneCurveChart.data.add(series)
    }
    //Actuliza el Perfil de la Imagen
    fun updatePerfil(imageMatrix: ImageMatrix?, line: Int, channel: String) {
        imageMatrix?: return
        val width = imageMatrix.width
        if (line < 0 || line >= (imageMatrix.height)) {
            println("Error: La línea $line no existe")
            return
        }
        val series = XYChart.Series<Number, Number>()
        series.name = "Fila $line"
        for (x in 0 until width) {
            val color: Int = when (channel) {
                "R" -> imageMatrix.pixels[line][x].r
                "G" -> imageMatrix.pixels[line][x].g
                "B" -> imageMatrix.pixels[line][x].b
                else -> -1
            }
            series.data.add(XYChart.Data(x, color))
        }
        perfilerChart.data.clear()
        perfilerChart.data.add(series)
    }
}