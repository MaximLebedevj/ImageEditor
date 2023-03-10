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
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin

class RotationFilterNode(nodeState: DataFormat, linkState: DataFormat, id: UInt): FilterFilterNode(nodeState, linkState, id){
    private lateinit var angleInput: LinkInput<Float?>

    @FXML
    override fun initialize() {
        super.initialize()

        angleInput = LinkInput(null, this)

        inputs = mapOf(
            Pair(angleInput, "Angle")
        )

        initInputs()
        addInputs(3)
        bindInputs()
    }

    override fun filterFunction(img: Image): Image {
        val bufferedImage = SwingFXUtils.fromFXImage(img, null)
        val rad = Math.toRadians(angleInput.valueProperty.value!!.toDouble())
        val sin = abs(sin(rad))
        val cos = abs(cos(rad))
        val width = floor(bufferedImage.width * cos + bufferedImage.height * sin).toInt()
        val height = floor(bufferedImage.height * cos + bufferedImage.width * sin).toInt()
        val rotatedImage = BufferedImage(width, height, bufferedImage.type)
        val affineTransform = AffineTransform()

        affineTransform.translate(width / 2.0, height / 2.0)
        affineTransform.rotate(rad, 0.0, 0.0)
        affineTransform.translate(-bufferedImage.width / 2.0, -bufferedImage.height / 2.0)
        val rotateOp = AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR)
        rotateOp.filter(bufferedImage, rotatedImage)
        return SwingFXUtils.toFXImage(rotatedImage, null)
    }

    override fun setTitle() {
        nodeTitle.text = "Rotate"
    }

    override fun initType(): NodeTypes = NodeTypes.ROTATION
    override fun initInputs() {
        linkInputs.addAll(
            listOf(
                imageInput,
                angleInput
            )
        )
    }
}