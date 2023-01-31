package com.editor.pack.core

import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.scene.shape.Circle

class LinkOutput<T>: AnchorPane() {
    init {
        setPrefSize(20.0, 20.0)
        setMaxSize(20.0, 20.0)
        val circle = Circle(7.0, Color.RED)
        circle.translateX = 20.0
        circle.translateY = 10.0
        children.add(circle)
    }
}