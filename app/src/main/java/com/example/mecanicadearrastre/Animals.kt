package com.example.mecanicadearrastre


import android.widget.ImageView

class Animals(val name: String, val imageResId: Int, val isSilueta: Boolean?, val imageView: ImageView?, var isCorrect: Boolean?,sound : Int) {
    constructor(name: String, imageResId: Int, sound: Int) : this(
        name, imageResId, null, null, null, sound
    )
}