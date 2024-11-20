package com.example.mecanicadearrastre

class Player(val name: String, val tries: MutableList<Try> = mutableListOf()) {

    fun addTry(newTry: Try) {
        this.tries.add(newTry)
    }
}