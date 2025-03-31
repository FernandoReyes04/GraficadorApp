package com.example.graficadorapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // 🔹 Buscar los elementos de la interfaz
        val etFuncion = findViewById<EditText>(R.id.etFuncion)
        val etValores = findViewById<EditText>(R.id.etValores)
        val btnGraficar = findViewById<Button>(R.id.btnGraficar)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        // 🔹 Crear una instancia de Graficador
        val graficador = Graficador()

        // Crear instancia para el parse de los valores
        fun parsearValores(valoresStr: String): Map<Char, Double> {
            val valores = mutableMapOf<Char, Double>()
            val pares = valoresStr.split(',')

            for (par in pares) {
                val partes = par.split('=')
                if (partes.size != 2) {
                    throw IllegalArgumentException("Formato incorrecto. Use: a=1,b=2")
                }

                val variable = partes[0].trim()[0]
                val valor = partes[1].trim().toDouble()

                valores[variable] = valor
            }

            return valores
        }

        // 🔹 Configurar el botón
        btnGraficar.setOnClickListener {
            try {
                val expresion = etFuncion.text.toString().trim()
                val valoresStr = etValores.text.toString().trim()

                if (expresion.isEmpty() || valoresStr.isEmpty()) {
                    Toast.makeText(this, "Ingrese la función y los valores", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }

                val valores = parsearValores(valoresStr)
                val postfija = graficador.infijaAPostfija(expresion)
                val resultado = graficador.evaluarPunto(postfija, valores)

                tvResultado.text = """
                Expresión: $expresion
                Postfija: $postfija
                Valores: ${valores.entries.joinToString { "${it.key}=${it.value}" }}
                Resultado: $resultado
                    """.trimIndent()

            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }

    }
}