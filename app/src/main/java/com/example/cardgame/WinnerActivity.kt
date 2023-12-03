package com.example.cardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView

class WinnerActivity : AppCompatActivity() {


    private lateinit var animationView: LottieAnimationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_winner)
        animationView = findViewById<LottieAnimationView>(R.id.my_animation_view)
        //Define variables
//        val winnerName = intent.getStringExtra("playerName")
//        val winnerScore = intent.getIntExtra("score", 0)
//
//        val winnerImage = findViewById<ImageView>(R.id.winnerImage)
//        val winnerText = findViewById<TextView>(R.id.winnerTextView)
//
//        winnerText.text = "Winner is\n$winnerName!"

        animationView.playAnimation()


    }
}