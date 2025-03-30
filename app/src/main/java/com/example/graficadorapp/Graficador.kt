package com.example.graficadorapp

class Graficador {
    private val pila = mutableListOf<Char>()

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
}
