package controllers

import actions.ConvolutionController
import actions.LigthController
import actions.RotationController
import actions.UmbralizerController
import actions.TonoController
import actions.ZoomController
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
import javafx.scene.control.ToggleGroup
import javafx.scene.image.ImageView
import javafx.stage.Stage
import models.ImageMatrix
import models.Pixel
import models.Kernel
import javax.swing.Spring.height
import javax.swing.Spring.width
import kotlin.math.roundToInt

class BasicViewController {

    private lateinit var stage: Stage
    fun setStage(stage: Stage) {
        this.stage = stage
    }
    //Estados Varios
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

    //Core Controllers
    private lateinit var chartController: ChartStateController
    private lateinit var imageController: ImageStateController
    private lateinit var dataController: DataStateController

    //Action Controllers
    private lateinit var umbralizerController: UmbralizerController
    private lateinit var tonoController: TonoController
    private lateinit var ligthController: LigthController
    private lateinit var rotationController: RotationController
    private lateinit var zoomController: ZoomController

    //Graficos
    @FXML
    private lateinit var histogramChart: AreaChart<Number, Number>
    @FXML
    private lateinit var toneCurveChart: LineChart<Number, Number>
    @FXML
    private lateinit var perfilAreaChart: AreaChart<Number, Number>

    //Data
    private var matrixImage: ImageMatrix? = null
    private var originalImage: ImageMatrix? = null

    //Estado del canal visualizado en el Histograma
    @FXML
    private lateinit var histogram: ToggleGroup
    @FXML
    fun onRadioHistogramClick(event: ActionEvent) {
        matrixImage?:return
        val channel = (histogram.selectedToggle as RadioButton).text
        chartController.updateHistogram(matrixImage, channel)
    }
    //Estado del canal visualizado en la Curva Tonal
    @FXML
    private lateinit var tonalCurve: ToggleGroup
    @FXML
    fun onRadioCurveClick(event: ActionEvent) {
        matrixImage?:return
        val channel = (tonalCurve.selectedToggle as RadioButton).text
        chartController.updateCurve(originalImage,matrixImage, channel)
    }
    //Estado del canal visualizado en el Perfil de Imagen
    @FXML
    private lateinit var imagePerfil: ToggleGroup
    @FXML
    private lateinit var perfilText: TextField
    @FXML
    fun onRadioPerfilClick(event: ActionEvent) {
        matrixImage?:return
        val channel = (imagePerfil.selectedToggle as RadioButton).text
        chartController.updatePerfil(matrixImage,perfilText.text.toInt(), channel)
    }
    //Estado del Perfil de la Imagen
    @FXML
    fun onPerfilButtonClick(event: ActionEvent) {
        matrixImage?:return
        chartController.updatePerfil(matrixImage,perfilText.text.toInt(), "R")
    }

    //Leer la imagen y setea el estado inicial de la aplicacion
    @FXML
    fun onLoadImageClick(event: ActionEvent) {
        //Controladores de Imagen, Gráficos e Información
        chartController = ChartStateController(histogramChart, toneCurveChart, perfilAreaChart)
        dataController = DataStateController(dimImage, colorsImage, bppImage)
        imageController = ImageStateController(stage,applicationConsole, mainImageView, chartController, dataController)
        //Controladores de Acción
        umbralizerController = UmbralizerController()
        tonoController = TonoController()
        ligthController = LigthController()
        rotationController = RotationController()
        zoomController = ZoomController()
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
        //mainImageView.isPreserveRatio = false
        //mainImageView.fitWidth = javafx.scene.layout.Region.USE_COMPUTED_SIZE
        //mainImageView.fitHeight = javafx.scene.layout.Region.USE_COMPUTED_SIZE
    }

    //Umbral Simple
    @FXML
    private lateinit var umbralSlider: Slider
    @FXML
    fun onUmbralButtonClick(event: ActionEvent){
        matrixImage?:return
        umbralizerController.simpleUmbral(matrixImage!!, umbralSlider.value)
        imageController.changeView(matrixImage!!)
    }
    //Umbral Multiple
    @FXML
    private lateinit var umbralMultInf: TextField
    @FXML
    private lateinit var umbralMultiSup: TextField
    fun onMultiUmbralButtonClick(event: ActionEvent){
        matrixImage?:return
        umbralizerController.multiUmbral(matrixImage!!,
            umbralMultInf.text.toInt(),
            umbralMultiSup.text.toInt())
        imageController.changeView(matrixImage!!)
    }
    //Negativo de la Imagen
    @FXML
    fun onNegativeButtonClick(event: ActionEvent) {
        matrixImage?:return
        tonoController.negativeImage(matrixImage!!)
        imageController.changeView(matrixImage!!)
    }
    //Escala de Grises
    @FXML
    fun onGreyscaleButtonClick(event: ActionEvent) {
        matrixImage?:return
        tonoController.greyScale(matrixImage!!)
        imageController.changeView(matrixImage!!)
    }
    //Escala de Color
    @FXML
    private lateinit var colorScalePicker: ColorPicker
    @FXML
    fun onColorScalePickerClick(event: ActionEvent) {
        matrixImage?:return
        tonoController.colorScale(matrixImage!!, colorScalePicker)
        imageController.changeView(matrixImage!!)
    }
    //Cambio de Brillo
    @FXML
    private lateinit var  lightSlider: Slider
    @FXML
    fun onBrigthnessButtonClick(event: ActionEvent) {
        matrixImage?:return
        ligthController.brightness(matrixImage!!, lightSlider.value)
        imageController.changeView(matrixImage!!)
    }
    //Cambio de Contraste
    @FXML
    private lateinit var contrastSlider: Slider
    @FXML
    fun onConstrastButtonClick(event: ActionEvent) {
        matrixImage?:return
        ligthController.contrast(matrixImage!!, contrastSlider.value)
        imageController.changeView(matrixImage!!)
    }
    //Espejo Horizontal
    @FXML
    fun onMirrorHClick(event: ActionEvent) {
        matrixImage?:return
        matrixImage = rotationController.mirrorH(matrixImage!!)
        imageController.changeView(matrixImage!!)
    }
    //Espejo Vertical
    @FXML
    fun onMirrorVClick(event: ActionEvent) {
        matrixImage?:return
        matrixImage = rotationController.mirrorV(matrixImage!!)
        imageController.changeView(matrixImage!!)
    }
    //Rotacion 90 grados
    @FXML
    fun onRotation90Click(event: ActionEvent) {
        matrixImage?:return
        matrixImage = rotationController.rotation90(matrixImage!!)
        imageController.changeView(matrixImage!!)
    }
    //Rotacion 180 grados
    @FXML
    fun onRotation180Click(event: ActionEvent) {
        matrixImage?:return
        matrixImage = rotationController.rotation180(matrixImage!!)
        imageController.changeView(matrixImage!!)
    }
    //Rotacion 270 grados
    @FXML
    fun onRotation270Click(event: ActionEvent) {
        matrixImage?:return
        matrixImage = rotationController.rotation270(matrixImage!!)
        imageController.changeView(matrixImage!!)
    }
    //Zoom In
    @FXML
    fun onZoomInNearestClick(event: ActionEvent) {
        matrixImage?:return
        matrixImage = zoomController.zoomINN(matrixImage!!, 2)
        imageController.changeView(matrixImage!!)
    }
    @FXML
    fun onZoomInBilinearClick(event: ActionEvent) {
        matrixImage?:return
        matrixImage = zoomController.zoomInBLI(matrixImage!!, 2)
        imageController.changeView(matrixImage!!)
    }
    //Zooom Out
    @FXML
    fun onZoomOutNearestClick(event: ActionEvent) {
        matrixImage?:return
        matrixImage = zoomController.zoomOutN(matrixImage!!, 2)
        imageController.changeView(matrixImage!!)
    }
    @FXML
    fun onZoomOutSuperSampling(event: ActionEvent) {
        matrixImage?:return
        matrixImage = zoomController.zoomOutSupersampling(matrixImage!!, 2)
        imageController.changeView(matrixImage!!)
    }

    //Convoluciones
    @FXML
    fun onPromButtonClick(event: ActionEvent) {
        val controllerConvolution = ConvolutionController()
        val kernel = Kernel(5,5)

        for (y in 0 until 5) {
            for (x in 0 until 5) {
                kernel.matrix[y][x] = 1/25.0
            }
        }

        matrixImage = controllerConvolution.apply(matrixImage!!, kernel)
        imageController.changeView(matrixImage!!)
    }

    @FXML
    fun onPerfilado4Click(event: ActionEvent) {
        val kernel = Kernel(3,3).perfilado8()
        val convolutionController = ConvolutionController()
        val laplacianImage = convolutionController.apply(matrixImage!!, kernel)

        val width = matrixImage!!.width
        val height = matrixImage!!.height
        val newImage = ImageMatrix(width, height)
        newImage.maxVal = matrixImage!!.maxVal
        newImage.header = matrixImage!!.header

        val alpha = 1.0
        for (y in 0 until height) {
            for (x in 0 until width) {
                val orig = matrixImage!!.pixels[y][x]
                val lap = laplacianImage.pixels[y][x]

                val r = (orig.r + alpha * lap.r).roundToInt().coerceIn(0, 255)
                val g = (orig.g + alpha * lap.g).roundToInt().coerceIn(0, 255)
                val b = (orig.b + alpha * lap.b).roundToInt().coerceIn(0, 255)

                newImage.pixels[y][x] = Pixel(r, g, b)
            }
        }
        matrixImage = newImage
        imageController.changeView(matrixImage!!)
    }

}