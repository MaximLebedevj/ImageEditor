package com.editor.pack.nodes

import com.editor.pack.NodeTypes
import com.editor.pack.core.FilterFilterNode
import com.editor.pack.core.LinkInput
import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.input.DataFormat

class BrightnessFilterNode(nodeState: DataFormat, linkState: DataFormat, id: UInt) : FilterFilterNode(nodeState, linkState, id) {
    private lateinit var levelInput: LinkInput<Int?>

    @FXML
    override fun initialize() {
        super.initialize()
        levelInput = LinkInput(null, this)

        inputs = mapOf(
            Pair(levelInput, "Level")
        )
        initInputs()
        addInputs(3)
        bindInputs()
    }

    override fun setTitle() {
        nodeTitle.text = "Brightness"
    }

    override fun filterFunction(img: Image): Image {
        val resultMat = imageToMat(img)
        imageToMat(img).convertTo(resultMat, -1, 1.0, levelInput.valueProperty.value!!.toDouble())
        return matToImage(resultMat)
    }

    override fun initType(): NodeTypes = NodeTypes.BRIGHTNESS
    override fun initInputs() {
       linkInputs.addAll(
           listOf(
               imageInput,
               levelInput,
           )
       )
    }
}