package com.editor.pack.nodes

import com.editor.pack.NodeTypes
import com.editor.pack.core.FilterFilterNode
import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.input.DataFormat
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc

class GrayFilterFilterNode(nodeState: DataFormat, linkState: DataFormat, id: UInt): FilterFilterNode(nodeState, linkState, id) {

    @FXML
    override fun initialize() {
        super.initialize()
        inputs = mapOf()
        initInputs()
    }

    override fun filterFunction(img: Image): Image {
        val tmpMat = imageToMat(img)
        val resultMat = Mat()
        Imgproc.cvtColor(tmpMat, resultMat, Imgproc.COLOR_RGB2GRAY)
        return matToImage(resultMat)
    }

    override fun setTitle() {
        nodeTitle.text = "Gray Filter"
    }

    override fun initType(): NodeTypes = NodeTypes.GRAY
    override fun initInputs() {
        linkInputs.addAll(
            listOf(
                imageInput,
            )
        )
    }
}