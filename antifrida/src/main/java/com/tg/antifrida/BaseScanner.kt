package com.tg.antifrida

abstract class BaseScanner {
    external fun nativeFileExists(path: String):Boolean
    companion object{
        init {
            System.loadLibrary("fileops")
        }
    }

}