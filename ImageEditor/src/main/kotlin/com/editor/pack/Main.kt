package com.editor.pack

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage

class Main : Application() {
    override fun start(stage: Stage) {
        val fxmlLoader = FXMLLoader(Main::class.java.getResource("Application.fxml"))
        val scene = Scene(fxmlLoader.load())
        stage.scene = scene
        stage.isMaximized = true
        stage.title = "Node Based Image Editor"
        stage.icons.add(Image("C:\\Users\\jnpw2\\Desktop\\ImageEditor\\ImageEditor\\src\\main\\kotlin\\com\\editor\\pack\\images\\icon.png"));
        stage.show()
    }
}

fun main() {
    Application.launch(Main::class.java)
}