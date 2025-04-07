package com.example.graficadorapp

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etFuncion = findViewById<EditText>(R.id.etFuncion)
        val etValores = findViewById<EditText>(R.id.etValores)
        val etXInicial = findViewById<EditText>(R.id.etXInicial)
        val etXFinal = findViewById<EditText>(R.id.etXFinal)
        val etPaso = findViewById<EditText>(R.id.etPaso)
        val btnGraficar = findViewById<Button>(R.id.btnGraficar)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)

        val graficador = Graficador()

        // Función original para parsear valores (sin cambios)
        fun parsearValores(valoresStr: String): Map<Char, Double> {
            if (valoresStr.isEmpty()) return emptyMap()

            val valores = mutableMapOf<Char, Double>()
            val pares = valoresStr.split(',')

            for (par in pares) {
                try {
                    val partes = par.split('=')
                    if (partes.size != 2) continue

                    val variable = partes[0].trim()[0]
                    val valor = partes[1].trim().toDouble()
                    valores[variable] = valor
                } catch (e: Exception) {
                    continue
                }
            }
            return valores
        }

        btnGraficar.setOnClickListener {
            try {
                val expresion = etFuncion.text.toString().trim()
                if (expresion.isEmpty()) {
                    Toast.makeText(this, "Ingrese una función", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val postfija = graficador.infijaAPostfija(expresion)
                val valores = parsearValores(etValores.text.toString().trim())
                val resultado = StringBuilder()

                // Evaluación de punto único (si hay valores)
                if (valores.isNotEmpty()) {
                    val y = graficador.evaluarPunto(postfija, valores)
                    resultado.append("Evaluación puntual:\n")
                    valores.forEach { (k, v) -> resultado.append("$k = $v\n") }
                    resultado.append("Resultado: y = $y\n\n")
                }

                // Evaluación por rango (si hay valores de rango)
                val xInicial = etXInicial.text.toString().toDoubleOrNull()
                val xFinal = etXFinal.text.toString().toDoubleOrNull()

                if (xInicial != null && xFinal != null) {
                    val paso = etPaso.text.toString().toDoubleOrNull() ?: 0.1
                    val puntos = graficador.evaluarRango(postfija, xInicial, xFinal, paso)

                    resultado.append("Evaluación por rango:\n")
                    resultado.append("De x=$xInicial a x=$xFinal (paso=$paso)\n")
                    resultado.append("Total puntos: ${puntos.size}\n\n")

                    // Mostrar primeros y últimos 3 puntos
                    puntos.take(3).forEach {
                        resultado.append("x=${it[0]}, y=${it[1]}\n")
                    }
                    if (puntos.size > 6) resultado.append("...\n")
                    puntos.takeLast(3).forEach {
                        resultado.append("x=${it[0]}, y=${it[1]}\n")
                    }

                    val graficaView = findViewById<GraficadorView>(R.id.graficaView)
                    graficaView.setPuntos(puntos)
                }

                tvResultado.text = resultado.toString()

            } catch (e: Exception) {
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}