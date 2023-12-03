package com.example.cardgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class WinnerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_winner)

        var winnerName = intent.getStringExtra("winnerName") ?: "Unknown Player"
        var playerName = intent.getStringExtra("playerName") ?: "Unknown Player"
        val winnerScore = intent.getIntExtra("score", 0)

        val winnerImage = findViewById<ImageView>(R.id.winnerImage)
        val winnerText = findViewById<TextView>(R.id.winnerTextView)
        val playAgainButton = findViewById<Button>(R.id.playAgainButton)

        winnerText.text = "Winner is\n$winnerName!"

        playAgainButton.setOnClickListener {

            //If user chose to play again, send back to mainActivity, flag intent to clear memory
            val restartIntent = Intent(this, MainActivity::class.java)
            restartIntent.putExtra("playerName", playerName)
            restartIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(restartIntent)
        }
    }
}