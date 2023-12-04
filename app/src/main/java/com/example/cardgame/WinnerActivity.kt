package com.example.cardgame

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class WinnerActivity : AppCompatActivity() {

//    var highscoreList = mutableListOf<GameResult>()

    private fun getHighScoreList(): MutableList<GameResult> {

        // Hämta referensen till SharedPreferences
        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

        // Hämta JSON-strängen från SharedPreferences
        val winnersJson = sharedPreferences.getString("highscoreList", "")

        // Konvertera JSON-strängen till en lista av GameResult-objekt
        val type = object : TypeToken<List<GameResult>>() {}.type
        val winnersList: MutableList<GameResult> = Gson().fromJson(winnersJson, type)

        return winnersList
    }

    private fun saveHighScoreList(winnerList: MutableList<GameResult>){

        // Få referensen till SharedPreferences
        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

        // Konvertera listan till en JSON-sträng
        val winnersJson = Gson().toJson(winnerList)

        // Spara JSON-strängen i SharedPreferences
        sharedPreferences.edit().putString("highscoreList", winnersJson).apply()

    }

    private fun clearHighscore(){
        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        Toast.makeText(this, "Highscore cleared!", Toast.LENGTH_SHORT).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_winner)


        val computerPlayerName = intent.getStringExtra("computerName") ?: "Computer"
        val playerName = intent.getStringExtra("playerName") ?: "Unknown Player"
        val playerScore = intent.getIntExtra("playerScore", 0)
        val computerPlayerScore = intent.getIntExtra("computerPlayerScore", 0)
        val avatarHumanPlayer = intent.getStringExtra("avatarHumanOne") ?: "avatarrobot"
        val avatarComputerPlayer = "avatarrobot"

        val winnerImage = findViewById<ImageView>(R.id.winnerImage)
        val winnerText = findViewById<TextView>(R.id.winnerTextView)
        val playAgainButton = findViewById<Button>(R.id.playAgainButton)

        var recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        //Who is the winner
        var gameResult: GameResult
        if (playerScore > computerPlayerScore) {
            winnerText.text = "Winner is $playerName with ${playerScore}p"
            gameResult = GameResult(playerName, playerScore, avatarHumanPlayer)
        } else if (playerScore < computerPlayerScore) {
            winnerText.text = "Winner is $computerPlayerName with ${computerPlayerScore}p"
            gameResult = GameResult(computerPlayerName, computerPlayerScore, avatarComputerPlayer)

        } else {
            //equal
            winnerText.text = "Tied!"
            gameResult = GameResult(playerName, playerScore, avatarHumanPlayer)
        }

        val winnersList: MutableList<GameResult> = getHighScoreList()

        winnersList.add(gameResult)

        saveHighScoreList(winnersList)

        //val mutableWinnersList = winnersList.toMutableList()
        winnersList.sortByDescending { it.score }


        val adapter = HighscoreRecycleAdapter(this, winnersList)
        recyclerView.adapter = adapter

        playAgainButton.setOnClickListener {

            //If user chose to play again, send back to mainActivity, flag intent to clear memory
            val restartIntent = Intent(this, MainActivity::class.java)
            restartIntent.putExtra("playerName", playerName)
            restartIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(restartIntent)
        }
    }
}