package com.editor.pack.core

import javafx.embed.swing.SwingFXUtils
import javafx.fxml.FXML
import javafx.scene.control.Label
import javafx.scene.image.Image
import javafx.scene.input.DataFormat
import javafx.scene.layout.RowConstraints
import javafx.scene.text.Font
import java.awt.image.BufferedImage

abstract class FilterFilterNode(
    nodeState: DataFormat,
    linkState: DataFormat,
    id: UInt
): NodeImage(nodeState, linkState, id) {
    lateinit var imageInput: LinkInput<BufferedImage>
    private lateinit var imageOutput: LinkOutput<BufferedImage>
    lateinit var inputs: Map<LinkInput<*>, String>

    @FXML
    override fun initialize() {
        super.initialize()
        setTitle()
        imageInput = LinkInput(null, this)
        imageInput.valueProperty.addListener {
                _, _, newValue ->
            newValue?.let {
                val filteredImage = filterImage(SwingFXUtils.toFXImage(newValue, null))
                filteredImage?.let {
                    valueProperty.value = SwingFXUtils.fromFXImage(filteredImage, null)
                    link.valueProperty.value = SwingFXUtils.fromFXImage(filteredImage, null)
                    image.image = filteredImage
                }
            }
        }
        imageInput.onDragDropped = linkDragDropped
        grid.add(imageInput, 0, 2)

        imageOutput = LinkOutput()
        imageOutput.onDragDetected = linkDragDetected
        initOutput()
        grid.add(imageOutput, 2, 2)
    }

    protected fun bindInputs() {
        for(input in inputs) {
            input.key.onDragDropped = linkDragDropped
            input.key.valueProperty.addListener {
                    _, _, _ ->
                val filteredImage = filterImage(
                    SwingFXUtils.toFXImage(imageInput.valueProperty.value, null)
                )
                filteredImage.let {
                    valueProperty.value = SwingFXUtils.fromFXImage(filteredImage, null)
                    link.valueProperty.value = SwingFXUtils.fromFXImage(filteredImage, null)
                }
                image.image = filteredImage
            }
        }
    }

    protected fun addInputs(startRow: Int) {
        var currentRow = startRow
        inputs.forEach {
                entry ->
            grid.rowConstraints.add(RowConstraints(60.0))
            grid.add(entry.key, 0, currentRow)
            grid.add(Label(entry.value).also { it.font = Font(14.0) }, 1, currentRow)
            currentRow += 1
        }
    }

    open fun filterImage(img: Image?): Image? {
        for (input in inputs) {
            if (input.key.valueProperty.value == null) return null
        }
        if (img == null) return null
        return filterFunction(img)
    }

    override fun initOutput() {
        output = imageOutput
    }

    abstract fun filterFunction(img: Image): Image
    abstract fun setTitle()
}