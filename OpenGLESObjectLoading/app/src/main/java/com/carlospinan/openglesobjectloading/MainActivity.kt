package com.carlospinan.openglesobjectloading

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.carlospinan.openglesobjectloading.databinding.MainLayoutBinding

enum class MenuType {
    GL_OBJECT,
    LINK
}

data class MenuElement(
    val textResource: Int,
    val relatedObject: Int,
    val type: MenuType
)

val menuElements = arrayListOf(
    MenuElement(R.string.source_code, R.string.source_url, MenuType.LINK),
    MenuElement(R.string.shape_cube, R.raw.cube, MenuType.GL_OBJECT),
    MenuElement(R.string.shape_cylinder, R.raw.cylinder, MenuType.GL_OBJECT),
    MenuElement(R.string.shape_ico_sphere, R.raw.icosphere, MenuType.GL_OBJECT),
    MenuElement(R.string.shape_monkey, R.raw.monkey, MenuType.GL_OBJECT),
    MenuElement(R.string.shape_plane, R.raw.plane, MenuType.GL_OBJECT),
    MenuElement(R.string.shape_sphere, R.raw.sphere, MenuType.GL_OBJECT),
    MenuElement(R.string.shape_torus, R.raw.torus, MenuType.GL_OBJECT),
    MenuElement(R.string.shape_tank, R.raw.tank, MenuType.GL_OBJECT),
    MenuElement(R.string.shape_dragon, R.raw.dragon_sample, MenuType.GL_OBJECT),
)

private var selectedResource = menuElements[1].relatedObject

/**
 * @author Carlos Piñan
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = MainLayoutBinding.inflate(layoutInflater)

        with(binding) {
            setContentView(root)

            objectView.setup(selectedResource)

            leftButton.setOnClickListener {
                objectView.moveLeft()
            }

            rightButton.setOnClickListener {
                objectView.moveRight()
            }

            zoomInButton.setOnClickListener {
                objectView.zoomIn()
            }

            zoomOutButton.setOnClickListener {
                objectView.zoomOut()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        for (element in menuElements) {
            menu.add(element.textResource)
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        for (element in menuElements) {
            // Dummy Validation.
            if (item.title == getString(element.textResource) &&
                element.relatedObject != selectedResource
            ) {
                if (element.type == MenuType.GL_OBJECT) {
                    selectedResource = element.relatedObject
                    recreate()
                } else {
                    val openUrlIntent =
                        Intent(Intent.ACTION_VIEW, Uri.parse(getString(element.relatedObject)))
                    startActivity(openUrlIntent)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}