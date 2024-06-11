package com.tg.antifrida

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.RandomAccessFile
import java.net.Socket
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

class FridaScannerBasic : BaseScanner(), FridaScanner {
    override fun scanFrida(onComplete: (isFound: Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            if (checkFridaFiles() || checkFridaClasses() || scanTextSectionsForStrings() || scanLocalTcpPorts()) {
                onComplete(true)
            } else {
                onComplete(false)
            }
        }
    }

    private fun checkFridaFiles(): Boolean {
        val paths = arrayOf(
            "/data/local/tmp/frida-server",
            "/data/local/tmp/re.frida.server",
            "/data/local/tmp/fs-data",
            "/data/local/tmp/fs-mnt"
        )
        for (path in paths) {
            if (nativeFileExists(path)) {
                return true
            }
        }
        return false
    }

    private fun checkFridaClasses(): Boolean {
        return try {
            Class.forName("re.frida.Gadget")
            true
        } catch (e: ClassNotFoundException) {
            false
        }
    }


    private suspend fun scanLocalTcpPorts(): Boolean = runBlocking {
        val portRange = 1..65535

        // Launching coroutines to scan ports in parallel
        val results = portRange.map { port ->
            async(Dispatchers.IO) {
                scanPort(port)
            }
        }.map { it.await() }

        // Check if any port returned true
        results.any { it }
    }

    private suspend fun scanPort(port: Int): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                Socket("127.0.0.1", port).use { socket ->
                    socket.getOutputStream().write("D-Bus".toByteArray(StandardCharsets.UTF_8))
                    socket.getOutputStream().flush()
                    BufferedReader(InputStreamReader(socket.getInputStream())).use { reader ->
                        var line: String?
                        while (reader.readLine().also { line = it } != null) {
                            if (line!!.contains("frida")) {
                                return@withContext true
                            }
                        }
                    }
                }
            } catch (ignored: Exception) {
            }
            false
        }
    }

    private fun scanTextSectionsForStrings(): Boolean {
        return try {
            RandomAccessFile("/proc/self/maps", "r").use { maps ->
                var line: String?
                val pattern = Pattern.compile("frida-gadget|frida-agent")
                while (maps.readLine().also { line = it } != null) {
                    if (line!!.contains(".so") && pattern.matcher(line!!).find()) {
                        return true
                    }
                }
            }

            false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

}