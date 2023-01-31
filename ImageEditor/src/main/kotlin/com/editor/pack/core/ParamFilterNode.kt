package com.editor.pack.core

import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.scene.control.TextField
import javafx.scene.input.DataFormat

abstract class ParamFilterNode<T>(
    nodeState: DataFormat,
    linkState: DataFormat,
    id: UInt,
    private val validatorRegex: Regex
    ): ValueFilterNode<T>(nodeState, linkState, id, FXMLLoader(ParamFilterNode::class.java.getResource("ParamNode.fxml"))
) {
    private lateinit var valueOutput: LinkOutput<T>

    @FXML
    lateinit var editField: TextField

    @FXML
    override fun initialize() {
        super.initialize()
        valueOutput = LinkOutput()
        valueOutput.onDragDetected = linkDragDetected
        outputLayout.children.add(valueOutput)
        initOutput()

        draggedArea.onDragDetected = dragDetected
        editField.textProperty().addListener {
                _, _, new ->
            validatorRegex
            if (!new.matches(validatorRegex)) {
                valueOutput.onDragDetected = null
            }
            else {
                valueOutput.onDragDetected = linkDragDetected
                value = toValue(editField.text)
                link.valueProperty.set(value)
            }
        }
    }

    override fun load(_x: Double, _y: Double, _value: T?) {
        super.load(_x, _y, _value)
        editField.textProperty().set(_value.toString())
    }

    override fun initOutput() {
        output = valueOutput
    }

    abstract fun toValue(text: String): T
}