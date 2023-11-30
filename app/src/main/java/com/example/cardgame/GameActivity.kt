package com.example.cardgame

import android.graphics.Color
import android.graphics.Color.parseColor
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.postDelayed
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import org.w3c.dom.Text

class GameActivity : AppCompatActivity() {

    var selectedLockedAnswerButton: TextView? = null
    var selectedAnswer: String = ""

    var selectedLockedAnswerButtonComputerPlayer: TextView? = null
    var selectedAnswerComputerPlayer: String = ""

    lateinit var displayedComputerCardView: ImageView
    lateinit var displayedPlayerCardView: ImageView
    lateinit var displayedComputerPlayerCardView: ImageView
    lateinit var cardsLeftBoxView: TextView
    lateinit var robot: ImageView
    lateinit var robotTextView: TextView
    lateinit var tapImageView: ImageView
    lateinit var closeImageView: ImageView

    var playerLockedAnswerJokerView: TextView? = null
    var playerLockedAnswerLowerView: TextView? = null
    var playerLockedAnswerHigherView: TextView? = null
    var playerLockedAnswerMatchView: TextView? = null

    var computerPlayerLockedAnswerJokerView: TextView? = null
    var computerPlayerLockedAnswerLowerView: TextView? = null
    var computerPlayerLockedAnswerHigherView: TextView? = null
    var computerLockedAnswerMatchView: TextView? = null
//    val cardsLeftBoxView: TextView? = null

    private val theDeck = Deck()

    private val computerDeck = theDeck.getComputerDeck()
    private val computerPlayerDeck = theDeck.getComputerPlayerDeck()
    private val playerDeck = theDeck.getPlayerDeck()

    val player = Player("Mattias")
    val computerPlayer = Player("ComputerPlayer")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)


        //Define variables

        closeImageView = findViewById<ImageView>(R.id.closeImageView)

        closeImageView.setOnClickListener{
            finish()
        }

        displayedComputerCardView = findViewById<ImageView>(R.id.displayedComputerCard)

        val coverCardBottomView = findViewById<ImageView>(R.id.covercardBottom)
        val coverCardMiddleView = findViewById<ImageView>(R.id.covercardMiddle)
        val coverCardTopView = findViewById<ImageView>(R.id.covercardTop)

        playerLockedAnswerJokerView = findViewById<TextView>(R.id.playerLockedAnswerJoker)
        playerLockedAnswerLowerView = findViewById<TextView>(R.id.playerLockedAnswerLower)
        playerLockedAnswerHigherView = findViewById<TextView>(R.id.playerLockedAnswerHigher)
        playerLockedAnswerMatchView = findViewById<TextView>(R.id.playerLockedAnswerMatch)

        cardsLeftBoxView = findViewById<TextView>(R.id.cardsLeftBox)


        robot = findViewById<ImageView>(R.id.robotImageView)
        robotTextView = findViewById<TextView>(R.id.questionTextView)

        tapImageView = findViewById<ImageView>(R.id.tapImageView)

        computerPlayerLockedAnswerJokerView = findViewById<TextView>(R.id.computerLockedAnswerJoker)
        computerPlayerLockedAnswerLowerView = findViewById<TextView>(R.id.computerLockedAnswerLower)
        computerPlayerLockedAnswerHigherView =
            findViewById<TextView>(R.id.computerLockedAnswerHigher)
        computerLockedAnswerMatchView = findViewById<TextView>(R.id.computerPlayerLockedAnswerMatch)

        val playerCoverCardView = findViewById<ImageView>(R.id.playerCoverCard)
        displayedPlayerCardView = findViewById<ImageView>(R.id.displayedPlayerCard)
        var playerScoreView = findViewById<TextView>(R.id.scorePlayerTextView)

        val computerCoverCardView = findViewById<ImageView>(R.id.computerCoverCard)
        displayedComputerPlayerCardView = findViewById<ImageView>(R.id.displayedComputerPlayerCard)
        var computerPlayerScoreView = findViewById<TextView>(R.id.scoreComputerTextView)

        //Clear answer (set color tag)
        clearAnswer()
        hideRobot()


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
        playerLockedAnswerMatchView?.setOnClickListener {
            lockAnswer(it as TextView, "match")
        }

    }

    private fun checkWin() {
        selectedAnswer
    }

    private fun showRemainingCards() {
        cardsLeftBoxView.text = "${computerDeck.size} cards left"
    }

    private fun showFirstCard() {

        //Load gif of shuffled card
        val imageResource = R.drawable.shuffle
        Glide.with(this)
            .load(imageResource)
            .into(displayedComputerCardView)

        Handler(Looper.getMainLooper()).postDelayed({
            //display first card in the deck
            displayedComputerCardView.setImageResource(android.R.color.transparent)

//            val displayThisCard = computerDeck.removeAt(0)
//
//            val imageName = displayThisCard.imageName
//            val resID = resources.getIdentifier(imageName, "drawable", packageName)
//            displayedComputerCardView.setBackgroundResource(resID)
            displayedComputerCardView.setBackgroundResource(R.drawable.covercard)
            displayedComputerCardView.isVisible = true

            showRemainingCards()
            showRobot()
        }, 3000)

    }

    private fun showPlayerNextCard() {
        if (playerDeck.isNotEmpty()) {

            val nextCard = playerDeck.removeAt(0)

            displayedPlayerCardView.setImageResource(android.R.color.transparent)

            val imageName = nextCard.imageName
            val resID = resources.getIdentifier(imageName, "drawable", packageName)
            displayedPlayerCardView.setBackgroundResource(resID)
            displayedPlayerCardView.isVisible = true
        }
    }

    private fun showComputerPlayerCard() {
        if (computerPlayerDeck.isNotEmpty()) {

            val nextCard = computerPlayerDeck.removeAt(0)

            displayedComputerPlayerCardView.setImageResource(android.R.color.transparent)

            val imageName = nextCard.imageName
            val resID = resources.getIdentifier(imageName, "drawable", packageName)
            displayedComputerPlayerCardView.setBackgroundResource(resID)
            displayedComputerPlayerCardView.isVisible = true
        }
    }

    private fun showNextCardByDealer() {

        if (computerDeck.isNotEmpty()) {

            val nextCard = computerDeck.removeAt(0)

            displayedComputerCardView.setImageResource(android.R.color.transparent)

            val imageName = nextCard.imageName
            val resID = resources.getIdentifier(imageName, "drawable", packageName)
            displayedComputerCardView.setBackgroundResource(resID)
            displayedComputerCardView.isVisible = true

            showRemainingCards()

//            if (computerDeck.size > 17) {
//                tapImageView.isVisible = true
//                Handler(Looper.getMainLooper()).postDelayed({
//                    tapImageView.isVisible = false
//                }, 2000)
//            }

        } else {
            //send to winner page?
        }

    }

    private fun clearAnswer() {

        if (selectedLockedAnswerButton != null) {

            val nonNullTextViews = listOf(
                playerLockedAnswerJokerView,
                playerLockedAnswerLowerView,
                playerLockedAnswerHigherView,
                playerLockedAnswerMatchView,
                computerLockedAnswerMatchView,
                computerPlayerLockedAnswerJokerView,
                computerPlayerLockedAnswerHigherView,
                computerPlayerLockedAnswerLowerView
            ).filterNotNull()
            initializeTextViews(nonNullTextViews)

            selectedLockedAnswerButton = null
            selectedLockedAnswerButtonComputerPlayer = null
            selectedAnswer = ""
            selectedAnswerComputerPlayer = ""

        }
        //"Throw away" used cards, display cover cards
        throwAwayCards()
    }

    private fun throwAwayCards() {

        hideRobot()

        displayedComputerPlayerCardView.setImageResource(android.R.color.transparent)
        displayedPlayerCardView.setImageResource(android.R.color.transparent)
        displayedComputerCardView.setImageResource(android.R.color.transparent)

        displayedPlayerCardView.setBackgroundResource(R.drawable.covercard)
        displayedComputerPlayerCardView.setBackgroundResource(R.drawable.covercard)
        displayedComputerCardView.setBackgroundResource(R.drawable.covercard)
    }

    private fun displayCoverCards() {

        //Ok so now display both players cards

        Handler(Looper.getMainLooper()).postDelayed({
            showComputerPlayerCard()
        }, 2000)

        Handler(Looper.getMainLooper()).postDelayed({
            showPlayerNextCard()
        }, 3000)

        Handler(Looper.getMainLooper()).postDelayed({
            showNextCardByDealer()
        }, 5000)

    }

    //Clear answer (set color tag)
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (event?.action == MotionEvent.ACTION_UP) {
            clearAnswer()
        }

        return true
    }

    private fun showRobot() {
        robot.isVisible = true
        robotTextView.isVisible = true
    }

    private fun hideRobot() {
        robot.isVisible = false
        robotTextView.isVisible = false

    }

    private fun lockAnswer(selection: TextView, nameOfAnswer: String) {
        if (selectedLockedAnswerButton == null) {
            setAndTagAlpha(selection, 1f)
            selectedLockedAnswerButton = selection
            selectedAnswer = nameOfAnswer

            computerSelectAnswer()?.let { lockAnswerComputerPlayer(it) }

            //display cover cards
            displayCoverCards()
        }
    }

    private fun lockAnswerComputerPlayer(selection: TextView) {
        if (selectedLockedAnswerButtonComputerPlayer == null) {
            setAndTagAlpha(selection, 1f)
            selectedLockedAnswerButtonComputerPlayer = selection

            when (selection) {
                computerPlayerLockedAnswerJokerView -> selectedAnswerComputerPlayer = "joker"
                computerPlayerLockedAnswerLowerView -> selectedAnswerComputerPlayer = "lower"
                computerPlayerLockedAnswerHigherView -> selectedAnswerComputerPlayer = "higher"
                computerLockedAnswerMatchView -> selectedAnswerComputerPlayer = "match"
                else -> selectedAnswerComputerPlayer = "joker"
            }
        }
    }

    private fun setAndTagAlpha(selection: TextView, alpha: Float) {
        selection.alpha = alpha
        selection.setTag(alpha)
    }

    private fun initializeTextViews(textViews: List<TextView>) {
        for (textView in textViews) {
            setAndTagAlpha(textView, 0.5f)
        }
    }

    private fun computerSelectAnswer(): TextView? {
        var thisAnswer = (1..4).random()

        when (thisAnswer) {
            1 -> return computerPlayerLockedAnswerJokerView
            2 -> return computerPlayerLockedAnswerLowerView
            3 -> return computerPlayerLockedAnswerHigherView
            4 -> return computerLockedAnswerMatchView

            else -> computerPlayerLockedAnswerJokerView
        }
        return computerPlayerLockedAnswerJokerView
    }

}