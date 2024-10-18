package com.example.mecanicadearrastre

import android.os.Bundle
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
        val c = linear.width
        val img1 = findViewById<ImageView>(R.id.imageViewSmall)
        val img2 = findViewById<ImageView>(R.id.imageViewSmall2)
        val imageView = findViewById<ImageView>(R.id.imageViewSmall)

        var X : Int = Random.nextInt(7800)+300
        var Y : Int = Random.nextInt(400)+200
        val layoutParams = img1.layoutParams as FrameLayout.LayoutParams

        (img1.layoutParams as FrameLayout.LayoutParams).leftMargin = X
        (img1.layoutParams as FrameLayout.LayoutParams).topMargin = Y

        (img2.layoutParams as FrameLayout.LayoutParams).leftMargin = X+8011
        (img2.layoutParams as FrameLayout.LayoutParams).topMargin = Y



        scroller.viewTreeObserver.addOnScrollChangedListener{
            val X = scroller.scrollX
            val maxLength = linear.width - 3000
            lifecycleScope.launch {
                if (X >= maxLength) {
                    val firstImageView = linear.getChildAt(0)
                    linear.removeViewAt(0)
                    linear.addView(firstImageView)
                    scroller.scrollTo(X - firstImageView.width, 0)
                    (img2.layoutParams as FrameLayout.LayoutParams).leftMargin = X+8011
                }else if (X <= 3000) {
                    val lastImageView = linear.getChildAt(1)
                    linear.removeViewAt(1)
                    linear.addView(lastImageView)
                    scroller.scrollTo(X + lastImageView.width, 0)
                }
            }


        }
    }
}