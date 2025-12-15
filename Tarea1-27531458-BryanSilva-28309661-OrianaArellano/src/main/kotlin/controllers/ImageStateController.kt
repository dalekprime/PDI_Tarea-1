package controllers

import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.FileChooser
import javafx.stage.Stage
import models.ImageMatrix
import java.io.File

class ImageStateController {
    fun loadImage(stage: Stage, label: Label): File? {
        val fileChooser = FileChooser().apply{
            title = "Selecionar Imagen"
            extensionFilters.add(FileChooser.ExtensionFilter(
                "Imagen",
                "*.png", "*.ppm", "*.pgm", "*.pbm"  ))
            initialDirectory = File(System.getProperty("user.dir")+"/imagesTest")
        }
        val file: File? = fileChooser.showOpenDialog(stage)
        if (file == null) {
            label.text = "No se selecciono imagen"
            return null
        }
        if (!file.exists() or ((file.extension.lowercase()) !in setOf("png", "ppm", "pgm", "pbm"))) {
            label.text = "Imagen Invalida"
            return null
        }
        return file
    }
    fun changeImage(imageView: ImageView, file: File?, label: Label): ImageMatrix?  {
        file?: return null
        val image = Image(file.toURI().toString())
        image.progressProperty().addListener { _, _, progress ->
            if (progress.toDouble() == 1.0) {
                label.text = "Imagen Cargada... ${file.absolutePath}"
            }
        }
        imageView.image = image
        return ImageMatrix(file)
    }
    fun changeView(imageView: ImageView, image: Image){
        imageView.image = image
    }
}