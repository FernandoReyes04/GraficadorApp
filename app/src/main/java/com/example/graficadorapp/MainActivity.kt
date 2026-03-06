package com.example.graficadoraapp

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
        val btnEjemplo = findViewById<Button>(R.id.btnEjemplo)
        val btnLimpiar = findViewById<Button>(R.id.btnLimpiar)
        val tvResultado = findViewById<TextView>(R.id.tvResultado)
        val graficaView = findViewById<GraficadorView>(R.id.graficaView)

        val graficador = Graficador()

        fun parsearValores(valoresStr: String): Map<Char, Double> {
            val valores = mutableMapOf<Char, Double>()
            if (valoresStr.isEmpty()) return valores
            try {
                val pares = valoresStr.split(',')
                for (par in pares) {
                    val partes = par.split('=')
                    if (partes.size == 2) {
                        val variable = partes[0].trim()[0]
                        val valor = partes[1].trim().toDouble()
                        valores[variable] = valor
                    }
                }
            } catch (e: Exception) {}
            return valores
        }

        btnLimpiar.setOnClickListener {
            etFuncion.text.clear()
            etValores.text.clear()
            etXInicial.text.clear()
            etXFinal.text.clear()
            etPaso.text.clear()
            tvResultado.text = ""
            graficaView.setPuntos(arrayOf())
        }

        btnEjemplo.setOnClickListener {
            etFuncion.setText("a * x^2 + b")
            etValores.setText("a=1, b=-4")
            etXInicial.setText("-5")
            etXFinal.setText("5")
            etPaso.setText("0.5")
            btnGraficar.performClick()
            Toast.makeText(this, "Ejemplo cargado correctamente", Toast.LENGTH_SHORT).show()
        }

        btnGraficar.setOnClickListener {
            try {
                val expresion = etFuncion.text.toString().trim()
                if (expresion.isEmpty()) {
                    etFuncion.error = "Campo obligatorio"
                    return@setOnClickListener
                }

                val xIni = etXInicial.text.toString().toDoubleOrNull()
                val xFin = etXFinal.text.toString().toDoubleOrNull()
                
                if (xIni == null || xFin == null) {
                    Toast.makeText(this, "Falta rango X", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val postfija = graficador.infijaAPostfija(expresion)
                val valoresExtra = parsearValores(etValores.text.toString().trim())
                val paso = etPaso.text.toString().toDoubleOrNull() ?: 0.1
                
                // IMPORTANTE: Ahora pasamos los valores extra (a, b, etc.) al evaluar el rango
                val puntos = graficador.evaluarRango(postfija, xIni, xFin, paso, valoresExtra)

                if (puntos.isEmpty()) {
                    tvResultado.text = "⚠️ No se generaron puntos.\nVerifica si la función usa variables que no definiste."
                    graficaView.setPuntos(arrayOf())
                } else {
                    graficaView.setPuntos(puntos)
                    
                    val info = StringBuilder()
                    info.append("GRAFICADA: $expresion\n")
                    info.append("Puntos generados: ${puntos.size}\n")
                    info.append("---------------------------------\n")
                    info.append("LISTADO DE PUNTOS (x, y):\n")
                    
                    // Mostrar la lista completa de puntos como pediste
                    for (p in puntos) {
                        info.append("x: ${String.format("%.2f", p[0])} | y: ${String.format("%.2f", p[1])}\n")
                    }
                    
                    tvResultado.text = info.toString()
                }

            } catch (e: Exception) {
                tvResultado.text = "❌ Error:\n${e.message}"
            }
        }
    }
}
