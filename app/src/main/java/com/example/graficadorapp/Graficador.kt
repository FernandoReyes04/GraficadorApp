package com.example.graficadoraapp

import kotlin.math.*

class Graficador {
    private val pila = mutableListOf<Char>()
    private val pilaEvaluacion = mutableListOf<Double>()

    fun infijaAPostfija(expresion: String): String {
        val expLimpia = expresion.replace(" ", "")
            .replace("pi", "P")
            .replace("e", "E")
        
        var postfija = ""
        var i = 0
        
        fun prioridad(op: Char): Int {
            return when (op) {
                '^' -> 3
                '*', '/' -> 2
                '+', '-' -> 1
                else -> 0
            }
        }

        while (i < expLimpia.length) {
            val caracter = expLimpia[i]
            when {
                caracter.isLetterOrDigit() || caracter == '.' -> {
                    postfija += caracter
                    if (i + 1 >= expLimpia.length || (!expLimpia[i+1].isLetterOrDigit() && expLimpia[i+1] != '.')) {
                        postfija += " "
                    }
                }
                caracter == '(' -> pila.add(caracter)
                caracter == ')' -> {
                    while (pila.isNotEmpty() && pila.last() != '(') {
                        postfija += pila.removeAt(pila.size - 1) + " "
                    }
                    if (pila.isNotEmpty()) pila.removeAt(pila.size - 1)
                }
                else -> {
                    while (pila.isNotEmpty() && prioridad(caracter) <= prioridad(pila.last())) {
                        postfija += pila.removeAt(pila.size - 1) + " "
                    }
                    pila.add(caracter)
                }
            }
            i++
        }
        while (pila.isNotEmpty()) {
            postfija += pila.removeAt(pila.size - 1) + " "
        }
        return postfija.trim()
    }

    fun evaluarPunto(expresionPostfija: String, valores: Map<Char, Double>): Double {
        pilaEvaluacion.clear()
        val tokens = expresionPostfija.split(" ").filter { it.isNotEmpty() }

        for (token in tokens) {
            when {
                token[0].isDigit() || (token.length > 1 && token[0] == '-' && token[1].isDigit()) -> {
                    pilaEvaluacion.add(token.toDouble())
                }
                token == "P" -> pilaEvaluacion.add(PI)
                token == "E" -> pilaEvaluacion.add(E)
                token.length == 1 && token[0].isLetter() -> {
                    val variable = token[0]
                    if (!valores.containsKey(variable)) {
                        throw IllegalArgumentException("Falta valor para '$variable'")
                    }
                    pilaEvaluacion.add(valores[variable]!!)
                }
                else -> {
                    if (pilaEvaluacion.size < 2) throw IllegalArgumentException("Error en expresión")
                    val op2 = pilaEvaluacion.removeAt(pilaEvaluacion.size - 1)
                    val op1 = pilaEvaluacion.removeAt(pilaEvaluacion.size - 1)

                    val res = when (token[0]) {
                        '+' -> op1 + op2
                        '-' -> op1 - op2
                        '*' -> op1 * op2
                        '/' -> if (op2 == 0.0) throw ArithmeticException("División por cero") else op1 / op2
                        '^' -> op1.pow(op2)
                        else -> throw IllegalArgumentException("Operador desconocido")
                    }
                    pilaEvaluacion.add(res)
                }
            }
        }
        return if (pilaEvaluacion.size == 1) pilaEvaluacion[0] else throw IllegalArgumentException("Error fatal")
    }

    fun evaluarRango(
        expresionPostfija: String,
        xIni: Double,
        xFin: Double,
        paso: Double = 0.1,
        valoresExtra: Map<Char, Double> = emptyMap()
    ): Array<DoubleArray> {
        val puntos = mutableListOf<DoubleArray>()
        var x = xIni
        val realPaso = if (paso <= 0) 0.1 else paso
        
        while (x <= xFin + 0.00001) {
            try {
                val todosLosValores = valoresExtra.toMutableMap()
                todosLosValores['x'] = x
                val y = evaluarPunto(expresionPostfija, todosLosValores)
                if (y.isFinite()) {
                    puntos.add(doubleArrayOf(x, y))
                }
            } catch (e: Exception) {
                // Ignorar puntos inválidos
            }
            x += realPaso
        }
        return puntos.toTypedArray()
    }
}
