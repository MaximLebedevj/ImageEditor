package com.editor.pack.nodes

import com.editor.pack.NodeTypes
import com.editor.pack.core.FilterFilterNode
import com.editor.pack.core.LinkInput
import javafx.fxml.FXML
import javafx.scene.image.Image
import javafx.scene.input.DataFormat
import org.opencv.core.Mat
import org.opencv.core.Size
import org.opencv.imgproc.Imgproc
import javafx.scene.image.WritablePixelFormat
import org.opencv.core.CvType
import org.opencv.core.MatOfByte
import org.opencv.imgcodecs.Imgcodecs
import java.io.ByteArrayInputStream

fun imageToMat(image: Image): Mat {
    val width: Int = image.width.toInt()
    val height: Int = image.height.toInt()
    val buffer = ByteArray(width * height * 4)

    val pixelReader = image.pixelReader
    val format = WritablePixelFormat.getByteBgraInstance()
    pixelReader.getPixels(0, 0, width, height, format, buffer, 0, width * 4)

    val res = Mat(height, width, CvType.CV_8UC4)
    res.put(0, 0, buffer)
    return res
}

fun matToImage(mat: Mat): Image {
    val buffer = MatOfByte()
    Imgcodecs.imencode(".png", mat, buffer)
    return Image(ByteArrayInputStream(buffer.toArray()))
}


class BlurFilterNode(
    nodeState: DataFormat,
    linkState: DataFormat,
    id: UInt
): FilterFilterNode(nodeState, linkState, id) {
    private lateinit var blurValue: LinkInput<Int?>

    @FXML
    override fun initialize() {
        super.initialize()
        blurValue = LinkInput(null, this)

        inputs = mapOf(
            Pair(blurValue, "Value")
        )

        initInputs()
        addInputs(3)
        bindInputs()
    }

    override fun filterFunction(img: Image): Image {
        val src = imageToMat(img)
        val res = Mat()
        var blurValue = blurValue.valueProperty.value!!
        if (blurValue > 500) blurValue = 500
        if (blurValue % 2 == 0) blurValue += 1

        Imgproc.GaussianBlur(src, res, Size(blurValue.toDouble(), blurValue.toDouble()), 0.0)
        return matToImage(res)
    }

    override fun setTitle() {
        nodeTitle.text = "Blur Filter"
    }

    override fun initType(): NodeTypes = NodeTypes.BLUR
    override fun initInputs() {
        linkInputs.addAll(
            listOf(
                imageInput,
                blurValue
            )
        )

    }

}