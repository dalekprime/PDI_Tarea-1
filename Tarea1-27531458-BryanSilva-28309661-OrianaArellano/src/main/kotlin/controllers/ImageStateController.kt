package controllers

import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.FileChooser
import javafx.stage.Stage
import models.ImageMatrix
import java.io.File

class ImageStateController {

    private var stage: Stage
    private var dataLabel: Label
    private var imageView: ImageView

    constructor(stage: Stage, label: Label, view: ImageView) {
        this.stage = stage
        this.dataLabel = label
        this.imageView = view
    }
    fun loadNewImage(): ImageMatrix?{
        val fileChooser = FileChooser().apply{
            title = "Selecionar Imagen"
            extensionFilters.add(FileChooser.ExtensionFilter(
                "Imagen",
                "*.png", "*.ppm", "*.pgm", "*.pbm", "*.bmp"))
            initialDirectory = File(System.getProperty("user.dir")+"/imagesTest")
        }
        val file: File? = fileChooser.showOpenDialog(stage)
        if (file == null) {
            dataLabel.text = "No se selecciono imagen"
            return null
        }
        if (!file.exists() or ((file.extension.lowercase()) !in setOf("png", "ppm", "pgm", "pbm", "bmp"))) {
            dataLabel.text = "Imagen Invalida"
            return null
        }
        dataLabel.text = "Imagen Cargada... ${file.absolutePath}"
        val matrixImage =  ImageMatrix(file)
        imageView.image = matrixImage.matrixToImage()
        return matrixImage
    }
    fun changeView(image: Image){
        imageView.image = image
    }
}