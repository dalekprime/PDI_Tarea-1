package controllers

import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.FileChooser
import javafx.stage.Stage
import models.ImageMatrix
import java.io.File

class ImageStateController {

    //Referencias a Información Externa
    private var stage: Stage
    private var dataLabel: Label
    private var imageView: ImageView

    //Referencias a los controladores de Gráficos e Información
    private var chartController: ChartStateController
    private var dataController: DataStateController

    //Imagen Inicial
    private lateinit var matrixImageOriginal: ImageMatrix

    constructor(stage: Stage, label: Label, view: ImageView,
                chartController: ChartStateController, dataController: DataStateController) {
        this.stage = stage
        this.dataLabel = label
        this.imageView = view
        this.chartController = chartController
        this.dataController = dataController
    }
    fun loadNewImage(): ImageMatrix?{
        val fileChooser = FileChooser().apply{
            title = "Selecionar Imagen"
            extensionFilters.add(FileChooser.ExtensionFilter(
                "Imagen",
                "*.png", "*.ppm", "*.pgm", "*.pbm", "*.bmp", "*.rle"))
            initialDirectory = File(System.getProperty("user.dir")+"/imagesTest")
        }
        val file: File? = fileChooser.showOpenDialog(stage)
        if (file == null) {
            dataLabel.text = "No se selecciono imagen"
            return null
        }
        if (!file.exists() or ((file.extension.lowercase()) !in setOf("png", "ppm", "pgm", "pbm", "bmp", "rle"))) {
            dataLabel.text = "Imagen Invalida"
            return null
        }
        dataLabel.text = "Imagen Cargada... ${file.name}"
        val matrixImage =  ImageMatrix(file)
        matrixImageOriginal = matrixImage.copy()
        changeView(matrixImage)
        return matrixImage
    }
    fun changeView(imageMatrix: ImageMatrix){
        imageView.image = imageMatrix.matrixToImage()
        //Crear primeros gráficos
        chartController.updateHistogram(imageMatrix, "R")
        chartController.updateCurve(matrixImageOriginal,imageMatrix, "R")
        //Crear la información Inicial
        dataController.update(imageMatrix)
    }
}