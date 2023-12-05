package com.example.cardgame

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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

    //Define lateinit variables
    private lateinit var computerPlayerName: String
    private lateinit var playerName: String
    private lateinit var avatarHumanPlayer: String
    private lateinit var avatarComputerPlayer: String

    private lateinit var winnerText: TextView
    private lateinit var playAgainButton: Button
    private lateinit var clearHighscoreButton: Button
    private lateinit var recyclerView: RecyclerView

    private fun getHighScoreList(): MutableList<GameResult> {

        // Hämta referensen till SharedPreferences
        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

        // Hämta JSON-strängen från SharedPreferences
        val winnersJson = sharedPreferences.getString("highscoreList", "")

        // Konvertera JSON-strängen till en lista av GameResult-objekt
        return if (winnersJson.isNullOrEmpty()) {
            mutableListOf()
        } else {
            val type = object : TypeToken<List<GameResult>>() {}.type
            val winnersList: MutableList<GameResult> = Gson().fromJson(winnersJson, type)

            return winnersList
        }
    }

    private fun saveHighScoreList(winnerList: MutableList<GameResult>) {

        // Få referensen till SharedPreferences
        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

        // Konvertera listan till en JSON-sträng
        val winnersJson = Gson().toJson(winnerList)

        // Spara JSON-strängen i SharedPreferences
        sharedPreferences.edit().putString("highscoreList", winnersJson).apply()

    }

    private fun clearHighscore() {
        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("highscoreList", "[]") // Sätt "highscoreList" till en tom JSON-sträng
        editor.apply()
        Toast.makeText(this, "Highscore cleared!", Toast.LENGTH_SHORT).show()
    }

    private fun calculateWinner(playerScore: Int, computerPlayerScore: Int): GameResult {
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

        return gameResult
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_winner)

        //Set variables
        initVar()
        val playerScore = intent.getIntExtra("playerScore", 0)
        val computerPlayerScore = intent.getIntExtra("computerPlayerScore", 0)
        recyclerView.layoutManager = LinearLayoutManager(this)


        //Who is the winner
        //Calculate
        val gameResult = calculateWinner(playerScore, computerPlayerScore)

        //Get latest highscore
        val winnersList: MutableList<GameResult> = getHighScoreList()

        //Save this result
        winnersList.add(gameResult)

        //Save the entire highscore list again (ie update existing)
        saveHighScoreList(winnersList)

        //Sort the list by descending, highest score first
        winnersList.sortByDescending { it.score }

        //Attach the list to the adapter
        val adapter = HighscoreRecycleAdapter(this, winnersList)
        recyclerView.adapter = adapter

        //Play again?
        playAgainButton.setOnClickListener {
            handlePlayAgain()
        }

        //Clear highscore list
        clearHighscoreButton.setOnClickListener {
            handleClearButton(clearHighscoreButton)
        }
    }

    private fun handlePlayAgain() {

        //If user chose to play again, send back to mainActivity, flag intent to clear memory (remove activities in the stack
        val restartIntent = Intent(this, MainActivity::class.java)
        restartIntent.putExtra("playerName", playerName)
        restartIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(restartIntent)
    }

    private fun initVar() {
        computerPlayerName = intent.getStringExtra("computerName") ?: "Computer"
        playerName = intent.getStringExtra("playerName") ?: "Unknown Player"

        avatarHumanPlayer = intent.getStringExtra("avatarHumanOne") ?: "avatarrobot"
        avatarComputerPlayer = "avatarrobot"

        winnerText = findViewById<TextView>(R.id.winnerTextView)
        playAgainButton = findViewById<Button>(R.id.playAgainButton)
        clearHighscoreButton = findViewById<Button>(R.id.clearHighscoreButton)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
    }

    private fun handleClearButton(button: Button) {
        var isDoubleTap = false
        val handler = Handler(Looper.getMainLooper())
        val runnable = Runnable {
            isDoubleTap = false
            Toast.makeText(this, "Double tap to clear high score", Toast.LENGTH_SHORT).show()
        }

        button.setOnClickListener {
            if (!isDoubleTap) {
                isDoubleTap = true
                handler.postDelayed(runnable, 1000) // Kör Runnable efter 1000 ms
            } else {
                handler.removeCallbacks(runnable)
                clearHighscore()
                isDoubleTap = false
            }
        }

    }
}