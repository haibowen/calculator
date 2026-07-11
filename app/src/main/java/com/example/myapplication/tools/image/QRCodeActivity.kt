package com.example.myapplication.tools.image

import android.content.ContentValues
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.myapplication.R
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

class QRCodeActivity : AppCompatActivity() {

    private lateinit var etInput: EditText
    private lateinit var btnGenerate: Button
    private lateinit var ivQrcode: ImageView
    private lateinit var btnSave: Button
    private var currentBitmap: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qrcode)

        etInput = findViewById(R.id.et_input)
        btnGenerate = findViewById(R.id.btn_generate)
        ivQrcode = findViewById(R.id.iv_qrcode)
        btnSave = findViewById(R.id.btn_save)

        btnGenerate.setOnClickListener { generate() }
        btnSave.setOnClickListener { saveToGallery() }
    }

    private fun generate() {
        val text = etInput.text.toString()
        if (text.isBlank()) return

        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, 512, 512)
        val bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.RGB_565)
        for (x in 0 until 512) {
            for (y in 0 until 512) {
                bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
            }
        }
        ivQrcode.setImageBitmap(bitmap)
        currentBitmap = bitmap
        btnSave.visibility = android.view.View.VISIBLE
    }

    private fun saveToGallery() {
        val bitmap = currentBitmap ?: return
        val filename = "QR_${System.currentTimeMillis()}.png"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val values = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            uri?.let {
                contentResolver.openOutputStream(it)?.use { os -> bitmap.compress(Bitmap.CompressFormat.PNG, 100, os) }
            }
        }
        Toast.makeText(this, "已保存到相册", Toast.LENGTH_SHORT).show()
    }
}
