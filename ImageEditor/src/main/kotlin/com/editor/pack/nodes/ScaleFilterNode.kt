package com.editor.pack.nodes

import com.editor.pack.NodeTypes
import com.editor.pack.core.FilterFilterNode
import com.editor.pack.core.LinkInput
import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.input.DataFormat
import java.awt.geom.AffineTransform
import java.awt.image.AffineTransformOp
import java.awt.image.BufferedImage
import kotlin.math.floor

class ScaleFilterNode(nodeState: DataFormat, linkState: DataFormat, id: UInt): FilterFilterNode(nodeState, linkState, id){
    private lateinit var xInput: LinkInput<Float?>
    private lateinit var yInput: LinkInput<Float?>

    @FXML
    override fun initialize() {
        super.initialize()

        xInput = LinkInput(null, this)
        yInput = LinkInput(null, this)

        inputs = mapOf(
            Pair(xInput, "x"),
            Pair(yInput, "y")
        )

        initInputs()
        addInputs(3)
        bindInputs()
    }

    override fun filterFunction(img: Image): Image {
        val bufferedImage = SwingFXUtils.fromFXImage(img, null)
        val xFactor = xInput.valueProperty.value!!
        val yFactor = yInput.valueProperty.value!!

        var scaledImage = BufferedImage(
            floor(bufferedImage.width * xFactor).toInt(),
            floor(bufferedImage.height * yFactor).toInt(),
            BufferedImage.TYPE_INT_ARGB
        )

        val affineTransform = AffineTransform.getScaleInstance(xFactor.toDouble(), yFactor.toDouble())
        val scaleOp = AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BICUBIC)
        scaledImage = scaleOp.filter(bufferedImage, scaledImage)

        return SwingFXUtils.toFXImage(scaledImage, null)
    }

    override fun setTitle() {
        nodeTitle.text = "Scale"
    }

    override fun initType(): NodeTypes = NodeTypes.SCALE
    override fun initInputs() {
        linkInputs.addAll(
            listOf(
                imageInput,
                xInput,
                yInput
            )
        )
    }
}