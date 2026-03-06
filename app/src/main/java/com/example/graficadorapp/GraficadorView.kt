package com.example.graficadoraapp

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

        // Dibujar ejes
        paint.color = Color.LTGRAY
        canvas.drawLine(0f, centerY, width.toFloat(), centerY, paint)
        canvas.drawLine(centerX, 0f, centerX, height.toFloat(), paint)

        val scaleFactor = 20f // Aumentado para mejor visibilidad
        paint.color = Color.BLUE
        paint.strokeWidth = 5f

        if (puntos.isNotEmpty()) {
            for (i in 0 until puntos.size - 1) {
                val x1 = puntos[i][0]
                val y1 = puntos[i][1]
                val x2 = puntos[i + 1][0]
                val y2 = puntos[i + 1][1]

                val startX = centerX + (x1 * scaleFactor)
                val startY = centerY - (y1 * scaleFactor)
                val stopX = centerX + (x2 * scaleFactor)
                val stopY = centerY - (y2 * scaleFactor)

                // Dibujar línea entre puntos para una gráfica continua
                canvas.drawLine(startX.toFloat(), startY.toFloat(), stopX.toFloat(), stopY.toFloat(), paint)
            }
        }
    }
}
