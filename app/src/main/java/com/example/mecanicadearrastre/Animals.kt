package com.example.mecanicadearrastre


import android.widget.ImageView

class Animals(val name: String, val imageResId: Int, val isSilueta: Boolean?, val imageView: ImageView?, var isCorrect: Boolean?,val sound : Int, val height: Int?, val width: Int?) {
    constructor(name: String, imageResId: Int, sound: Int,width: Int,height: Int) : this(
        name, imageResId, null, null, null, sound,width,height
    )
}