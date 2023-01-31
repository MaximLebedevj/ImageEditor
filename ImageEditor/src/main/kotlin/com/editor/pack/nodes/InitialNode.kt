package com.editor.pack.nodes

import com.editor.pack.NodeTypes
import javafx.fxml.FXML
import javafx.scene.input.DataFormat

class InitialNode(nodeState: DataFormat, linkState: DataFormat, id: UInt): ImageSelectNode(nodeState, linkState, id) {
    @FXML
    override fun initialize() {
        super.initialize()
        nodeTitle.text = "Initial Node"
        grid.children.remove(deleteButton)
    }

    override fun initType(): NodeTypes = NodeTypes.INITIAL
}