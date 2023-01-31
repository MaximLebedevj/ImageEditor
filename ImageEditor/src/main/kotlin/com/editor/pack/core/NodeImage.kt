package com.editor.pack.core

import javafx.scene.image.ImageView
import javafx.scene.input.DataFormat
import javafx.scene.layout.GridPane
import java.awt.image.BufferedImage
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader

abstract class NodeImage(nodeState: DataFormat, linkState: DataFormat, id: UInt):
    ValueFilterNode<BufferedImage>(nodeState, linkState, id, FXMLLoader(NodeImage::class.java.getResource("Node.fxml")))
{
    lateinit var image: ImageView

    @FXML
    lateinit var grid: GridPane

    @FXML
    override fun initialize() {
        super.initialize()
        valueProperty = SimpleObjectProperty()

        valueProperty.addListener { _, _, newValue -> link.valueProperty.set(newValue)
            value = newValue
        }
    }
}