package com.carlospinan.openglesobjectloading.common

/**
 * @author Carlos Piñan
 */

import android.content.Context

/**
 * @author Carlos Piñan
 */
const val BYTES_PER_FLOAT = 4

/**
 * @author Carlos Piñan
 */
abstract class Object3D(
    context: Context,
    vertexResourceId: Int,
    fragmentResourceId: Int
) {

    protected var program = 0
        private set

    init {
        program = createProgram(
            context,
            vertexResourceId,
            fragmentResourceId,
            "program has failed in Primitive.kt"
        )
    }

    abstract fun draw(modelViewProjectionMatrix: FloatArray)

}