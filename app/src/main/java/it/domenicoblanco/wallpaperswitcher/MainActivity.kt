package it.domenicoblanco.wallpaperswitcher

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import java.io.*

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private val _imgPicker = Intent(Intent.ACTION_GET_CONTENT)
    private val _intentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            when (lastClicked) {
                R.id.darkButton -> {
                    val imageFile = saveFile(it?.data?.data, "dark")
                    darkImage.setImageURI(imageFile.toUri())
                }
                R.id.lightButton -> {
                    val imageFile = saveFile(it?.data?.data, "light")
                    lightImage.setImageURI(imageFile.toUri())
                }
            }
            recreate()
        }
    }

    private lateinit var darkImage: ImageView
    private lateinit var lightImage: ImageView
    private lateinit var darkButton: Button
    private lateinit var lightButton: Button

    private var lastClicked = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val int = Intent(applicationContext, ChangeTheme::class.java)
            startService(int)
        }

        darkButton = findViewById(R.id.darkButton)
        lightButton = findViewById(R.id.lightButton)
        darkImage = findViewById(R.id.darkImage)
        lightImage = findViewById(R.id.lightImage)
        darkButton.setOnClickListener(this)
        lightButton.setOnClickListener(this)

        _imgPicker.type = "image/*"

        if (fileList().contains("dark")) {
            darkImage.setImageURI(File(filesDir, "dark").toUri())
        }
        if (fileList().contains("light")) {
            lightImage.setImageURI(File(filesDir, "light").toUri())
        }
    }

    private fun saveFile(sourceUri: Uri?, fileName: String): File {
        if (sourceUri != null) {
            val inS = contentResolver.openInputStream(sourceUri)
            val res = inS?.readBytes()
            inS?.close()

            val outS = openFileOutput(fileName, MODE_PRIVATE)
            outS.write(res)
            outS.close()
        }

        return File(filesDir, fileName)
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            lastClicked = p0.id
        }

        _intentLauncher.launch(_imgPicker)
    }
}