package com.example.customwidgets

import android.content.res.Configuration
import android.graphics.Color
import android.media.MediaPlayer
import android.os.*
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var customBar: CustomBar
    private lateinit var customSwitch: CustomSwitch
    private lateinit var circularProgressBar: CircularProgressBar
    private lateinit var resetButton: Button
    private lateinit var stopButton: Button
    private lateinit var seekBar: SeekBar
    private lateinit var themeToggleButton: Button
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var progressSound: MediaPlayer
    private lateinit var vibrator: Vibrator
    private var isAnimating = false

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize widgets
        customBar = findViewById(R.id.custom_bar)
        customSwitch = findViewById(R.id.custom_switch)
        circularProgressBar = findViewById(R.id.circular_progress_bar)
        resetButton = findViewById(R.id.reset_button)
        stopButton = findViewById(R.id.stop_button)
        seekBar = findViewById(R.id.seek_bar)
        themeToggleButton = findViewById(R.id.theme_toggle_button)

        // Initialize media player and vibrator
        mediaPlayer = MediaPlayer.create(this, R.raw.button_click)
        progressSound = MediaPlayer.create(this, R.raw.progress_sound)
        vibrator = getSystemService(Vibrator::class.java)

        // Setup interactions
        setupCustomSwitch()
        setupCustomBar()
        setupCircularProgressBar()
        setupSeekBar()
        setupResetButton()
        setupStopButton()
        setupThemeToggle()
        autoDarkMode()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupCustomSwitch() {
        customSwitch.setOnClickListener {
            val isChecked = customSwitch.isSwitchOn()

            playClickSound()
            vibrateDevice()

            if (isChecked) {
                customBar.visibility = View.VISIBLE
                circularProgressBar.visibility = View.VISIBLE
            } else {
                customBar.visibility = View.INVISIBLE
                circularProgressBar.visibility = View.INVISIBLE
            }
        }
    }

    private fun setupCustomBar() {
        customBar.setOnClickListener {
            val newProgress = (10..100).random()

            animateCustomBarProgress(newProgress)
            circularProgressBar.setProgressWithAnimation(newProgress)
            seekBar.progress = newProgress
            playProgressSound()
        }
        customBar.setGradientColors(Color.RED, Color.YELLOW)
    }

    private fun setupCircularProgressBar() {
        circularProgressBar.setProgressWithAnimation(50) // Default progress
        circularProgressBar.setOnClickListener {
            val newProgress = (10..100).random()
            circularProgressBar.setProgressWithAnimation(newProgress)
            animateCustomBarProgress(newProgress)
            seekBar.progress = newProgress
            playProgressSound()
        }
        circularProgressBar.setGradientColors(Color.GREEN, Color.MAGENTA)
    }

    private fun setupSeekBar() {
        seekBar.max = 100
        seekBar.progress = customBar.getProgress()

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    customBar.run { setProgress(progress) }
                    circularProgressBar.setProgressWithAnimation(progress)
                }
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                playProgressSound()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setupResetButton() {
        resetButton.setOnClickListener {
            playClickSound()
            vibrateDevice()
            customBar.setProgress(0)
            circularProgressBar.setProgressWithAnimation(0)
            seekBar.progress = 0
        }
    }

    private fun setupStopButton() {
        stopButton.setOnClickListener {
            playClickSound()
            isAnimating = false
        }
    }

    private fun setupThemeToggle() {
        themeToggleButton.setOnClickListener {
            val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
        }
    }

    private fun autoDarkMode() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        if (hour in 18..6) { // Activate dark mode in the evening and night
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun animateCustomBarProgress(targetProgress: Int) {
        isAnimating = true
        val currentProgress = customBar.getProgress()
        if (currentProgress == targetProgress) return

        val step = if (targetProgress > currentProgress) 1 else -1

        Thread {
            var i = currentProgress
            while (isAnimating && ((step > 0 && i <= targetProgress) || (step < 0 && i >= targetProgress))) {
                runOnUiThread {
                    customBar.setProgress(i)
                }
                i += step
                Thread.sleep(10)
            }
        }.start()
    }

    private fun playClickSound() {
        mediaPlayer.start()
    }

    private fun playProgressSound() {
        progressSound.start()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun vibrateDevice() {
        if (vibrator.hasVibrator()) {
            vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.release()
        progressSound.release()
    }
}