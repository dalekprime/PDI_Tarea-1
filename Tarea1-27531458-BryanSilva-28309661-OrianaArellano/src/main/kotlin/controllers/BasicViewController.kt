package controllers

import javafx.event.ActionEvent
import javafx.fxml.FXML
import javafx.scene.chart.AreaChart
import javafx.scene.chart.LineChart
import javafx.scene.chart.XYChart
import javafx.scene.control.Accordion
import javafx.scene.control.ColorPicker
import javafx.scene.control.Label
import javafx.scene.control.RadioButton
import javafx.scene.control.Slider
import javafx.scene.control.TextField
import javafx.scene.control.TitledPane
import javafx.scene.control.ToggleButton
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
    private lateinit var toneCurveChart: LineChart<Number, Number>

    @FXML
    private lateinit var applicationConsole: Label

    @FXML
    private lateinit var mainImageView: ImageView

    @FXML
    private lateinit var bppImage: Label

    @FXML
    private lateinit var colorsImage: Label

    @FXML
    private lateinit var dimImage: Label

    private lateinit var chartController: ChartStateController
    private lateinit var imageController: ImageStateController
    private lateinit var dataController: DataStateController

    private var matrixImage: ImageMatrix? = null
    private var originalImage: ImageMatrix? = null

    //Estado del canal visualizado en el Histograma
    @FXML
    private lateinit var histogram: ToggleGroup
    @FXML
    fun onRadioHistogramClick(event: ActionEvent) {
        val channel = (histogram.selectedToggle as RadioButton).text
        chartController.updateHistogram(matrixImage, channel)
    }

    //Leer la imagen y setea el estado inicial de la aplicacion
    @FXML
    fun onLoadImageClick(event: ActionEvent) {
        //Controladores de Imagen, Gráficos e Información
        chartController = ChartStateController(histogramChart, toneCurveChart)
        dataController = DataStateController(dimImage, colorsImage, bppImage)
        imageController = ImageStateController(stage,applicationConsole, mainImageView, chartController, dataController)
        //Leer Imagen Inicial y crea una copia
        matrixImage = imageController.loadNewImage()
        originalImage = matrixImage!!.copy()
    }

    //Inicializar estado
    @FXML
    private lateinit var dataPanel: TitledPane
    @FXML
    private lateinit var rightAccordion: Accordion
    @FXML
    fun initialize() {
        rightAccordion.expandedPane = dataPanel
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
                }else{
                    matrixImage!!.pixels[y][x].r = 255
                    matrixImage!!.pixels[y][x].g = 255
                    matrixImage!!.pixels[y][x].b = 255
                }
            }
        }
        imageController.changeView(matrixImage!!)
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
        imageController.changeView(matrixImage!!)
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
        imageController.changeView(matrixImage!!)
    }

    @FXML
    private lateinit var colorScalePicker: ColorPicker
    @FXML
    fun onColorScalePickerClick(event: ActionEvent) {
        val r = (colorScalePicker.value.red * 255.0).toInt()
        val g = (colorScalePicker.value.green * 255.0).toInt()
        val b = (colorScalePicker.value.blue * 255.0).toInt()
        val width = matrixImage!!.width
        val height = matrixImage!!.height
        for (y in 0 until height) {
            for (x in 0 until width) {
                val greyColor = (0.299*originalImage!!.pixels[y][x].r + 0.587*originalImage!!.pixels[y][x].g + 0.114*originalImage!!.pixels[y][x].b).toInt()
                if(greyColor < 128){
                    matrixImage!!.pixels[y][x].r = r * greyColor/128
                    matrixImage!!.pixels[y][x].g = g * greyColor/128
                    matrixImage!!.pixels[y][x].b = b * greyColor/128
                }else{
                    matrixImage!!.pixels[y][x].r = r + (255 - r)*(greyColor - 128)/128
                    matrixImage!!.pixels[y][x].g = g + (255 - g)*(greyColor - 128)/128
                    matrixImage!!.pixels[y][x].b = b + (255 - b)*(greyColor - 128)/128
                }
            }
        }
        imageController.changeView(matrixImage!!)
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
        imageController.changeView(matrixImage!!)
    }

    @FXML
    private lateinit var contrastSlider: Slider
    @FXML
    fun onConstrastButtonClick(event: ActionEvent) {
        val change = contrastSlider.value
        val width = matrixImage!!.width
        val height = matrixImage!!.height
        for (y in 0 until height) {
            for (x in 0 until width) {
                matrixImage!!.pixels[y][x].r = (((originalImage!!.pixels[y][x].r - 128) * change) + 128).coerceIn(0.0, 255.0).toInt()
                matrixImage!!.pixels[y][x].g = (((originalImage!!.pixels[y][x].g - 128) * change) + 128).coerceIn(0.0, 255.0).toInt()
                matrixImage!!.pixels[y][x].b = (((originalImage!!.pixels[y][x].b - 128) * change) + 128).coerceIn(0.0, 255.0).toInt()
            }
        }
        imageController.changeView(matrixImage!!)
    }

    @FXML
    private lateinit var perfilAreaChart: AreaChart<Number, Number>
    @FXML
    private lateinit var perfilText: TextField
    @FXML
    fun onPerfilButtonClick(event: ActionEvent) {
        matrixImage?:return
        val width = matrixImage!!.width
        val line = perfilText.text.toIntOrNull()
        line?:return
        if (line < 0 || line >= (matrixImage!!.height)) {
            println("Error: La línea $line no existe")
            return
        }
        val series = XYChart.Series<Number, Number>()
        series.name = "Fila $line"
        for (x in 0 until width) {
            series.data.add(XYChart.Data(x, matrixImage!!.pixels[line][x].r))
        }
        perfilAreaChart.data.clear()
        perfilAreaChart.data.add(series)
    }


}