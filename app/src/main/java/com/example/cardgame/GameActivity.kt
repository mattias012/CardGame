package com.example.cardgame

import android.graphics.Color
import android.graphics.Color.parseColor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.postDelayed
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import org.w3c.dom.Text

class GameActivity : AppCompatActivity() {

    var selectedLockedAnswerButton: TextView? = null
    var selectedAnswer: String = ""
    lateinit var displayedComputerCardView : ImageView

    var playerLockedAnswerJokerView: TextView? = null
    var playerLockedAnswerLowerView: TextView? = null
    var playerLockedAnswerHigherView: TextView? = null

//    val cardsLeftBoxView: TextView? = null

    val theDeck = Deck()

    val computerDeck = theDeck.getComputerDeck()
    val computerPlayerDeck = theDeck.getComputerPlayerDeck()
    val playerDeck = theDeck.getPlayerDeck()

    val player = Player("Mattias")
    val computerPlayer = Player("ComputerPlayer")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        //Define variables
        displayedComputerCardView = findViewById<ImageView>(R.id.displayedComputerCard)
        val coverCardBottomView = findViewById<ImageView>(R.id.covercardBottom)
        val coverCardMiddleView = findViewById<ImageView>(R.id.covercardMiddle)
        val coverCardTopView = findViewById<ImageView>(R.id.covercardTop)

         playerLockedAnswerJokerView = findViewById<TextView>(R.id.playerLockedAnswerJoker)
         playerLockedAnswerLowerView = findViewById<TextView>(R.id.playerLockedAnswerLower)
         playerLockedAnswerHigherView = findViewById<TextView>(R.id.playerLockedAnswerHigher)

        val cardsLeftBoxView = findViewById<TextView>(R.id.cardsLeftBox)
        cardsLeftBoxView.text = "${computerDeck.size} cards left"




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

        //Clear answer (set color tag)
        clearAnswer()


        //"Shuffle" and display first card by dealer
        showFirstCard()

        //Lock in answer
        playerLockedAnswerJokerView?.setOnClickListener {
            lockAnswer(it as TextView, "joker")
        }
        playerLockedAnswerLowerView?.setOnClickListener {
            lockAnswer(it as TextView, "lower")
        }
        playerLockedAnswerHigherView?.setOnClickListener {
            lockAnswer(it as TextView, "higher")
        }



    }

    private fun showFirstCard(){

        //Load gif of shuffled card
        val imageResource = R.drawable.shuffle
        Glide.with(this)
            .load(imageResource)
            .into(displayedComputerCardView)

        Handler(Looper.getMainLooper()).postDelayed({
            //display first card in the deck
            displayedComputerCardView.setImageResource(android.R.color.transparent)

            val displayThisCard = computerDeck.removeAt(0)

            Log.d("!!!", "${displayThisCard.imageName}")

            val imageName = displayThisCard.imageName
            val resID = resources.getIdentifier(imageName, "drawable", packageName)
            displayedComputerCardView.setBackgroundResource(resID)
            displayedComputerCardView.isVisible = true

        }, 3000)

    }

    private fun showNextCard() {

        if (computerDeck.isNotEmpty()) {

            val nextCard = computerDeck.removeAt(0)

            displayedComputerCardView.setImageResource(android.R.color.transparent)

            val imageName = nextCard.imageName
            val resID = resources.getIdentifier(imageName, "drawable", packageName)
            displayedComputerCardView.setBackgroundResource(resID)
            displayedComputerCardView.isVisible = true

            Log.d("!!!", "${nextCard.imageName}")

            //Clear answer (set color tag)
            Handler(Looper.getMainLooper()).postDelayed({
                clearAnswer()
            }, 2000)

        }
        else {
            //send to winner page
        }

    }

    private fun clearAnswer(){

        if (selectedLockedAnswerButton != null) {

            val nonNullTextViews = listOf(playerLockedAnswerJokerView, playerLockedAnswerLowerView, playerLockedAnswerHigherView).filterNotNull()
            initializeTextViews(nonNullTextViews)

            selectedLockedAnswerButton = null
            selectedAnswer = ""

        }
    }
    private fun lockAnswer(selection: TextView, nameOfAnswer: String) {


        if (selectedLockedAnswerButton == null) {
            setAndTagColor(selection, "#E86F6F")
            selectedLockedAnswerButton = selection
            selectedAnswer = nameOfAnswer

            //show next card for dealer
            showNextCard()
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