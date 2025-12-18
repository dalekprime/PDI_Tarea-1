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
import models.Pixel
import models.Kernel
import javax.swing.Spring.height
import javax.swing.Spring.width

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

        // Configuración del ImageView para que se adapte al tamaño real de la imagen
        //mainImageView.isPreserveRatio = false
        //mainImageView.fitWidth = javafx.scene.layout.Region.USE_COMPUTED_SIZE
        //mainImageView.fitHeight = javafx.scene.layout.Region.USE_COMPUTED_SIZE
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
    //Umbral Multiple
    @FXML
    private lateinit var umbralMultInf: TextField
    @FXML
    private lateinit var umbralMultiSup: TextField
    fun onMultiUmbralButtonClick(event: ActionEvent){
        val threshold1 = umbralMultInf.text.toInt()
        val threshold2 = umbralMultiSup.text.toInt()
        val width = matrixImage!!.width
        val height = matrixImage!!.height
        for (y in 0 until height) {
            for (x in 0 until width) {
                val grey = (0.299*matrixImage!!.pixels[y][x].r + 0.587*matrixImage!!.pixels[y][x].g + 0.114*matrixImage!!.pixels[y][x].b)
                if(threshold1 > grey && grey < threshold2){
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

    @FXML
    fun onMirrorHClick(event: ActionEvent) {
        val width = matrixImage!!.width
        val height = matrixImage!!.height
        val newImage = ImageMatrix(width, height)
        newImage.maxVal = matrixImage!!.maxVal
        newImage.header = matrixImage!!.header
        for (y in 0 until height) {
            for (x in 0 until width) {
                newImage.pixels[y][x] = matrixImage!!.pixels[y][width-1-x]
            }
        }
        matrixImage = newImage
        imageController.changeView(matrixImage!!)
    }

    @FXML
    fun onMirrorVClick(event: ActionEvent) {
        val width = matrixImage!!.width
        val height = matrixImage!!.height
        val newImage = ImageMatrix(width, height)
        newImage.maxVal = matrixImage!!.maxVal
        newImage.header = matrixImage!!.header
        for (y in 0 until height) {
            for (x in 0 until width) {
                newImage.pixels[y][x] = matrixImage!!.pixels[height-1-y][x]
            }
        }
        matrixImage = newImage
        imageController.changeView(matrixImage!!)
    }

    @FXML
    fun onRotation90Click(event: ActionEvent) {
        val width = matrixImage!!.width
        val height = matrixImage!!.height
        val newImage = ImageMatrix(height, width)
        newImage.maxVal = matrixImage!!.maxVal
        newImage.header = matrixImage!!.header
        for (y in 0 until width) {
            for (x in 0 until height) {
                newImage.pixels[y][x] = matrixImage!!.pixels[height-1-x][y]
            }
        }
        matrixImage = newImage
        imageController.changeView(matrixImage!!)
    }

    @FXML
    fun onRotation180Click(event: ActionEvent) {
        val width = matrixImage!!.width
        val height = matrixImage!!.height
        val newImage = ImageMatrix(width, height)
        newImage.maxVal = matrixImage!!.maxVal
        newImage.header = matrixImage!!.header
        for (y in 0 until height) {
            for (x in 0 until width) {
                newImage.pixels[y][x] = matrixImage!!.pixels[height-1-y][width-1-x]
            }
        }
        matrixImage = newImage
        imageController.changeView(matrixImage!!)
    }

    @FXML
    fun onRotation270Click(event: ActionEvent) {
        val width = matrixImage!!.width
        val height = matrixImage!!.height
        val newImage = ImageMatrix(height, width)
        newImage.maxVal = matrixImage!!.maxVal
        newImage.header = matrixImage!!.header
        for (y in 0 until width) {
            for (x in 0 until height) {
                newImage.pixels[y][x] = matrixImage!!.pixels[x][width-1-y]
            }
        }
        matrixImage = newImage
        imageController.changeView(matrixImage!!)
    }

    @FXML

    fun onZoomInNearestClick(event: ActionEvent) {
        val width = matrixImage!!.width
        val height = matrixImage!!.height
        val factor = 2
        val newImage = ImageMatrix(width * factor, height * factor)
        newImage.maxVal = matrixImage!!.maxVal
        newImage.header = matrixImage!!.header
        for (y in 0 until newImage.height) {
            val y1 = (y.toDouble() / factor + 0.5).toInt().coerceIn(0, height-1)
            for (x in 0 until newImage.width) {
                val x1 = (x.toDouble() / factor + 0.5).toInt().coerceIn(0, width-1)
                newImage.pixels[y][x] = matrixImage!!.pixels[y1][x1]
            }
        }
        matrixImage = newImage
        imageController.changeView(matrixImage!!)
    }

    @FXML
    fun onZoomInBilinearClick(event: ActionEvent) {
        val width = matrixImage!!.width
        val height = matrixImage!!.height
        val factor = 2
        val newImage = ImageMatrix(width * factor, height * factor)
        newImage.maxVal = matrixImage!!.maxVal
        newImage.header = matrixImage!!.header

        fun rangeX(x: Int) = x.coerceIn(0, width-1)
        fun rangeY(y: Int) = y.coerceIn(0, height-1)

        for (y in 0 until newImage.height) {
            val v = y.toDouble() / factor
            val j = v.toInt()
            val b = v - j
            for (x in 0 until newImage.width) {
                val u = x.toDouble() / factor
                val i = u.toInt()
                val a = u - i

                val p00 = matrixImage!!.pixels[rangeY(j)][rangeX(i)]
                val p10 = matrixImage!!.pixels[rangeY(j)][rangeX(i+1)]
                val p01 = matrixImage!!.pixels[rangeY(j+1)][rangeX(i)]
                val p11 = matrixImage!!.pixels[rangeY(j+1)][rangeX(i+1)]

                fun mix(c00: Int, c10: Int, c01: Int, c11: Int): Int {
                    val r0 = (1-a)*c00 + a*c10
                    val r1 = (1-a)*c01 + a*c11
                    val c = (1-b)*r0 + b*r1
                    return c.toInt().coerceIn(0,255)
                }

                newImage.pixels[y][x] = Pixel(
                    mix(p00.r, p10.r, p01.r, p11.r),
                    mix(p00.g, p10.g, p01.g, p11.g),
                    mix(p00.b, p10.b, p01.b, p11.b)
                )
            }
        }
        matrixImage = newImage
        imageController.changeView(matrixImage!!)
    }

    @FXML
    fun onZoomOutNearestClick(event: ActionEvent) {
        val width = matrixImage!!.width
        val height = matrixImage!!.height
        val factor = 2

        if (width / factor < 1 || height / factor < 1) {
            println("Tamaño mínimo alcanzado.")
            return
        }
        val newImage = ImageMatrix(width / factor, height / factor)
        newImage.maxVal = matrixImage!!.maxVal
        newImage.header = matrixImage!!.header

        for (y in 0 until newImage.height) {
            val y1 = y * factor
            for (x in 0 until newImage.width) {
                val x1 = x * factor
                newImage.pixels[y][x] = matrixImage!!.pixels[y1][x1]
            }
        }
        matrixImage = newImage
        imageController.changeView(matrixImage!!)
    }

    @FXML
    fun onZoomOutBilinearClick(event: ActionEvent) {
        val width = matrixImage!!.width
        val height = matrixImage!!.height
        val factor = 2

        if (width / factor < 1 || height / factor < 1) {
            println("Tamaño mínimo alcanzado.")
            return
        }

        val newImage = ImageMatrix(width / factor, height / factor)
        newImage.maxVal = matrixImage!!.maxVal
        newImage.header = matrixImage!!.header

        fun rangeX(x: Int) = x.coerceIn(0, width - 1)
        fun rangeY(y: Int) = y.coerceIn(0, height - 1)

        for (y in 0 until newImage.height) {
            val v = y.toDouble() * factor
            val j = v.toInt()
            val b = v - j
            for (x in 0 until newImage.width) {
                val u = x.toDouble() * factor
                val i = u.toInt()
                val a = u - i

                val p00 = matrixImage!!.pixels[rangeY(j)][rangeX(i)]
                val p10 = matrixImage!!.pixels[rangeY(j)][rangeX(i + 1)]
                val p01 = matrixImage!!.pixels[rangeY(j + 1)][rangeX(i)]
                val p11 = matrixImage!!.pixels[rangeY(j + 1)][rangeX(i + 1)]

                fun mix(c00: Int, c10: Int, c01: Int, c11: Int): Int {
                    val r0 = (1 - a) * c00 + a * c10
                    val r1 = (1 - a) * c01 + a * c11
                    val c = (1 - b) * r0 + b * r1
                    return c.toInt().coerceIn(0, 255)
                }

                newImage.pixels[y][x] = Pixel(
                    mix(p00.r, p10.r, p01.r, p11.r),
                    mix(p00.g, p10.g, p01.g, p11.g),
                    mix(p00.b, p10.b, p01.b, p11.b)
                )
            }
        }
        matrixImage = newImage

        fun onPromButtonClick(event: ActionEvent) {
            val controllerConvolution = ConvolutionController()
            val kernel = Kernel(10, 10)
            for (y in 0 until 10) {
                for (x in 0 until 10) {
                    kernel.matrix[y][x] = 1 / 100.00
                }
            }
            matrixImage = controllerConvolution.apply(matrixImage!!, kernel)
            imageController.changeView(matrixImage!!)
        }
    }
}