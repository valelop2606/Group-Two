package com.example.grouptwo

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class PantallaDeInicio : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pantalla_de_inicio) // <-- tu xml de arriba

        val imgMojito: ImageView = findViewById(R.id.imgMojito)
        imgMojito.setOnClickListener {
            val i = Intent(this, VerRecetaDetallada::class.java)
            startActivity(i)
        }
    }
}
