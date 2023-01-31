package com.editor.pack.nodes

import com.editor.pack.NodeTypes
import com.editor.pack.core.FilterFilterNode
import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.input.DataFormat
import java.awt.Color

class InvertFilterNode(nodeState: DataFormat, linkState: DataFormat, id: UInt): FilterFilterNode(nodeState, linkState, id) {

    @FXML
    override fun initialize() {
        super.initialize()
        inputs = mapOf()
        initInputs()
    }

    override fun filterFunction(img: Image): Image {
        val bufferedImage = SwingFXUtils.fromFXImage(img, null)

        for (x in 0 until bufferedImage.width) {
            for (y in 0 until bufferedImage.height) {
                val rgba = bufferedImage.getRGB(x, y)
                val color = Color(rgba, true)
                val newColor = Color(
                    255 - color.red,
                    255 - color.green,
                    255 - color.blue
                )
                bufferedImage.setRGB(x, y, newColor.rgb)
            }
        }

        return SwingFXUtils.toFXImage(bufferedImage, null)
    }

    override fun setTitle() {
        nodeTitle.text = "Invert"
    }

    override fun initType(): NodeTypes = NodeTypes.INVERT
    override fun initInputs() {
        linkInputs.addAll(
            listOf(
                imageInput,
            )
        )
    }

}