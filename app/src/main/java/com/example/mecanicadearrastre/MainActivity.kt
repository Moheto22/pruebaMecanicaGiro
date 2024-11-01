package com.example.mecanicadearrastre

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.launch
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    private var job: Job? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        var reloj = findViewById<TextView>(R.id.tiempo)

        var secondsPassed = 0
        setContentView(R.layout.activity_main)
        val siquenceAnimals = generateSequenceAnimals()
        var points = 0
        val marcador = findViewById<TextView>(R.id.marcador)
        val scroller = findViewById<HorizontalScrollView>(R.id.scroll)
        val linear = findViewById<LinearLayout>(R.id.cara)
        val searchImage = findViewById<ImageView>(R.id.searchImage)
        searchImage.setImageResource(siquenceAnimals[0])
        searchImage.tag = siquenceAnimals[0]
        val listaAnimales = generateListaAnimales();
        scroller.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                scroller.scrollTo(4000, 0)
                scroller.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        generatePositions(listaAnimales)
        val commonClickListener = View.OnClickListener { view ->
            val imageView = view as ImageView
            if(imageView.drawable.constantState == searchImage.drawable.constantState){
                siquenceAnimals.removeAt(0)
                points ++
                marcador.setText("${points}/8")
                listaAnimales[imageView.tag as Int][0].visibility = View.GONE
                listaAnimales[imageView.tag as Int][1].visibility = View.GONE
                if (siquenceAnimals.isNotEmpty()){
                    searchImage.setImageResource(siquenceAnimals[0])
                }else{
                    finish()
                }
            }
        }
        applyListener(listaAnimales,commonClickListener)

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
        startTimer(secondsPassed,reloj)
    }

    private fun generateSequenceAnimals(): MutableList<Int> {
        val listMod = listOf(R.drawable.pato,R.drawable.vaca,R.drawable.conejo,R.drawable.caballo,R.drawable.cerdo,R.drawable.gallina,R.drawable.oveja,R.drawable.perro)
        val list : MutableList<Int> = listMod.shuffled().toMutableList()
        return list
    }

    private fun applyListener(listaAnimales: MutableList<List<ImageView>>, commonClickListener: View.OnClickListener) {
        for (i in 0 until 8) {
            listaAnimales[i][0].setOnClickListener(commonClickListener)
            listaAnimales[i][1].setOnClickListener(commonClickListener)
        }
    }
    private fun startTimer(secondsPassed: Int, reloj: TextView) {
        var seconds = secondsPassed // Empieza desde el valor inicial dado o desde 0 si no lo quieres inicializar.

        // Cancela cualquier temporizador anterior para evitar múltiples ejecuciones.
        job?.cancel()

        // Crea un nuevo trabajo para contar el tiempo.
        job = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                delay(1000) // Espera un segundo.
                seconds++
                val timeFormatted = convertSecondsToMinutesAndSeconds(seconds)
                reloj.text = "Tiempo: $timeFormatted"
            }
        }
    }

    private fun generatePositions(listaAnimales: MutableList<List<ImageView>>) {

        var posicionesOcupadas: MutableList<Int> = mutableListOf()
        var positionX : Int
        var positionY : Int
        var listaIndexAnimales = MutableList(8) {it}
        for (i in 0 until 8) {
            positionY = Random.nextInt(600)+450
            val index = listaIndexAnimales[Random.nextInt(listaIndexAnimales.size)]
            listaIndexAnimales.remove(index)
            do {
                positionX = Random.nextInt(10300)+500
            }while (checkPosition(posicionesOcupadas,positionX))

            (listaAnimales[index][0].layoutParams as FrameLayout.LayoutParams).leftMargin = positionX
            (listaAnimales[index][0].layoutParams as FrameLayout.LayoutParams).topMargin = positionY
            (listaAnimales[index][1].layoutParams as FrameLayout.LayoutParams).leftMargin = positionX+11630
            (listaAnimales[index][1].layoutParams as FrameLayout.LayoutParams).topMargin = positionY
        }
    }

    private fun checkPosition(posicionesOcupadas: MutableList<Int>, position: Int): Boolean {
        for (pos in posicionesOcupadas) {
            if (pos + 3000 > position && pos - 3000 < position) {
                // Si encontramos una posición conflictiva, retorna false directamente.
                return false
            }
        }
        // Si no se encontró una posición conflictiva, agrega la posición y retorna true.
        posicionesOcupadas.add(position)
        return true
    }

    private fun generateListaAnimales(): MutableList<List<ImageView>> {
        var list =  listOf(
            listOf(findViewById(R.id.imageVaca1), findViewById(R.id.imageVaca2)),
            listOf(findViewById(R.id.imagePerro1), findViewById(R.id.imagePerro2)),
            listOf(findViewById(R.id.imageGallina1), findViewById(R.id.imageGallina2)),
            listOf(findViewById(R.id.imageCaballo1),findViewById(R.id.imageCaballo2)),
            listOf(findViewById(R.id.imageCerdo1),findViewById(R.id.imageCerdo2)),
            listOf(findViewById(R.id.imageConejo1),findViewById(R.id.imageConejo2)),
            listOf(findViewById(R.id.imagePato1),findViewById(R.id.imagePato2)),
            listOf(findViewById(R.id.imageOveja1),findViewById<ImageView>(R.id.imageOveja2))
        ).toMutableList()
        for (i in 0 until 8) {
            list[i][0].tag=i
            list[i][1].tag=i
        }
        return list
    }
    fun convertSecondsToMinutesAndSeconds(seconds: Int): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return "$minutes:${String.format("%02d", remainingSeconds)}"
    }

}
