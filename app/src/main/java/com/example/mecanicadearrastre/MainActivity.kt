package com.example.mecanicadearrastre

import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
        val vacaImg1 = findViewById<ImageView>(R.id.imageVaca1)
        val vacaImg2 = findViewById<ImageView>(R.id.imageVaca2)
        val perroImg1 = findViewById<ImageView>(R.id.imagePerro1)
        val perroImg2 = findViewById<ImageView>(R.id.imagePerro2)
        val imgSearch = findViewById<ImageView>(R.id.searchImage)

        scroller.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                scroller.scrollTo(4000, 0)
                scroller.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        generatePositions(perroImg1,perroImg2,vacaImg2,vacaImg1);
        // Configuración de las imágenes
        var X: Int = Random.nextInt(7800) +500
        var Y: Int = Random.nextInt(400) + 500
        (vacaImg1.layoutParams as FrameLayout.LayoutParams).leftMargin = X
        (vacaImg1.layoutParams as FrameLayout.LayoutParams).topMargin = Y
        (perroImg1.layoutParams as FrameLayout.LayoutParams).leftMargin = X+11630
        (perroImg1.layoutParams as FrameLayout.LayoutParams).topMargin = Y

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
                } else if (X <= 3000) {
                    val lastImageView = linear.getChildAt(1)
                    linear.removeViewAt(1)
                    linear.addView(lastImageView)
                    scroller.scrollTo(X + lastImageView.width, 0)
                }
            }
        }
    }

    private fun generatePositions(
        perroImg1: ImageView,
        perroImg2: ImageView,
        vacaImg2: ImageView,
        vacaImg1: ImageView
    ) {
        var X: Int = Random.nextInt(7800) +500
        var Y: Int = Random.nextInt(400) + 500
        (vacaImg1.layoutParams as FrameLayout.LayoutParams).leftMargin = X
        (vacaImg1.layoutParams as FrameLayout.LayoutParams).topMargin = Y
        (vacaImg2.layoutParams as FrameLayout.LayoutParams).leftMargin = X+11630
        (vacaImg2.layoutParams as FrameLayout.LayoutParams).topMargin = Y

        (perroImg1.layoutParams as FrameLayout.LayoutParams).leftMargin = 2000
        (perroImg1.layoutParams as FrameLayout.LayoutParams).topMargin = Y
        (perroImg2.layoutParams as FrameLayout.LayoutParams).leftMargin = 2000 + 11630
        (perroImg2.layoutParams as FrameLayout.LayoutParams).topMargin = Y

    }
}
