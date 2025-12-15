package controllers


import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.chart.BarChart
import javafx.scene.chart.LineChart
import javafx.scene.control.Label
import javafx.scene.control.MenuItem
import javafx.scene.image.ImageView
import javafx.stage.Stage
import models.ImageMatrix
import java.io.File


class BasicViewController {
    private lateinit var stage: Stage

    fun setStage(stage: Stage) {
        this.stage = stage
    }

    @FXML
    private lateinit var loadImage: MenuItem

    @FXML
    private val histogramChart: BarChart<Number, Number>? = null

    @FXML
    private val toneCurveChat: LineChart<Number, Number>? = null

    @FXML
    private lateinit var applicationConsole: Label

    @FXML
    private lateinit var mainImageView: ImageView

    private var startingImage: File? = null

    private var matrixImage: ImageMatrix? = null

    @FXML
    fun onLoadImageClick(event: ActionEvent) {
        val controller = ImageStateController()
        val selectedFile: File? = controller.loadImage(stage, applicationConsole)
        matrixImage = controller.changeImage(mainImageView, selectedFile, applicationConsole)
        startingImage = selectedFile
    }

    @FXML
    fun onUmbralButtonClick(event: ActionEvent){
        val width = matrixImage!!.width
        val height = matrixImage!!.height
        for (y in 0 until height) {
            for (x in 0 until width) {
                if((matrixImage!!.pixels[y][x].r+matrixImage!!.pixels[y][x].g+matrixImage!!.pixels[y][x].b)/3 <= 45){
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
    fun onLightButtonClick(event: ActionEvent) {
        val width = matrixImage!!.width
        val height = matrixImage!!.height
        for (y in 0 until height) {
            for (x in 0 until width) {
                val change = -50
                matrixImage!!.pixels[y][x].r = (matrixImage!!.pixels[y][x].r + change).coerceIn(0, 255)
                matrixImage!!.pixels[y][x].g = (matrixImage!!.pixels[y][x].g + change).coerceIn(0, 255)
                matrixImage!!.pixels[y][x].b = (matrixImage!!.pixels[y][x].b + change).coerceIn(0, 255)
            }
        }
        val imageNew = matrixImage!!.matrixToImage()
        mainImageView.image = imageNew
    }
}