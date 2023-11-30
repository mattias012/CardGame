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
    lateinit var displayedPlayerCardView : ImageView
    lateinit var displayedComputerPlayerCardView: ImageView
    lateinit var cardsLeftBoxView: TextView

    var playerLockedAnswerJokerView: TextView? = null
    var playerLockedAnswerLowerView: TextView? = null
    var playerLockedAnswerHigherView: TextView? = null
    var playerLockedAnswerMatchView: TextView? = null

    var  computerPlayerLockedAnswerJokerView : TextView? = null
    var computerPlayerLockedAnswerLowerView : TextView? = null
    var  computerPlayerLockedAnswerHigherView : TextView? = null

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
        playerLockedAnswerMatchView = findViewById<TextView>(R.id.playerLockedAnswerMatch)

        cardsLeftBoxView = findViewById<TextView>(R.id.cardsLeftBox)


         computerPlayerLockedAnswerJokerView = findViewById<TextView>(R.id.computerLockedAnswerJoker)
         computerPlayerLockedAnswerLowerView = findViewById<TextView>(R.id.computerLockedAnswerLower)
         computerPlayerLockedAnswerHigherView = findViewById<TextView>(R.id.computerLockedAnswerHigher)

        val playerCoverCardView = findViewById<ImageView>(R.id.playerCoverCard)
         displayedPlayerCardView = findViewById<ImageView>(R.id.displayedPlayerCard)
        var playerScoreView = findViewById<TextView>(R.id.scorePlayerTextView)

        val computerCoverCardView = findViewById<ImageView>(R.id.computerCoverCard)
         displayedComputerPlayerCardView = findViewById<ImageView>(R.id.displayedComputerPlayerCard)
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

    private fun checkWin(){
        selectedAnswer
    }
    private fun showRemainingCards(){
        cardsLeftBoxView.text = "${computerDeck.size} cards left"
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

            val imageName = displayThisCard.imageName
            val resID = resources.getIdentifier(imageName, "drawable", packageName)
            displayedComputerCardView.setBackgroundResource(resID)
            displayedComputerCardView.isVisible = true

            showRemainingCards()

        }, 3000)

    }

    private fun showPlayerNextCard(){
        if (playerDeck.isNotEmpty()) {

            val nextCard = playerDeck.removeAt(0)

            displayedPlayerCardView.setImageResource(android.R.color.transparent)

            val imageName = nextCard.imageName
            val resID = resources.getIdentifier(imageName, "drawable", packageName)
            displayedPlayerCardView.setBackgroundResource(resID)
            displayedPlayerCardView.isVisible = true
        }

    }
    private fun showComputerPlayerCard(){
        if (computerPlayerDeck.isNotEmpty()) {

            val nextCard = computerPlayerDeck.removeAt(0)

            displayedComputerPlayerCardView.setImageResource(android.R.color.transparent)

            val imageName = nextCard.imageName
            val resID = resources.getIdentifier(imageName, "drawable", packageName)
            displayedComputerPlayerCardView.setBackgroundResource(resID)
            displayedComputerPlayerCardView.isVisible = true
        }
    }
    private fun showNextCard() {

        if (computerDeck.isNotEmpty()) {

            val nextCard = computerDeck.removeAt(0)

            displayedComputerCardView.setImageResource(android.R.color.transparent)

            val imageName = nextCard.imageName
            val resID = resources.getIdentifier(imageName, "drawable", packageName)
            displayedComputerCardView.setBackgroundResource(resID)
            displayedComputerCardView.isVisible = true

            Handler(Looper.getMainLooper()).postDelayed({
                showComputerPlayerCard()
            }, 1000)

            Handler(Looper.getMainLooper()).postDelayed({
                showPlayerNextCard()
            }, 2000)

            //Clear answer (set color tag)
            Handler(Looper.getMainLooper()).postDelayed({
                clearAnswer()
            }, 3000)

            showRemainingCards()
        }
        else {
            //send to winner page
        }

    }

    private fun clearAnswer(){

        if (selectedLockedAnswerButton != null) {

            val nonNullTextViews = listOf(playerLockedAnswerJokerView, playerLockedAnswerLowerView, playerLockedAnswerHigherView, playerLockedAnswerMatchView).filterNotNull()
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