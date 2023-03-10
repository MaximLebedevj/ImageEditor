package com.editor.pack

import com.editor.pack.nodes.ResultNode
import javafx.embed.swing.SwingFXUtils
import javafx.geometry.Pos
import javafx.scene.image.ImageView
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.StackPane

class Prerender(parent: ResultNode): AnchorPane() {
    private val imageView = ImageView()

    init {
        imageView.isPreserveRatio = true
        this.children.add(imageView)
        StackPane.setAlignment(imageView, Pos.CENTER)
        imageView.image = SwingFXUtils.toFXImage(parent.valueProperty.value, null)

        parent.valueProperty.addListener {
            _, _, newValue ->
            imageView.image = SwingFXUtils.toFXImage(newValue, null)
        }
    }
}