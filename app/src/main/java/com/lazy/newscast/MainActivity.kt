package com.lazy.newscast

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Choreographer
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.lazy.newscast.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlin.system.measureTimeMillis

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)
        val bottomNavigationMenu = binding.bottomMenu
        val navHost =
            supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHost.findNavController()

        bottomNavigationMenu.setupWithNavController(navController)

        setupActionBarWithNavController(navController)

        //startFPSCount()
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    private fun startFPSCount() {

        var frameCount = 0
        var lastFrameTime = System.nanoTime()
        var minFps = Float.MAX_VALUE
        var totalFps = 0f

        // Создаем список для хранения всех замеров FPS
        val fpsList = mutableListOf<Float>()

        val frameCallback = object : Choreographer.FrameCallback {
            override fun doFrame(frameTimeNanos: Long) {
                val currentTime = System.nanoTime()
                val elapsedNanos = currentTime - lastFrameTime
                val elapsedMs =
                    elapsedNanos / 1_000_000f // Добавляем f, чтобы результат был типа Float
                frameCount++

                if (frameCount > 0 && elapsedMs > 1_000) { // Исправляем условие: если прошло ровно 1000 мс, то должно замеряться
                    val fps = frameCount * 1_000f / elapsedMs
                    totalFps += fps
                    minFps = minOf(minFps, fps)

                    fpsList.add(fps)
                    val averageFps = if (fpsList.isNotEmpty()) {
                        (fpsList.sum()) / (fpsList.size)
                    } else {
                        fps
                    }

                    // Выводим результаты в лог
                    Log.i("FPS", "FPS: $fps, Min FPS: $minFps, Avg FPS: $averageFps")

                    // Сбрасываем значения для следующего замера
                    frameCount = 0
                    lastFrameTime = currentTime
                }

                // Запускаем следующий замер FPS
                Choreographer.getInstance().postFrameCallback(this)
            }
        }

        Choreographer.getInstance().postFrameCallback(frameCallback)
    }
}