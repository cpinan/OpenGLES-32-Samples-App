package com.carlospinan.openglesobjectloading

import android.content.Context
import android.opengl.GLES32
import android.opengl.Matrix
import android.os.SystemClock
import android.util.Log
import com.carlospinan.openglesobjectloading.common.BaseGLRenderer
import com.carlospinan.openglesobjectloading.common.parseObjFile
import com.carlospinan.openglesobjectloading.model.CustomObject
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * @author Carlos Piñan
 */
class MainRenderer(
    context: Context,
    private val objResource: Int,
) : BaseGLRenderer(context) {

    var x = 0F
    var zoom = 0F

    // Used to project into 2D screen. For shader program
    private val modelViewProjectionMatrix = FloatArray(16)

    // Basic frustum. Check matrix projection
    private val projectionMatrix = FloatArray(16)

    // For camera view. SetLookAt
    private val viewMatrix = FloatArray(16)

    // To allocate the result between viewMatrix * modelMatrix
    private val modelViewMatrix = FloatArray(16)

    /**
     * Store the model matrix.
     * This matrix is used to move models from object space (where each model can be thought
     * of being located at the center of the universe) to world space.
     */
    private val modelMatrix = FloatArray(16)

    private lateinit var customObject: CustomObject

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        update(objResource)

        GLES32.glEnable(GLES32.GL_DEPTH_TEST)
        GLES32.glDepthFunc(GLES32.GL_LEQUAL)
        GLES32.glEnable(GLES32.GL_CULL_FACE)
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES32.glViewport(0, 0, width, height)

        val ratio = width.toFloat() / height

        Matrix.frustumM(
            projectionMatrix,
            0,
            -ratio,
            ratio,
            -1F,
            1F,
            1F,
            1000F
        )
    }

    override fun onDrawFrame(gl: GL10?) {
        // Do a complete rotation every 10 seconds.
        val time = SystemClock.uptimeMillis() % 10000L
        val angleInDegrees = 360.0f / 10000.0f * time

        GLES32.glClearColor(0F, 0F, 0F, 1F)
        GLES32.glClearDepthf(1F)

        GLES32.glClear(GLES32.GL_COLOR_BUFFER_BIT or GLES32.GL_DEPTH_BUFFER_BIT)

        setupCamera()

        Matrix.setIdentityM(modelViewProjectionMatrix, 0)
        Matrix.setIdentityM(modelViewMatrix, 0)
        Matrix.setIdentityM(modelMatrix, 0)

        // MODEL MATRIX CALCULATION
        Matrix.translateM(
            modelMatrix,
            0,
            x,
            0F,
            -5F + zoom
        )

        val rotationYMatrix = FloatArray(16)
        Matrix.setRotateM(
            rotationYMatrix,
            0,
            angleInDegrees,
            0F, 1F, 0F
        )

        val rotationXMatrix = FloatArray(16)
        Matrix.setRotateM(
            rotationXMatrix,
            0,
            angleInDegrees,
            1F, 0F, 0F
        )

        Matrix.multiplyMM(
            modelMatrix,
            0,
            modelMatrix,
            0,
            rotationYMatrix,
            0
        )

        Matrix.multiplyMM(
            modelMatrix,
            0,
            modelMatrix,
            0,
            rotationXMatrix,
            0
        )

        // MODEL VIEW MATRIX
        Matrix.multiplyMM(
            modelViewMatrix,
            0,
            viewMatrix,
            0,
            modelMatrix,
            0
        )

        // FINAL MATRIX FOR PROJECTION
        Matrix.multiplyMM(
            modelViewProjectionMatrix, // productMatrix
            0,
            projectionMatrix,
            0,
            modelViewMatrix,
            0
        )

        customObject.draw(modelViewProjectionMatrix)
    }

    private fun setupCamera() {
        // Position the eye behind the origin.
        // val eyeX = 0F
        // val eyeY = 0F
        // val eyeZ = -4F
        val eyeX = 0F
        val eyeY = 0F
        val eyeZ = 4F

        // We are looking toward the distance
        val lookX = 0F
        val lookY = 0F
        val lookZ = 0F

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        val upX = 0.0F
        val upY = 1.0F
        val upZ = 0.0F

        Matrix.setLookAtM(
            viewMatrix,
            0,
            eyeX,
            eyeY,
            eyeZ,
            lookX,
            lookY,
            lookZ,
            upX,
            upY,
            upZ
        )
    }

    private fun update(selectedResource: Int) {
        val objParser = parseObjFile(context, selectedResource)
        Log.d("objParser", "${objParser.id} ; ${objParser.name}")
        customObject = CustomObject(context, objParser)
    }
}