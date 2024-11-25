package com.example.mecanicadearrastre

import android.content.res.Resources
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.TypedValue
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class EasyGame : AppCompatActivity() {
    object constantsDataUser{
        const val INDEX = "INDEX"
        const val LISTPLAYERS = "LISTPLAYERS"
    }

    private var  seconds = 0
    private var timeRunnable: Runnable? = null
    private var handler = Handler(Looper.getMainLooper())
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val date = LocalDateTime.now()
        enableEdgeToEdge()
        val mediaBandasonora = MediaPlayer.create(this@EasyGame, R.raw.bandasonora)
        mediaBandasonora.isLooping = true  // Reproducir música en bucle
        mediaBandasonora.start()
        mediaBandasonora.setVolume(
            0.3f,
            0.3f
        )
        var errors = 0
        var handler = Handler(Looper.getMainLooper())
        setContentView(R.layout.activity_easy_game)
        val siquenceAnimals = generateSequenceAnimals()
        var startTime = System.currentTimeMillis()
        var points = 0
        val marcador = findViewById<TextView>(R.id.marcador)
        val scroller = findViewById<HorizontalScrollView>(R.id.scroll)
        val linear = findViewById<LinearLayout>(R.id.cara)
        val searchImage = findViewById<ImageView>(R.id.searchImage)
        searchImage.setImageResource(siquenceAnimals[0].imageResId)
        searchImage.tag = siquenceAnimals[0].imageResId
        val listaImageView = generateListImageView(siquenceAnimals)
        scroller.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                scroller.scrollTo(4000, 0)
                scroller.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })
        generatePositions(listaImageView)
        val commonClickListener = View.OnClickListener { view ->
            val imageView = view as ImageView
            if(imageView.drawable.constantState == searchImage.drawable.constantState){
                val mediaAnimal = siquenceAnimals[0].sound.let { MediaPlayer.create(this, it) }
                mediaAnimal.setVolume(0.3f,0.3f)
                mediaAnimal.start()
                siquenceAnimals.removeAt(0)
                points ++
                marcador.setText("${points}/8")
                listaImageView[imageView.tag as Int][0].visibility = View.GONE
                listaImageView[imageView.tag as Int][1].visibility = View.GONE
                if (siquenceAnimals.isNotEmpty()){
                    searchImage.setImageResource(siquenceAnimals[0].imageResId)
                }else{
                    stopTimer()
                    val timeString = findViewById<TextView>(R.id.tiempo)
                    val seconds = stringToSeconds(timeString.text.toString())
                    val background = findViewById<View>(R.id.finishBackground)
                    val frame = findViewById<ImageView>(R.id.finalFrame)
                    val image = findViewById<ImageView>(R.id.imageCongratulation)
                    val timeRecord = findViewById<TextView>(R.id.time_watch_final)
                    timeRecord.text = timeString.text.toString()
                    val watchImg = findViewById<ImageView>(R.id.watch_final)
                    val errorsRecord = findViewById<TextView>(R.id.num_error_final)
                    errorsRecord.text = errors.toString()
                    val errorImg = findViewById<ImageView>(R.id.error_final)
                    val buttonReplay = findViewById<Button>(R.id.button_repetir_final)
                    val buttonSalir = findViewById<Button>(R.id.button_salir_final)
                    background.visibility = View.VISIBLE
                    frame.visibility = View.VISIBLE
                    image.visibility = View.VISIBLE
                    timeRecord.visibility = View.VISIBLE
                    watchImg.visibility = View.VISIBLE
                    errorsRecord.visibility = View.VISIBLE
                    errorImg.visibility = View.VISIBLE
                    buttonReplay.visibility = View.VISIBLE
                    buttonSalir.visibility = View.VISIBLE

                }
            }else{
                errors ++
                var mediaError = MediaPlayer.create(this,R.raw.error)
                mediaError.setVolume(0.2f,0.2f)
                mediaError.start()
            }
            }
        applyListener(listaImageView,commonClickListener)

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
        startTimer(startTime,handler)
    }

    private fun stringToSeconds(timeString: String): Int {
        val minuts = timeString.substring(0,2).toInt()
        val seconds = timeString.substring(timeString.length-2).toInt()
        return (minuts * 60)+seconds
    }

    private fun generateSequenceAnimals(): MutableList<AnimalsEasy> {
        return listOf(AnimalsEasy(R.drawable.vaca,R.raw.vaca_sonido,200,150),
            AnimalsEasy(R.drawable.pato,R.raw.pato_sonido,100,100),
            AnimalsEasy(R.drawable.perro,R.raw.perro_sonido,150,100),
            AnimalsEasy(R.drawable.caballo,R.raw.caballo_sonido,200,300),
            AnimalsEasy(R.drawable.oveja,R.raw.oveja,150,150),
            AnimalsEasy(R.drawable.cerdo,R.raw.cerdo_sonido,200,150),
            AnimalsEasy(R.drawable.conejo,R.raw.conejo,100,100),
            AnimalsEasy(R.drawable.gallina,R.raw.gallina_sonido,100,100)).shuffled().toMutableList()

    }

    private fun applyListener(listaAnimales: MutableList<List<ImageView>>, commonClickListener: View.OnClickListener) {
        for (i in 0 until 8) {
            listaAnimales[i][0].setOnClickListener(commonClickListener)
            listaAnimales[i][1].setOnClickListener(commonClickListener)
        }
    }
    private fun startTimer(startTime: Long, handler: Handler) {
        // Runnable que se ejecuta cada segundo
        timeRunnable = object : Runnable {
            override fun run() {
                // Obtener el tiempo transcurrido
                val elapsedMillis = System.currentTimeMillis() - startTime
                val seconds = (elapsedMillis / 1000) % 60
                val minutes = (elapsedMillis / 1000) / 60

                // Formatear el tiempo en "MM:SS"
                val timeString = String.format("%02d:%02d", minutes, seconds)

                // Actualizar el TextView con el tiempo
                val timerTextView = findViewById<TextView>(R.id.tiempo)
                timerTextView.text = timeString
                // Reprogramar el siguiente "tick"
                handler.postDelayed(this, 1000)
            }
        }

        // Iniciar el timer
        handler.post(timeRunnable!!)
    }

    private fun generatePositions(listaAnimales: MutableList<List<ImageView>>) {
        var posicionesOcupadas: MutableList<Int> = mutableListOf()
        var positionX : Int
        var positionY : Int
        var listaIndexAnimales = MutableList(8) {it}
        for (i in 0 until 8) {
            positionY = Random.nextInt(500)+450
            val index = listaIndexAnimales[Random.nextInt(listaIndexAnimales.size)]
            listaIndexAnimales.remove(index)
            do {
                positionX = Random.nextInt(19500)+1000
            }while (checkPosition(posicionesOcupadas,positionX))

            (listaAnimales[index][0].layoutParams as FrameLayout.LayoutParams).leftMargin = positionX
            (listaAnimales[index][0].layoutParams as FrameLayout.LayoutParams).topMargin = positionY
            (listaAnimales[index][1].layoutParams as FrameLayout.LayoutParams).leftMargin = positionX+21750
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

    private fun generateListImageView(siquenceAnimals: List<AnimalsEasy>): MutableList<List<ImageView>> {
        var list =  listOf(
            listOf(findViewById(R.id.image1_1), findViewById(R.id.image1_2)),
            listOf(findViewById(R.id.image2_1), findViewById(R.id.image2_2)),
            listOf(findViewById(R.id.image3_1), findViewById(R.id.image3_2)),
            listOf(findViewById(R.id.image4_1),findViewById(R.id.image4_2)),
            listOf(findViewById(R.id.image5_1),findViewById(R.id.image5_2)),
            listOf(findViewById(R.id.image6_1),findViewById(R.id.image6_2)),
            listOf(findViewById(R.id.image7_1),findViewById(R.id.image7_2)),
            listOf(findViewById(R.id.image8_1),findViewById<ImageView>(R.id.image8_2))
        ).toMutableList()
        for (i in 0 until 8) {
            list[i][0].setImageResource(siquenceAnimals[i].imageResId)
            list[i][0].setSizeInDp(siquenceAnimals[i].width,siquenceAnimals[i].height)
            list[i][0].tag = i
            list[i][1].setImageResource(siquenceAnimals[i].imageResId)
            list[i][1].setSizeInDp(siquenceAnimals[i].width,siquenceAnimals[i].height)
            list[i][1].tag = i
        }
        return list
    }
    fun ImageView.setSizeInDp(widthDp: Int, heightDp: Int) {
        // Convertir dp a píxeles
        val widthPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, widthDp.toFloat(), Resources.getSystem().displayMetrics
        ).toInt()

        val heightPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, heightDp.toFloat(), Resources.getSystem().displayMetrics
        ).toInt()

        // Cambiar las dimensiones del ImageView
        val params = this.layoutParams
        params.width = widthPx
        params.height = heightPx
        this.layoutParams = params
    }
    private fun stopTimer() {
        timeRunnable?.let { handler.removeCallbacks(it) }
    }



}
