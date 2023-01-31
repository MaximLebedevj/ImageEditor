package com.editor.pack.nodes

import com.editor.pack.NodeTypes
import com.editor.pack.core.ParamFilterNode
import javafx.fxml.FXML
import javafx.scene.input.DataFormat

class StringNode(
    nodeState: DataFormat,
    linkState: DataFormat,
    id: UInt
): ParamFilterNode<String>(nodeState, linkState, id, Regex("^[\\s\\S]*")) {

    @FXML
    override fun initialize() {
        super.initialize()
        value = ""
        link.valueProperty.set(value)
        editField.text = ""
        nodeTitle.text = "String"
    }

    override fun toValue(text: String): String = text
    override fun initType(): NodeTypes = NodeTypes.STRING
    override fun initInputs() {
    }
}