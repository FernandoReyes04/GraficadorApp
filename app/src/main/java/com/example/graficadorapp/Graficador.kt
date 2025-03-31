package com.example.graficadorapp

class Graficador {
    private val pila = mutableListOf<Char>()
    private val pilaEvaluacion = mutableListOf<Double>()

    // Función para convertir una expresión infija a postfija
    fun infijaAPostfija(expresion: String): String {
        var postfija = ""

        // Función para determinar la prioridad de los operadores
        fun prioridad(op: Char): Int {
            return when (op) {
                '^' -> 3
                '*', '/' -> 2
                '+', '-' -> 1
                else -> 0
            }
        }

        for (caracter in expresion) {
            when {
                caracter.isLetterOrDigit() -> postfija += caracter

                caracter == '(' -> pila.add(caracter)

                caracter == ')' -> {
                    while (pila.isNotEmpty() && pila.last() != '(') {
                        postfija += pila.removeAt(pila.size - 1)
                    }
                    pila.removeAt(pila.size - 1)
                }

                // Si es un operador
                else -> {
                    // Mientras la pila no esté vacía y el operador en la pila tenga mayor o igual prioridad
                    while (pila.isNotEmpty() && prioridad(caracter) <= prioridad(pila.last())) {
                        postfija += pila.removeAt(pila.size - 1)
                    }
                    pila.add(caracter)
                }
            }
        }


        while (pila.isNotEmpty()) {
            postfija += pila.removeAt(pila.size - 1)
        }

        return postfija
    }

    // Nueva función para evaluar un punto en la expresión postfija
    fun evaluarPunto(expresionPostfija: String, valores: Map<Char, Double>): Double {
        pilaEvaluacion.clear()

        for (caracter in expresionPostfija) {
            when {
                caracter.isDigit() -> {
                    pilaEvaluacion.add(caracter.toString().toDouble())
                }
                caracter.isLetter() -> {
                    if (!valores.containsKey(caracter)) {
                        throw IllegalArgumentException("Valor no proporcionado para '$caracter'")
                    }
                    pilaEvaluacion.add(valores[caracter]!!)
                }
                else -> {
                    if (pilaEvaluacion.size < 2) {
                        throw IllegalArgumentException("Expresión postfija inválida")
                    }

                    val operando2 = pilaEvaluacion.removeAt(pilaEvaluacion.size - 1)
                    val operando1 = pilaEvaluacion.removeAt(pilaEvaluacion.size - 1)

                    val resultado = when (caracter) {
                        '+' -> operando1 + operando2
                        '-' -> operando1 - operando2
                        '*' -> operando1 * operando2
                        '/' -> operando1 / operando2
                        '^' -> Math.pow(operando1, operando2)
                        else -> throw IllegalArgumentException("Operador no soportado: $caracter")
                    }

                    pilaEvaluacion.add(resultado)
                }
            }
        }

        if (pilaEvaluacion.size != 1) {
            throw IllegalArgumentException("Expresión postfija inválida")
        }

        return pilaEvaluacion[0]
    }


    //  Evaluar un rango de puntos
    fun evaluarRango(
        expresionPostfija: String,
        xInicial: Double,
        xFinal: Double,
        paso: Double = 0.1
    ): Array<DoubleArray> {
        val puntos = mutableListOf<DoubleArray>()
        var x = xInicial

        while (x <= xFinal) {
            val y = evaluarPunto(expresionPostfija, mapOf('x' to x))
            puntos.add(doubleArrayOf(x, y))
            x += paso
        }

        return puntos.toTypedArray()
    }
}
