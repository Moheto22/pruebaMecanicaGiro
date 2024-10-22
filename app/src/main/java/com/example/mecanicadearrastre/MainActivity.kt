package com.example.mecanicadearrastre

import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val scroller = findViewById<HorizontalScrollView>(R.id.scroll)
        val linear = findViewById<LinearLayout>(R.id.cara)
        val img1 = findViewById<ImageView>(R.id.imageViewSmall)
        val img2 = findViewById<ImageView>(R.id.imageViewSmall2)
        val fondo = findViewById<ImageView>(R.id.imageView1)


        scroller.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                scroller.scrollTo(4000, 0)
                scroller.viewTreeObserver.removeOnGlobalLayoutListener(this)
                val totalWidth = linear.width
                println("El ancho total del LinearLayout es: $totalWidth")
            }
        })

        // Configuración de las imágenes
        var X: Int = Random.nextInt(7800) -3700
        var Y: Int = Random.nextInt(400) + 200
        (img1.layoutParams as FrameLayout.LayoutParams).leftMargin = X
        (img1.layoutParams as FrameLayout.LayoutParams).topMargin = Y
        (img2.layoutParams as FrameLayout.LayoutParams).leftMargin = X+fondo.width
        (img2.layoutParams as FrameLayout.LayoutParams).topMargin = Y

        // Listener para detectar el desplazamiento
        scroller.viewTreeObserver.addOnScrollChangedListener {
            val X = scroller.scrollX
            val maxLength = linear.width - 3000
            lifecycleScope.launch {
                if (X >= maxLength) {
                    val firstImageView = linear.getChildAt(0)
                    linear.removeViewAt(0)
                    linear.addView(firstImageView)
                    scroller.scrollTo(X - firstImageView.width, 0)
                    (img1.layoutParams as FrameLayout.LayoutParams).leftMargin += fondo.width
                    (img2.layoutParams as FrameLayout.LayoutParams).leftMargin += fondo.width
                } else if (X <= 3000) {
                    val lastImageView = linear.getChildAt(1)
                    linear.removeViewAt(1)
                    linear.addView(lastImageView)
                    scroller.scrollTo(X + lastImageView.width, 0)
                    (img1.layoutParams as FrameLayout.LayoutParams).leftMargin -= fondo.width
                    (img2.layoutParams as FrameLayout.LayoutParams).leftMargin -= fondo.width
                }
            }
        }
    }
}
