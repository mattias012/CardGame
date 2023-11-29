package com.example.cardgame

import android.graphics.Color
import android.graphics.Color.parseColor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import org.w3c.dom.Text

class GameActivity : AppCompatActivity() {

    var selectedLockedAnswer: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        val theDeck = Deck()

        val computerDeck = theDeck.getComputerDeck()
        val computerPlayerDeck = theDeck.getComputerPlayerDeck()
        val playerDeck = theDeck.getPlayerDeck()

        val player = Player("Mattias")
        val computerPlayer = Player("ComputerPlayer")

        //Define variables
        var displayedComputerCardView = findViewById<ImageView>(R.id.displayedComputerCard)
        val coverCardBottomView = findViewById<ImageView>(R.id.covercardBottom)
        val coverCardMiddleView = findViewById<ImageView>(R.id.covercardMiddle)
        val coverCardTopView = findViewById<ImageView>(R.id.covercardTop)

        val playerLockedAnswerJokerView = findViewById<TextView>(R.id.playerLockedAnswerJoker)
        val playerLockedAnswerLowerView = findViewById<TextView>(R.id.playerLockedAnswerLower)
        val playerLockedAnswerHigherView = findViewById<TextView>(R.id.playerLockedAnswerHigher)

        val computerLockedAnswerJokerView = findViewById<TextView>(R.id.computerLockedAnswerJoker)
        val computerLockedAnswerLowerView = findViewById<TextView>(R.id.computerLockedAnswerLower)
        val computerLockedAnswerHigherView = findViewById<TextView>(R.id.computerLockedAnswerHigher)

        val playerCoverCardView = findViewById<ImageView>(R.id.playerCoverCard)
        var displayedPlayerCardView = findViewById<ImageView>(R.id.displayedPlayerCard)
        var playerScoreView = findViewById<TextView>(R.id.scorePlayerTextView)

        val computerCoverCardView = findViewById<ImageView>(R.id.computerCoverCard)
        var displayedComputerPlayerCardView =
            findViewById<ImageView>(R.id.displayedComputerPlayerCard)
        var computerPlayerScoreView = findViewById<TextView>(R.id.scoreComputerTextView)



        //Lock in correct answer
        initializeTextViews(
            listOf(
                playerLockedAnswerJokerView,
                playerLockedAnswerLowerView,
                playerLockedAnswerHigherView
            )
        )
        //setAndTagColor(playerLockedAnswerJokerView, "#C1C1C1")

        playerLockedAnswerJokerView.setOnClickListener {
            toggleLock(it as TextView)
        }
        playerLockedAnswerLowerView.setOnClickListener {
            toggleLock(it as TextView)
        }
        playerLockedAnswerHigherView.setOnClickListener {
            toggleLock(it as TextView)
        }


        //Load gif of shuffled card
        val imageResource = R.drawable.shuffle
        Glide.with(this)
            .load(imageResource)
            .into(displayedComputerCardView)

        Handler(Looper.getMainLooper()).postDelayed({
            //display first card in the deck
            displayedComputerCardView.setImageResource(android.R.color.transparent)

            var displayThisCard = computerDeck[0]

            val imageName = displayThisCard.imageName
            val resID = resources.getIdentifier(imageName, "drawable", packageName)
            displayedComputerCardView.setBackgroundResource(resID)
            displayedComputerCardView.isVisible = true
        }, 3000)

    }

    private fun toggleLock(selection: TextView) {
        selectedLockedAnswer?.let {
            if (it != selection) {
                setAndTagColor(it, "#C1C1C1")
                selectedLockedAnswer = null
            }
        }

        if (selectedLockedAnswer == null) {
            setAndTagColor(selection, "#E86F6F")
            selectedLockedAnswer = selection

        }
        else if (selection.tag == "#E86F6F") {
            setAndTagColor(selection, "#C1C1C1")
            selectedLockedAnswer = null
        }
    }

    private fun setAndTagColor(selection: TextView, color: String) {
        selection.setBackgroundColor(Color.parseColor(color))
        selection.tag = color
    }

    private fun initializeTextViews(textViews: List<TextView>) {
        for (textView in textViews) {
            setAndTagColor(textView, "#C1C1C1")
        }
    }

}