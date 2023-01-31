package com.editor.pack.core

import javafx.beans.binding.Bindings
import javafx.beans.binding.When
import javafx.beans.property.DoubleProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.geometry.Point2D
import javafx.scene.layout.AnchorPane
import javafx.scene.shape.CubicCurve

class NodeLink<T>(val source: DraggableNode<T>) : AnchorPane() {
    @FXML
    lateinit var link: CubicCurve

    private val offsetX = SimpleDoubleProperty()
    private val offsetY = SimpleDoubleProperty()
    private val offsetDirX1 = SimpleDoubleProperty()
    private val offsetDirX2 = SimpleDoubleProperty()
    private val offsetDirY1 = SimpleDoubleProperty()
    private val offsetDirY2 = SimpleDoubleProperty()
    val valueProperty = SimpleObjectProperty<T>()

    var isConnected: Boolean = false

    var destination: LinkInput<T>? = null

    @FXML
    fun initialize() {
        offsetX.set(100.0)
        offsetY.set(50.0)

        valueProperty.addListener {
                _, _, newValue ->

            println(newValue)
            destination?.valueProperty?.set(newValue)
        }

        offsetDirX1.bind(
            When(link.startXProperty().greaterThan(link.endXProperty())).then(-1.0).otherwise(1.0)
        )

        offsetDirX2.bind(
            When(link.startXProperty().greaterThan(link.endXProperty())).then(1.0).otherwise(-1.0)
        )

        link.controlX1Property().bind(Bindings.add(link.startXProperty(), offsetX.multiply(offsetDirX1)))
        link.controlX2Property().bind(Bindings.add(link.endXProperty(), offsetX.multiply(offsetDirX2)))
        link.controlY1Property().bind(Bindings.add(link.startYProperty(), offsetY.multiply(offsetDirY1)))
        link.controlY2Property().bind(Bindings.add(link.endYProperty(), offsetY.multiply(offsetDirY2)))

    }

    fun setEnd(point: Point2D) {
        link.endX = point.x
        link.endY = point.y
    }

    fun bindStart(source: LinkOutput<*>) {
        bindLayoutProperty(source, link.startXProperty(), link.startYProperty())
    }

    fun <T> bindEnd(source: LinkInput<T>) {
        bindLayoutProperty(source, link.endXProperty(), link.endYProperty())
    }

    fun unbindEnd() {
        link.endXProperty().unbind()
        link.endYProperty().unbind()
        setEnd(Point2D(link.startX, link.startY))
        destination?.connectedLink = null
        destination?.parent?.connectedLinks?.remove(this)
        destination?.valueProperty?.set(destination?.defaultValue)
        destination = null
    }

    private fun bindLayoutProperty(source: AnchorPane, propertyX: DoubleProperty, propertyY: DoubleProperty) {
        val currentBindingX =
            Bindings.add(source.layoutXProperty(), source.width / 2.0)
                .add(source.parent.layoutXProperty())
                .add(source.parent.parent.layoutXProperty())
                .add(source.parent.parent.parent.layoutXProperty())

        val currentBindingY =
            Bindings.add(source.layoutYProperty(), source.height / 2.0)
                .add(source.parent.layoutYProperty())
                .add(source.parent.parent.layoutYProperty())
                .add(source.parent.parent.parent.layoutYProperty())

        propertyX.bind(currentBindingX)
        propertyY.bind(currentBindingY)
    }

    init {
        val fxmlLoader = FXMLLoader(
            javaClass.getResource("Link.fxml")
        )
        fxmlLoader.setRoot(this)
        fxmlLoader.setController(this)
        fxmlLoader.load<Any>()
    }
}