package com.example.graficadorapp

// el tamano es el valor maximo de elementos que puede alcanzar.
// la cima es el indice del ultimo elemento.
class Pila(val tam:Int, var elementos:Array<Char>) {
    var cima = elementos.size

    fun crearPila(e:Array<Char>) {
        if (e.size < tam) {
            elementos = e
            cima = e.size
        } else {
            println("La pila solo va a tomar hasta los ultimos $tam elementos")
            elementos = e.takeLast(tam).toTypedArray() // se achica la pila para que no haya desbordamiento
        }
        cima = elementos.size
    }

    fun insertar(elemento: Char) {
        if (cima < tam) {
            elementos = elementos.plus(elemento)
            cima++
        } else {
            println("La pila esta llena.")
        }
    }

    fun quitar() {
        if (cima > 0) {
            // quita el primer elemento, convierte a un array en caso de ser convertido a lista.
            elementos = elementos.dropLast(1).toTypedArray()
            cima--
        } else {
            println("La pila esta vacia.")
        }
    }
}