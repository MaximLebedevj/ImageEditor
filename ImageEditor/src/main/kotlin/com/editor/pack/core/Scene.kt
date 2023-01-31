package com.editor.pack.core

import com.editor.pack.*
import com.google.gson.GsonBuilder
import javafx.scene.input.DataFormat

class Scene(val nodeState: DataFormat, val linkState: DataFormat, private var currentId: UInt) {
    val nodes: MutableList<DraggableNode<*>> = mutableListOf()
    val connections: MutableList<InputLinksState> = mutableListOf()

    fun add(node: DraggableNode<*>) {
        nodes.add(node)
    }

    fun remove(node: DraggableNode<*>) {
        nodes.remove(node)
    }

    fun findNodeById(id: UInt): DraggableNode<*>? {
        for(node in nodes) {
            if (node.id == id) {
                return node
            }
        }
        return null
    }

    fun getId(): UInt {
        return currentId.also { currentId += 1u }
    }

    fun save(): String {
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(Scene::class.java, SceneSerializer())
            .registerTypeAdapter(DraggableNodeState::class.java, DraggableNodeSerializer())
            .create()

        println(gson.toJson(this))
        return gson.toJson(this)
    }

    fun load(json: String): Scene {
        val gson = GsonBuilder()
            .registerTypeAdapter(Scene::class.java, SceneDeserializer(nodeState, linkState))
            .registerTypeAdapter(DraggableNode::class.java, DraggableNodeDeserializer(nodeState, linkState))
            .create()

        return gson.fromJson(json, Scene::class.java)

    }
}