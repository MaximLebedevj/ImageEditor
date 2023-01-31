package com.editor.pack

import com.editor.pack.core.DraggableNode
import com.google.gson.*
import com.editor.pack.core.Scene
import com.editor.pack.nodes.*
import javafx.geometry.Bounds
import javafx.scene.input.DataFormat
import java.awt.image.BufferedImage
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.Serializable
import java.lang.reflect.Type
import javax.imageio.ImageIO

class DraggableNodeState(node: DraggableNode<*>) {
    val position: Bounds = node.boundsInParent
    val id = node.id
    val type = node.type
    val value = node.value
}

data class InputLinksState (
    val id: Int,
    val connections: MutableList<LinkKey<Int, Int>>
): Serializable

data class LinkKey<T: Serializable, E: Serializable>(
    val nodeId: T,
    val inputId: E
): Serializable

fun JsonArray.toByteArray(): ByteArray {
    val byteArray = ByteArray(size())
    for(i in 0 until size()) {
        byteArray[i] = (get(i).asInt and 0xFF).toByte()
    }
    return byteArray
}

class DraggableNodeSerializer: JsonSerializer<DraggableNodeState> {
    override fun serialize(src: DraggableNodeState?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val result = JsonObject()
        if (src == null) return result

        val position = src.position
        val typeToString = src.type.toString()
        result.addProperty("x", position.minX)
        result.addProperty("y", position.minY)
        result.addProperty("id", src.id.toString())
        result.addProperty("type", typeToString)


        if(src.value is BufferedImage?) {
            var writableImage: ByteArray? = null
            if (src.value != null) {
                val byteStream = ByteArrayOutputStream()
                ImageIO.write(src.value, "png", byteStream)
                writableImage = byteStream.toByteArray()
            }
            result.add("value", context!!.serialize(writableImage))
        }
        if (src.value is Int || src.value is String || src.value is Float) {
            result.addProperty("value", src.value.toString())
        }

        return result
    }
}

class DraggableNodeDeserializer(
    val nodeState: DataFormat,
    val linkState: DataFormat,
): JsonDeserializer<DraggableNode<*>>
{
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): DraggableNode<*>? {
        val jsonObject = json?.asJsonObject

        return jsonObject?.let { jo ->
            val id = jo.get("id").asInt.toUInt()
            val x = jo.get("x").asDouble
            val y = jo.get("y").asDouble

            when(jo.get("type").asString) {
                NodeTypes.INT.toString() -> {
                    val value = jo.get("value").asInt
                    IntNode(nodeState, linkState, id).also { it.load(x, y, value) }
                }
                NodeTypes.FLOAT.toString() -> {
                    val value = jo.get("value").asFloat
                    FloatNode(nodeState, linkState, id).also { it.load(x, y, value) }
                }
                NodeTypes.STRING.toString() -> {
                    val value = jo.get("value").asString
                    StringNode(nodeState, linkState, id).also {it.load(x, y, value)}
                }
                NodeTypes.IMAGE.toString() ->  {
                    val value = jo.get("value")
                    val bufImage: BufferedImage? = if (value == null) null
                    else ImageIO.read(ByteArrayInputStream(jo.getAsJsonArray("value").toByteArray()))

                    ImageSelectNode(nodeState, linkState, id).also { it.load(x, y, bufImage) }
                }
                NodeTypes.RESULT.toString() -> {
                    ResultNode(nodeState, linkState, id).also { it.load(x, y, null) }
                }
                NodeTypes.INITIAL.toString() -> {
                    val value = jo.get("value")
                    val bufImage: BufferedImage? = if (value == null) null
                    else ImageIO.read(ByteArrayInputStream(jo.getAsJsonArray("value").toByteArray()))

                    InitialNode(nodeState, linkState, id).also { it.load(x, y, bufImage) }
                }
                NodeTypes.ADDIMAGE.toString() -> {
                    AddImageFilterNode(nodeState, linkState, id).also { it.load(x, y, null) }
                }
                NodeTypes.ADDTEXT.toString() -> {
                    AddTextFilterNode(nodeState, linkState, id).also { it.load(x, y, null) }
                }
                NodeTypes.BLUR.toString() -> {
                    BlurFilterNode(nodeState, linkState, id).also { it.load(x, y, null) }
                }
                NodeTypes.BRIGHTNESS.toString() -> {
                    BrightnessFilterNode(nodeState, linkState, id).also { it.load(x, y, null) }
                }
                NodeTypes.GRAY.toString() -> {
                    GrayFilterFilterNode(nodeState, linkState, id).also { it.load(x, y, null) }
                }
                NodeTypes.INVERT.toString() -> {
                    InvertFilterNode(nodeState, linkState, id).also { it.load(x, y, null) }
                }
                NodeTypes.MOVE.toString() -> {
                    MoveFilterNode(nodeState, linkState, id).also { it.load(x, y, null) }
                }
                NodeTypes.ROTATION.toString() -> {
                    RotationFilterNode(nodeState, linkState, id).also { it.load(x, y, null) }
                }
                NodeTypes.SCALE.toString() -> {
                    ScaleFilterNode(nodeState, linkState, id).also { it.load(x, y, null) }
                }
                NodeTypes.SEPIA.toString() -> {
                    SepiaFilterNode(nodeState, linkState, id).also { it.load(x, y, null) }
                }
                else -> null
            }

        }
    }
}

class SceneSerializer: JsonSerializer<Scene> {
    override fun serialize(src: Scene?, typeOfSrc: Type?, context: JsonSerializationContext?): JsonElement {
        val result = JsonObject()
        if (src == null || context == null) return result
        val nodes = JsonArray()
        val connections = JsonArray()

        for (node in src.nodes) {
            val nodeState = DraggableNodeState(node)
            val serializedNode = context.serialize(nodeState)
            nodes.add(serializedNode)

            val nodeConnections = node.connectedLinks
            for (connection in nodeConnections) {
                val currentConnections = mutableListOf<LinkKey<Int, Int>>()
                currentConnections.add(
                    LinkKey(connection.source.id.toInt(), node.linkInputs.indexOf(connection.destination))
                )
                connections.add(context.serialize(InputLinksState(node.id.toInt(), currentConnections)))
            }
        }

        result.addProperty("currentId", src.getId().toInt())
        result.add("connections", connections)
        result.add("nodes", nodes)

        return result
    }
}

class SceneDeserializer(val nodeState: DataFormat, val linkState: DataFormat): JsonDeserializer<Scene> {
    override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?): Scene? {
        val jsonObject = json?.asJsonObject
        if(context == null || json == null) return null

        return jsonObject?.let { jo ->
            val currentId = jo.get("currentId").asInt.toUInt()
            val scene = Scene(nodeState, linkState, currentId)
            val nodes = jo.get("nodes").asJsonArray
            for(node in nodes) {
                scene.add(context.deserialize(node, DraggableNode::class.java))
            }

            val jsonConnections = jo.getAsJsonArray("connections")

            for (jsonNodeConnections in jsonConnections) {
                scene.connections.add(context.deserialize(jsonNodeConnections, InputLinksState::class.java))

            }
            scene
        }
    }
}


