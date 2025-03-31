package com.example.graficadorapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() { // Elimina la palabra "abstract"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 🔹 Buscar los elementos de la interfaz
        val etFuncion = findViewById<EditText>(R.id.etFuncion)
        val btnGraficar = findViewById<Button>(R.id.btnGraficar)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        // 🔹 Crear una instancia de Graficador
        val graficador = Graficador()

        // 🔹 Configurar el botón
        btnGraficar.setOnClickListener {
            val expresion = etFuncion.text.toString() // Obtener la expresión del usuario
            val resultado = graficador.infijaAPostfija(expresion) // Convertir a postfija
            tvResultado.text = "Postfija: $resultado" // Mostrar resultado
        }
    }
}
