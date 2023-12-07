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

        //Get the reference to SharedPreferences
        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

        //Get JSON string from SharedPreferences
        val winnersJson = sharedPreferences.getString("highscoreList", "")

        //Convert JSON string to a list of GameResult objects
        return if (winnersJson.isNullOrEmpty()) {
            mutableListOf()
        } else {
            val type = object : TypeToken<List<GameResult>>() {}.type
            val winnersList: MutableList<GameResult> = Gson().fromJson(winnersJson, type)

            //Return the list
            return winnersList
        }
    }

    private fun saveHighScoreList(winnerList: MutableList<GameResult>) {

        //Get the current list from SharedPreferences
        val sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)

        //Convert list to JSON string
        val winnersJson = Gson().toJson(winnerList)

        //Save new list to SharedPreferences
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
            //At this time in case of tied result, the humanplayer's score is added to result
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

        //Get THIS game result, based on timestamp, so we can scroll to that position
        var position = -1
        val recentGameTimestamp = gameResult.timestamp

        for (i in 0 until winnersList.size) {
            if (winnersList[i].timestamp == recentGameTimestamp) {
                position = i
                break
            }
        }

        //Attach the list and recent timestamp to the adapter
        val adapter = HighscoreRecycleAdapter(this, winnersList, recentGameTimestamp)
        recyclerView.adapter = adapter

        //Scroll to THIS result
        recyclerView.smoothScrollToPosition(position)

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
            Toast.makeText(this, "Double tap to clear highscore", Toast.LENGTH_SHORT).show()
        }

        button.setOnClickListener {
            if (!isDoubleTap) {
                isDoubleTap = true
                handler.postDelayed(runnable, 1000) //
            } else {
                handler.removeCallbacks(runnable)
                clearHighscore()
                isDoubleTap = false
            }
        }

    }
}