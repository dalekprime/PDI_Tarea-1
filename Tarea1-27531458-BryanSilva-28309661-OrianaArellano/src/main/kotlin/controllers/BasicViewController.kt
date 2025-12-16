package controllers


import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.chart.AreaChart
import javafx.scene.chart.LineChart
import javafx.scene.control.Label
import javafx.scene.control.RadioButton
import javafx.scene.control.Slider
import javafx.scene.control.ToggleGroup
import javafx.scene.image.ImageView
import javafx.stage.Stage
import models.ImageMatrix


class BasicViewController {

    private lateinit var stage: Stage
    fun setStage(stage: Stage) {
        this.stage = stage
    }

    @FXML
    private lateinit var histogramChart: AreaChart<Number, Number>

    @FXML
    private lateinit var toneCurveChat: LineChart<Number, Number>

    @FXML
    private lateinit var applicationConsole: Label

    @FXML
    private lateinit var mainImageView: ImageView

    private lateinit var chartController: ChartsController

    private var matrixImage: ImageMatrix? = null

    @FXML
    private lateinit var histogram: ToggleGroup
    @FXML
    fun onRadioHistogramClick(event: ActionEvent) {
        val channel = (histogram.selectedToggle as RadioButton).text
        chartController.update(matrixImage, channel)

    }

    @FXML
    fun onLoadImageClick(event: ActionEvent) {
        val controller = ImageStateController(stage,applicationConsole, mainImageView)
        chartController = ChartsController(histogramChart, toneCurveChat)
        matrixImage = controller.loadNewImage()
        chartController.update(matrixImage, "R")
    }

    @FXML
    private lateinit var umbralSlider: Slider
    @FXML
    fun onUmbralButtonClick(event: ActionEvent){
        val threshold = umbralSlider.value.toInt()
        val width = matrixImage!!.width
        val height = matrixImage!!.height
        for (y in 0 until height) {
            for (x in 0 until width) {
                if((0.299*matrixImage!!.pixels[y][x].r + 0.587*matrixImage!!.pixels[y][x].g + 0.114*matrixImage!!.pixels[y][x].b) < threshold){
                    matrixImage!!.pixels[y][x].r = 0
                    matrixImage!!.pixels[y][x].g = 0
                    matrixImage!!.pixels[y][x].b = 0
                }else {
                    matrixImage!!.pixels[y][x].r = 255
                    matrixImage!!.pixels[y][x].g = 255
                    matrixImage!!.pixels[y][x].b = 255
                }
            }
        }
        val imageNew = matrixImage!!.matrixToImage()
        mainImageView.image = imageNew
    }

    @FXML
    fun onNegativeButtonClick(event: ActionEvent) {
        val width = matrixImage!!.width
        val height = matrixImage!!.height
        for (y in 0 until height) {
            for (x in 0 until width) {
                matrixImage!!.pixels[y][x].r = 225 - matrixImage!!.pixels[y][x].r
                matrixImage!!.pixels[y][x].g = 225 - matrixImage!!.pixels[y][x].g
                matrixImage!!.pixels[y][x].b = 225 - matrixImage!!.pixels[y][x].b
            }
        }
        val imageNew = matrixImage!!.matrixToImage()
        mainImageView.image = imageNew
    }

    @FXML
    fun onGreyscaleButtonClick(event: ActionEvent) {
        val width = matrixImage!!.width
        val height = matrixImage!!.height
        for (y in 0 until height) {
            for (x in 0 until width) {
                val greyColor = 0.299*matrixImage!!.pixels[y][x].r + 0.587*matrixImage!!.pixels[y][x].g + 0.114*matrixImage!!.pixels[y][x].b
                matrixImage!!.pixels[y][x].r = greyColor.toInt()
                matrixImage!!.pixels[y][x].g = greyColor.toInt()
                matrixImage!!.pixels[y][x].b = greyColor.toInt()
            }
        }
        val imageNew = matrixImage!!.matrixToImage()
        mainImageView.image = imageNew
    }

    @FXML
    private lateinit var  lightSlider: Slider
    @FXML
    fun onLightButtonClick(event: ActionEvent) {
        val change = lightSlider.value.toInt()
        val width = matrixImage!!.width
        val height = matrixImage!!.height
        for (y in 0 until height) {
            for (x in 0 until width) {
                matrixImage!!.pixels[y][x].r = (matrixImage!!.pixels[y][x].r + change).coerceIn(0, 255)
                matrixImage!!.pixels[y][x].g = (matrixImage!!.pixels[y][x].g + change).coerceIn(0, 255)
                matrixImage!!.pixels[y][x].b = (matrixImage!!.pixels[y][x].b + change).coerceIn(0, 255)
            }
        }
        val imageNew = matrixImage!!.matrixToImage()
        mainImageView.image = imageNew
    }
}