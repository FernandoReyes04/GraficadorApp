package com.example.graficadorapp

class Pila(val tam:Int, var cima:Int, var elementos:Array<Char>) {
    fun crearPila(elementos:Array<Char>) {} // TODO: metodo crear pila

    fun insertar(elemento: Char) {
        if (elementos.size < tam) {
            // entonces, añadir elemento.
            cima++ // se incrementa el "indice"
            elementos.plus(elemento) // se anade.
        } else {
            println("No se pudo añadir el elemento :(")
        }
    }

    fun quitar() {} // TODO: metodo quitar elemento.
}