package com.example.cardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView

class WinnerActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_winner)

        val winnerName = intent.getStringExtra("playerName")
        val winnerScore = intent.getIntExtra("score", 0)

        val winnerImage = findViewById<ImageView>(R.id.winnerImage)
        val winnerText = findViewById<TextView>(R.id.winnerTextView)

        winnerText.text = "Winner is\n$winnerName!"


    }
}