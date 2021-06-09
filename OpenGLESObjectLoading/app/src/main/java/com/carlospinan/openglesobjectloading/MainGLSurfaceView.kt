package com.carlospinan.openglesobjectloading

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

private const val ZOOM_FACTOR = 2F
private const val TRANSLATE_FACTOR = 2F

/**
 * @author Carlos Piñan
 */
class MainGLSurfaceView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : GLSurfaceView(context, attrs) {

    private lateinit var mainRenderer: MainRenderer

    fun zoomIn() {
        mainRenderer.zoom += ZOOM_FACTOR
    }

    fun zoomOut() {
        mainRenderer.zoom -= ZOOM_FACTOR
    }

    fun moveLeft() {
        mainRenderer.x -= TRANSLATE_FACTOR
    }

    fun moveRight() {
        mainRenderer.x += TRANSLATE_FACTOR
    }

    fun setup(resource: Int) {
        mainRenderer = MainRenderer(context, resource)

        setEGLContextClientVersion(3)
        setRenderer(mainRenderer)
    }

}