package com.tg.antifrida

interface FridaScanner {
    fun scanFrida(onComplete:(isFound:Boolean)->Unit)
}
