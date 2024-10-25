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
        val gallinaImg1 = findViewById<ImageView>(R.id.imageGallina1)
        val gallinaImg2 = findViewById<ImageView>(R.id.imageGallina2)
        val caballoImg1 = findViewById<ImageView>(R.id.imageCaballo1)
        val caballoImg2 = findViewById<ImageView>(R.id.imageCaballo2)
        val cerdoImg1 = findViewById<ImageView>(R.id.imageCerdo1)
        val cerdoImg2 = findViewById<ImageView>(R.id.imageCerdo2)
        val conejoImg1 = findViewById<ImageView>(R.id.imageConejo1)
        val conejoImg2 = findViewById<ImageView>(R.id.imageConejo2)
        val patoImg1 = findViewById<ImageView>(R.id.imagePato1)
        val patoImg2 = findViewById<ImageView>(R.id.imagePato2)
        val ovejaImg1 = findViewById<ImageView>(R.id.imageOveja1)
        val ovejaImg2 = findViewById<ImageView>(R.id.imageOveja2)
        val imgSearch = findViewById<ImageView>(R.id.searchImage)
        val listaAnimales = generateListaAnimales();
        scroller.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                scroller.scrollTo(4000, 0)
                scroller.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        generatePositions(perroImg1,perroImg2,vacaImg2,vacaImg1,gallinaImg1,gallinaImg2,caballoImg1,caballoImg2,cerdoImg1,
            cerdoImg2,conejoImg1,conejoImg2,patoImg1,patoImg2,ovejaImg1,ovejaImg2);
        generatePositions(listaAnimales)


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

    private fun generatePositions(listaAnimales: Any) {

        var posicionesOcupadas = mutableListOf(Int)
        var position = 0

        var listaIndexAnimales = MutableList(8) {it}
        for (i in 0 until 8) {
            val index = listaIndexAnimales[Random.nextInt(listaIndexAnimales.size)]
            listaIndexAnimales.remove(index)
            do {
                position = Random.nextInt(10300)+500
            }while (checkPosition(posicionesOcupadas,position))

        }
    }

    private fun checkPosition(posicionesOcupadas: MutableList<Int>, position: Int): Boolean {
        var valide = true
        var index = 0
        do {
            if (posicionesOcupadas[index] + 300> position && posicionesOcupadas[index]-300< position ){
                valide = false
            }
        } while (valide)
        if (valide){
            posicionesOcupadas.add(position)
        }
        return valide
    }

    private fun generateListaAnimales(): Any {
        return listOf(
            listOf(findViewById<ImageView>(R.id.imageVaca1), findViewById<ImageView>(R.id.imageVaca2)),
            listOf(findViewById<ImageView>(R.id.imagePerro1), findViewById<ImageView>(R.id.imagePerro2)),
            listOf(findViewById<ImageView>(R.id.imageGallina1), findViewById<ImageView>(R.id.imageGallina2)),
            listOf(findViewById<ImageView>(R.id.imageCaballo1),findViewById<ImageView>(R.id.imageCaballo2)),
            listOf(findViewById<ImageView>(R.id.imageCerdo1),findViewById<ImageView>(R.id.imageCerdo2)),
            listOf(findViewById<ImageView>(R.id.imageConejo1),findViewById<ImageView>(R.id.imageConejo2)),
            listOf(findViewById<ImageView>(R.id.imagePato1),findViewById<ImageView>(R.id.imagePato2)),
            listOf(findViewById<ImageView>(R.id.imageOveja1),findViewById<ImageView>(R.id.imageOveja2))
        )
    }

    private fun generatePositions(
        perroImg1: ImageView,
        perroImg2: ImageView,
        vacaImg2: ImageView,
        vacaImg1: ImageView,
        gallinaImg1: ImageView,
        gallinaImg2: ImageView,
        caballoImg1: ImageView,
        caballoImg2: ImageView,
        cerdoImg1: ImageView,
        cerdoImg2: ImageView,
        conejoImg1: ImageView,
        conejoImg2: ImageView,
        patoImg1: ImageView,
        patoImg2: ImageView,
        ovejaImg1: ImageView,
        ovejaImg2: ImageView
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

        (gallinaImg1.layoutParams as FrameLayout.LayoutParams).leftMargin = 6000
        (gallinaImg1.layoutParams as FrameLayout.LayoutParams).topMargin = Y
        (gallinaImg2.layoutParams as FrameLayout.LayoutParams).leftMargin = 6000 + 11630
        (gallinaImg2.layoutParams as FrameLayout.LayoutParams).topMargin = Y

        (caballoImg1.layoutParams as FrameLayout.LayoutParams).leftMargin = 5000
        (caballoImg1.layoutParams as FrameLayout.LayoutParams).topMargin = 700
        (caballoImg2.layoutParams as FrameLayout.LayoutParams).leftMargin = 5000 + 11630
        (caballoImg2.layoutParams as FrameLayout.LayoutParams).topMargin = 700

        (cerdoImg1.layoutParams as FrameLayout.LayoutParams).leftMargin = 7000
        (cerdoImg1.layoutParams as FrameLayout.LayoutParams).topMargin = 700
        (cerdoImg2.layoutParams as FrameLayout.LayoutParams).leftMargin = 7000 + 11630
        (cerdoImg2.layoutParams as FrameLayout.LayoutParams).topMargin = 700

        (conejoImg1.layoutParams as FrameLayout.LayoutParams).leftMargin = 8000
        (conejoImg1.layoutParams as FrameLayout.LayoutParams).topMargin = 700
        (conejoImg2.layoutParams as FrameLayout.LayoutParams).leftMargin = 8000 + 11630
        (conejoImg2.layoutParams as FrameLayout.LayoutParams).topMargin = 700

        (patoImg1.layoutParams as FrameLayout.LayoutParams).leftMargin = 9000
        (patoImg1.layoutParams as FrameLayout.LayoutParams).topMargin = 900
        (patoImg2.layoutParams as FrameLayout.LayoutParams).leftMargin = 9000 + 11630
        (patoImg2.layoutParams as FrameLayout.LayoutParams).topMargin = 900

        (ovejaImg1.layoutParams as FrameLayout.LayoutParams).leftMargin = 3000
        (ovejaImg1.layoutParams as FrameLayout.LayoutParams).topMargin = 600
        (ovejaImg2.layoutParams as FrameLayout.LayoutParams).leftMargin = 3000 + 11630
        (ovejaImg2.layoutParams as FrameLayout.LayoutParams).topMargin = 600
    }

}
