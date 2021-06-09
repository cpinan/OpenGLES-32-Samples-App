package com.carlospinan.openglesobjectloading.model

import android.content.Context
import android.opengl.GLES32
import androidx.core.math.MathUtils
import com.carlospinan.openglesobjectloading.R
import com.carlospinan.openglesobjectloading.common.BYTES_PER_FLOAT
import com.carlospinan.openglesobjectloading.common.ObjParser
import com.carlospinan.openglesobjectloading.common.Object3D
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import java.nio.IntBuffer
import kotlin.random.Random

/**
 * @author Carlos Piñan
 */
class CustomObject(
    context: Context,
    private val objectParsed: ObjParser
) :
    Object3D(context, R.raw.vertex_with_color_shader, R.raw.fragment_color_shader) {

    private val aVertexPositionHandle by lazy {
        GLES32.glGetAttribLocation(program, "aVertexPosition")
    }

    private val aVertexColorHandle by lazy {
        GLES32.glGetAttribLocation(program, "aVertexColor")
    }

    private val uMVPMatrixHandle by lazy {
        GLES32.glGetUniformLocation(program, "uMVPMatrix")
    }

    private val coordsPerVertex = 3
    private val vertexStride = BYTES_PER_FLOAT * coordsPerVertex

    private val vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(objectParsed.vertex.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                put(objectParsed.vertex)
                position(0)
            }

    private val colorBuffer: FloatBuffer =
        ByteBuffer.allocateDirect(objectParsed.vertex.size * BYTES_PER_FLOAT)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer()
            .apply {
                var colorIndex = 0
                val colors = FloatArray(objectParsed.vertex.size)
                for (i in 0..(colors.size - coordsPerVertex) step coordsPerVertex) {
                    colors[colorIndex++] = MathUtils.clamp(Random.nextFloat(), 0.2F, 1F)
                    colors[colorIndex++] = MathUtils.clamp(Random.nextFloat(), 0.2F, 0.5F)
                    colors[colorIndex++] = MathUtils.clamp(Random.nextFloat(), 0.2F, 0.5F)
                }
                put(colors)
                position(0)
            }

    private val indexBuffer: IntBuffer =
        IntBuffer.allocate(objectParsed.order.size)
            .apply {
                put(objectParsed.order)
                position(0)
            }

    override fun draw(modelViewProjectionMatrix: FloatArray) {
        GLES32.glUseProgram(program)

        GLES32.glUniformMatrix4fv(
            uMVPMatrixHandle,
            1,
            false,
            modelViewProjectionMatrix,
            0
        )

        GLES32.glEnableVertexAttribArray(aVertexPositionHandle)
        GLES32.glEnableVertexAttribArray(aVertexColorHandle)

        GLES32.glVertexAttribPointer(
            aVertexPositionHandle,
            coordsPerVertex,
            GLES32.GL_FLOAT,
            false,
            vertexStride,
            vertexBuffer
        )

        GLES32.glVertexAttribPointer(
            aVertexColorHandle,
            coordsPerVertex,
            GLES32.GL_FLOAT,
            false,
            vertexStride,
            colorBuffer
        )

        GLES32.glDrawElements(
            GLES32.GL_TRIANGLES,
            objectParsed.order.size,
            GLES32.GL_UNSIGNED_INT,
            indexBuffer
        )

        GLES32.glDisableVertexAttribArray(aVertexPositionHandle)
        GLES32.glDisableVertexAttribArray(aVertexColorHandle)

        GLES32.glUseProgram(0)
    }

}