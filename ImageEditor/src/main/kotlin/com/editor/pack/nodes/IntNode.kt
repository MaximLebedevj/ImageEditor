package com.editor.pack.nodes

import com.editor.pack.NodeTypes
import com.editor.pack.core.ParamFilterNode
import javafx.fxml.FXML
import javafx.scene.input.DataFormat

class IntNode(
    nodeState: DataFormat,
    linkState: DataFormat,
    id: UInt
): ParamFilterNode<Int>(nodeState, linkState, id, Regex("^[+-]?\\d+\$")) {
    @FXML
    override fun initialize() {
        super.initialize()
        value = 0
        link.valueProperty.set(value)
        editField.text = "0"
        nodeTitle.text = "Int"
    }

    override fun toValue(text: String): Int = text.toInt()
    override fun initType(): NodeTypes = NodeTypes.INT
    override fun initInputs() {
    }

}