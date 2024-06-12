package com.tg.antifrida

import android.os.Bundle
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.tg.antifrida.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val frida = FridaScannerBasic()
        frida.scanFrida {
            CoroutineScope(Dispatchers.Main).launch {
                if (it) {
                    Toast.makeText(this@MainActivity, "Found", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this@MainActivity, "Not Found", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}