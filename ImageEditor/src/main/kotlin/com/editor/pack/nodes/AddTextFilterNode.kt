package com.editor.pack.nodes

import com.editor.pack.NodeTypes
import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.input.DataFormat
import java.awt.Color
import java.awt.Font
import com.editor.pack.core.FilterFilterNode
import com.editor.pack.core.LinkInput
import javafx.embed.swing.SwingFXUtils

class AddTextFilterNode(
    nodeState: DataFormat,
    linkState: DataFormat,
    id: UInt
): FilterFilterNode(nodeState, linkState, id) {
    private lateinit var xInput: LinkInput<Int?>
    private lateinit var yInput: LinkInput<Int?>
    private lateinit var textInput: LinkInput<String?>

    @FXML
    override fun initialize() {
        super.initialize()
        xInput = LinkInput(null, this)
        yInput = LinkInput(null, this)
        textInput = LinkInput(null, this)

        inputs = mapOf(
            Pair(xInput, "x"),
            Pair(yInput, "y"),
            Pair(textInput, "Text")
        )

        initInputs()
        addInputs(3)
        bindInputs()
    }

    override fun filterFunction(img: Image): Image {
        val bufferedImage = SwingFXUtils.fromFXImage(img, null)
        val font = Font("Arial", Font.BOLD, 100)
        val graphics = bufferedImage.graphics
        graphics.font = font
        graphics.color = Color.BLACK
        graphics.drawString(
            textInput.valueProperty.value!!,
            xInput.valueProperty.value!!,
            yInput.valueProperty.value!!
        )

        return SwingFXUtils.toFXImage(bufferedImage, null)
    }

    override fun setTitle() {
        nodeTitle.text = "Add Text"
    }

    override fun initType(): NodeTypes = NodeTypes.ADDTEXT

    override fun initInputs() {
       linkInputs.addAll(
           listOf(
               imageInput,
               xInput,
               yInput,
               textInput
           )
       )
    }

}