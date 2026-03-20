package com.example.graficadoraapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.*

class GraficadorView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var puntos: Array<DoubleArray> = arrayOf()
    private val paintEjes = Paint().apply {
        color = Color.parseColor("#9E9E9E")
        strokeWidth = 2f
    }
    private val paintRejilla = Paint().apply {
        color = Color.parseColor("#E0E0E0")
        strokeWidth = 1f
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
    }
    private val paintLinea = Paint().apply {
        color = Color.parseColor("#2196F3")
        strokeWidth = 5f
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
    }
    private val paintTexto = Paint().apply {
        color = Color.DKGRAY
        textSize = 24f
    }

    fun setPuntos(puntos: Array<DoubleArray>) {
        this.puntos = puntos
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (puntos.isEmpty()) return

        val width = width.toFloat()
        val height = height.toFloat()

        // Determinar límites para el Auto-Zoom
        var minX = puntos.minOf { it[0] }
        var maxX = puntos.maxOf { it[0] }
        var minY = puntos.minOf { it[1] }
        var maxY = puntos.maxOf { it[1] }

        // Agregar margen para que no toque los bordes
        val xRange = (maxX - minX).let { if (it == 0.0) 1.0 else it }
        val yRange = (maxY - minY).let { if (it == 0.0) 1.0 else it }
        
        minX -= xRange * 0.1
        maxX += xRange * 0.1
        minY -= yRange * 0.1
        maxY += yRange * 0.1

        // Funciones de mapeo (mundo a pantalla)
        fun mapX(x: Double) = ((x - minX) / (maxX - minX) * width).toFloat()
        fun mapY(y: Double) = (height - (y - minY) / (maxY - minY) * height).toFloat()

        // Dibujar Rejilla y Ejes
        val originX = mapX(0.0)
        val originY = mapY(0.0)

        // Eje X
        if (originY in 0f..height) {
            canvas.drawLine(0f, originY, width, originY, paintEjes)
        }
        // Eje Y
        if (originX in 0f..width) {
            canvas.drawLine(originX, 0f, originX, height, paintEjes)
        }

        // Marcas y números básicos (0, min, max)
        canvas.drawText("0", originX + 5, originY - 5, paintTexto)
        canvas.drawText(String.format("%.1f", minX), 5f, originY - 5, paintTexto)
        canvas.drawText(String.format("%.1f", maxX), width - 60, originY - 5, paintTexto)

        // Dibujar la curva
        for (i in 0 until puntos.size - 1) {
            val startX = mapX(puntos[i][0])
            val startY = mapY(puntos[i][1])
            val stopX = mapX(puntos[i+1][0])
            val stopY = mapY(puntos[i+1][1])

            // Solo dibujar si está dentro de límites razonables para evitar líneas infinitas
            if (startY in -height..height*2 && stopY in -height..height*2) {
                canvas.drawLine(startX, startY, stopX, stopY, paintLinea)
            }
        }
    }
}
