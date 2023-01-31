package com.editor.pack

import com.editor.pack.core.DraggableNode
import com.editor.pack.core.LinkInput
import com.editor.pack.core.NodeLink
import com.editor.pack.core.Scene
import com.editor.pack.nodes.*
import javafx.scene.control.Button
import javafx.scene.input.DataFormat
import javafx.scene.layout.AnchorPane
import javafx.stage.FileChooser
import javafx.stage.Stage
import nu.pattern.OpenCV
import java.awt.image.BufferedImage
import java.io.File
import javafx.fxml.FXML

class Controller {
    private val nodeState = DataFormat("nodeState")
    private val linkState = DataFormat("linkState")
    private var scene = Scene(nodeState, linkState, 0u)

    @FXML
    private lateinit var floatBButton: Button

    @FXML
    private lateinit var intBButton: Button

    @FXML
    private lateinit var stringBButton: Button

    @FXML
    private lateinit var addImageBButton: Button

    @FXML
    private lateinit var imageBButton: Button

    @FXML
    private lateinit var sepiaBButton: Button

    @FXML
    private lateinit var addTextBButton: Button

    @FXML
    private lateinit var grayFilterBButton: Button

    @FXML
    private lateinit var brightnessFilterBButton: Button

    @FXML
    private lateinit var blurFilterBButton: Button

    @FXML
    private lateinit var invertFilterBButton: Button

    @FXML
    private lateinit var moveBButton: Button

    @FXML
    private lateinit var scaleBButton: Button

    @FXML
    private lateinit var rotateBButton: Button

    @FXML
    private lateinit var saveMenuItem: Button

    @FXML
    private lateinit var openMenuItem: Button

    @FXML
    private lateinit var sceneContainer: AnchorPane

    fun initialize() {
        OpenCV.loadLocally()

        addNode(InitialNode(nodeState, linkState, scene.getId()).also { it.layoutY = 130.0 }.also { it.layoutX = 100.0 })
        addNode(ResultNode(nodeState, linkState, scene.getId()).also { it.layoutY = 130.0 }.also { it.layoutX = 950.0 })

       floatBButton.setOnAction {
           println("Float Node was created")
           addNode(FloatNode(nodeState, linkState, scene.getId()))
       }

       intBButton.setOnAction {
            println("Int Node was Created")
            addNode(IntNode(nodeState, linkState, scene.getId()))
        }

       stringBButton.setOnAction {
            println("String Node was created")
            addNode(StringNode(nodeState, linkState, scene.getId()))
        }

       addImageBButton.setOnAction {
           println("Add Image Node was created")
           addNode(AddImageFilterNode(nodeState, linkState, scene.getId()))
       }

       sepiaBButton.setOnAction {
            println("Sepia Node was created")
            addNode(SepiaFilterNode(nodeState, linkState, scene.getId()))
        }

       addTextBButton.setOnAction {
            println("Add Text Node was created")
            addNode(AddTextFilterNode(nodeState, linkState, scene.getId()))
        }

       grayFilterBButton.setOnAction {
            println("Gray Filter Node was created")
            addNode(GrayFilterFilterNode(nodeState, linkState, scene.getId()))
        }

       imageBButton.setOnAction {
            println("Image Node was created")
            addNode(ImageSelectNode(nodeState, linkState, scene.getId()))
        }

       brightnessFilterBButton.setOnAction {
           println("Brightness Node was created")
           addNode(BrightnessFilterNode(nodeState, linkState, scene.getId()))
       }

       blurFilterBButton.setOnAction {
           println("Blur Node was created")
           addNode(BlurFilterNode(nodeState, linkState, scene.getId()))
       }

       invertFilterBButton.setOnAction {
           println("Invert Node was created")
           addNode(InvertFilterNode(nodeState, linkState, scene.getId()))
       }

       rotateBButton.setOnAction {
           println("Rotate Node was created")
           addNode(RotationFilterNode(nodeState, linkState, scene.getId()))
       }

       scaleBButton.setOnAction {
           println("Scale Node was created")
           addNode(ScaleFilterNode(nodeState, linkState, scene.getId()))
       }

       moveBButton.setOnAction {
           println("Move Node was created")
           addNode(MoveFilterNode(nodeState, linkState, scene.getId()))
       }

        openMenuItem.setOnAction {
            val fileChooser = FileChooser()
            val extensionFilter = FileChooser.ExtensionFilter("JSON files (*.json)", "*.json")

            fileChooser.extensionFilters.add(extensionFilter)

            val file = fileChooser.showOpenDialog(sceneContainer.scene.window as Stage)
            val json = file.readText()
            scene = scene.load(json)
            clearScene()

            val nodesIterator = scene.nodes.iterator()
            for(node in nodesIterator) {
                sceneContainer.children.add(node)
            }

            loadLinks()
        }

       saveMenuItem.setOnAction {
           val json = scene.save()
           val fileChooser = FileChooser()
           val extensionFilter = FileChooser.ExtensionFilter("JSON files (*.json)", "*.json")

           fileChooser.extensionFilters.add(extensionFilter)

           var file = fileChooser.showSaveDialog(sceneContainer.scene.window as Stage)

           if (file.nameWithoutExtension == file.name) {
               file = File(file.parentFile, file.nameWithoutExtension + ".json")
           }

           file.writeText(json)
       }
    }

    private fun <T> addNode(node: DraggableNode<T>) {
        node.onNodeRemovedCallback = {
            scene.remove(it)
        }
        sceneContainer.children.add(node)
        scene.add(node)
    }


    private fun clearScene() {
        sceneContainer.children.clear()
    }

    private fun loadLinks() {
        for (nodeConnections in scene.connections) {
            val node = scene.findNodeById(nodeConnections.id.toUInt())
            node?.let {
                for (connectionKey in nodeConnections.connections) {
                    val connectedNode = scene.findNodeById(connectionKey.nodeId.toUInt())
                    connectedNode.let {
                        val connectedLink = connectedNode!!.link
                        val currentInput = node.linkInputs[connectionKey.inputId]
                        when {
                            connectedLink.valueProperty.value is Int? && currentInput.valueProperty.value is Int? ->
                                node.loadLink(connectedLink as NodeLink<Int?>, currentInput as LinkInput<Int?>)

                            connectedLink.valueProperty.value is Float? && currentInput.valueProperty.value is Float? ->
                                node.loadLink(connectedLink as NodeLink<Float?>, currentInput as LinkInput<Float?>)

                            connectedLink.valueProperty.value is String? && currentInput.valueProperty.value is String? ->
                                node.loadLink(connectedLink as NodeLink<String?>, currentInput as LinkInput<String?>)

                            connectedLink.valueProperty.value is BufferedImage? && currentInput.valueProperty.value is BufferedImage? ->
                                node.loadLink(
                                    connectedLink as NodeLink<BufferedImage?>,
                                    currentInput as LinkInput<BufferedImage?>
                                )
                        }
                    }
                }
            }
        }
    }
}