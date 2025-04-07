package com.example.graficadorapp

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

// view es separar lo visual.
class GraficadorView(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var puntos: Array<DoubleArray> = arrayOf()  // arreglo de puntos a graficar

    fun setPuntos(puntos: Array<DoubleArray>) {
        this.puntos = puntos
        invalidate()  // redibujo en view
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = Paint()
        paint.color = Color.BLACK
        paint.strokeWidth = 3f
        paint.textSize = 20f

        val centerX = width / 2f
        val centerY = height / 2f

        canvas.drawLine(0f, centerY, width.toFloat(), centerY, paint)
        canvas.drawLine(centerX, 0f, centerX, height.toFloat(), paint)

        val scaleFactor = 10f

        for (punto in puntos) {
            val x = punto[0]
            val y = punto[1]

            val canvasX = centerX + (x * scaleFactor)
            val canvasY = centerY - (y * scaleFactor) // y aumenta hacia abajo en pantalla

            // Dibujar el punto
            canvas.drawCircle(canvasX.toFloat(), canvasY.toFloat(), 5f, paint)
        }
    }
}
