package com.raywanderlich.timefighter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.PersistableBundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private lateinit var tapBtn: Button
    private lateinit var textScore: TextView
    private lateinit var timeCount: TextView

    private var score = 0


    private var gameStarted = false

    private lateinit var countDownTimer: CountDownTimer
    private val initialCountDown: Long = 60000
    private val countDownInterval: Long = 1000
    private var timeLeftOnTimer: Long = 60000

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        private const val SCORE_KEY = "SCORE_KEY"
        private const val TIME_LEFT_KEY = "TIME_LEFT_KEY"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "onCreate called. Score is: $score & Time Left: $timeLeftOnTimer ")


        tapBtn = findViewById(R.id.tapBtn)
        textScore = findViewById(R.id.textScore)
        timeCount = findViewById(R.id.timeCount)


        tapBtn.setOnClickListener { view ->
            val bounceAnimation = AnimationUtils.loadAnimation(this, R.anim.bounce)
            view.startAnimation(bounceAnimation)
            incrementScore()
        }

        if (savedInstanceState != null) {
            score = savedInstanceState.getInt(SCORE_KEY)
            timeLeftOnTimer = savedInstanceState.getLong(TIME_LEFT_KEY)
            restoreGame()
        } else {
            resetGame()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.actionAbout) {
            showInfo()
        }
        return true
    }

    private fun showInfo () {
        val dialogTitle = getString(R.string.aboutTitle, BuildConfig.VERSION_NAME)
        val dialogMessage = getString(R.string.aboutMessage)

        val builder = AlertDialog.Builder(this)
        builder.setTitle(dialogTitle)
        builder.setMessage(dialogMessage)
        builder.create().show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putInt(SCORE_KEY, score)
        outState.putLong(TIME_LEFT_KEY, timeLeftOnTimer)
        countDownTimer.cancel()

    Log.d(TAG, "onDestroy called.")
    }

    private fun resetGame () {
        score = 0

        textScore.text = getString(R.string.yourScore, score)

        val initialTimeLeft = initialCountDown / 1000
        timeCount.text = getString(R.string.timeLeft, initialTimeLeft)

        countDownTimer = object : CountDownTimer(initialCountDown, countDownInterval) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftOnTimer = millisUntilFinished
                val timeLeft = millisUntilFinished / 1000
                timeCount.text = getString(R.string.timeLeft, timeLeft)
            }

            override fun onFinish() {
            endGame()
            }
        }
        gameStarted = false
        }

        private fun restoreGame() {
            textScore.text = getString(R.string.yourScore, score)

            val restoredTime = timeLeftOnTimer /1000
            timeCount.text = getString(R.string.timeLeft, restoredTime)

            countDownTimer = object : CountDownTimer(timeLeftOnTimer, countDownInterval) {
                override fun onTick(millisUntilFinished: Long) {
                    timeLeftOnTimer = millisUntilFinished
                    val timeleft = millisUntilFinished / 1000
                    timeCount.text = getString(R.string.timeLeft, timeleft )

                }

                override fun onFinish() {
                    endGame()
                }
            }
            countDownTimer.start()
            gameStarted = true
        }



        private fun incrementScore() {
            if (!gameStarted){
                startGame()
            }
            score += 1
            val newScore = getString(R.string.yourScore, score)
            textScore.text = newScore

            val blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha)
            textScore.startAnimation(blinkAnimation)
        }


        private fun startGame(){
            countDownTimer.start()
            gameStarted = true
        }


        private fun endGame(){
            Toast.makeText(this, getString(R.string.gameOver, score), Toast.LENGTH_LONG).show()
            resetGame()

        }

    }