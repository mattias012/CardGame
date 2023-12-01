package com.example.cardgame

import android.content.Intent
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
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import org.w3c.dom.Text

class GameActivity : AppCompatActivity() {

    val handler = Handler(Looper.getMainLooper())

    private lateinit var animationView: LottieAnimationView



    private var selectedLockedAnswerButton: TextView? = null
    private var selectedAnswer: String = ""

    private var selectedLockedAnswerButtonComputerPlayer: TextView? = null
    private var selectedAnswerComputerPlayer: String = ""

    private lateinit var displayedComputerCardView: ImageView
    private lateinit var displayedPlayerCardView: ImageView
    private lateinit var displayedComputerPlayerCardView: ImageView
    private lateinit var cardsLeftBoxView: TextView
    private lateinit var robot: ImageView
    private lateinit var robotTextView: TextView
    private lateinit var closeImageView: ImageView
    private lateinit var playerScoreView: TextView
    private lateinit var computerPlayerScoreView: TextView

    private var playerLockedAnswerJokerView: TextView? = null
    private var playerLockedAnswerLowerView: TextView? = null
    private var playerLockedAnswerHigherView: TextView? = null
    private var playerLockedAnswerMatchView: TextView? = null

    private var computerPlayerLockedAnswerJokerView: TextView? = null
    private var computerPlayerLockedAnswerLowerView: TextView? = null
    private var computerPlayerLockedAnswerHigherView: TextView? = null
    private var computerLockedAnswerMatchView: TextView? = null


    private val theDeck = Deck()
    private var currentDisplayedDealerCard : Card? = null
    private var currentDisplayedPlayerCard : Card? = null
    private var currentDisplayedComputerPlayerCard : Card? = null

    private val computerDeck = theDeck.getComputerDeck()
    private val computerPlayerDeck = theDeck.getComputerPlayerDeck()
    private val playerDeck = theDeck.getPlayerDeck()

    val player = Player("Mattias")
    val computerPlayer = Player("ComputerPlayer")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        animationView = findViewById<LottieAnimationView>(R.id.my_animation_view)
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

        computerPlayerLockedAnswerJokerView = findViewById<TextView>(R.id.computerLockedAnswerJoker)
        computerPlayerLockedAnswerLowerView = findViewById<TextView>(R.id.computerLockedAnswerLower)
        computerPlayerLockedAnswerHigherView =
            findViewById<TextView>(R.id.computerLockedAnswerHigher)
        computerLockedAnswerMatchView = findViewById<TextView>(R.id.computerPlayerLockedAnswerMatch)

        val playerCoverCardView = findViewById<ImageView>(R.id.playerCoverCard)
        displayedPlayerCardView = findViewById<ImageView>(R.id.displayedPlayerCard)
        playerScoreView = findViewById<TextView>(R.id.scorePlayerTextView)

        val computerCoverCardView = findViewById<ImageView>(R.id.computerCoverCard)
        displayedComputerPlayerCardView = findViewById<ImageView>(R.id.displayedComputerPlayerCard)
        computerPlayerScoreView = findViewById<TextView>(R.id.scoreComputerTextView)

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
    private fun calculatePlayerPoints(answ: String, valuePlayer: Int?, valueDealer: Int?): Int {

        Log.d("!!!","calculatePlayerPoints called with answ: $answ, valuePlayer: $valuePlayer, valueDealer: $valueDealer")

        // if for some reason any value is null, return 0
        if (valueDealer == null || valuePlayer == null) {
            return 0
        }

        // Calculate results
        return when (answ) {
            "higher" -> if (valuePlayer > valueDealer) 1 else 0
            "lower" -> if (valuePlayer < valueDealer) 1 else 0
            "match" -> if (valueDealer == valuePlayer) 3 else 0
            "joker" -> if (valuePlayer == 15) 2 else 0
            else -> 0
        }
    }
    private fun checkWin() {
        val valueDealer = currentDisplayedDealerCard?.value
        val valuePlayer = currentDisplayedPlayerCard?.value
        val valueComputerPlayer = currentDisplayedComputerPlayerCard?.value

        val humanPlayerPoints = calculatePlayerPoints(selectedAnswer, valuePlayer, valueDealer)
        val computerPlayerPoints = calculatePlayerPoints(selectedAnswerComputerPlayer, valueComputerPlayer, valueDealer)

        if (humanPlayerPoints > 0){
            startStarPoint()
        }
        // add points to each player
        player.score = player.score + humanPlayerPoints
        computerPlayer.score = computerPlayer.score + computerPlayerPoints
        Log.d("!!!","humanPlayerPoints: $humanPlayerPoints, computerPlayerPoints: $computerPlayerPoints")
    }

    private fun showRemainingCards() {
        cardsLeftBoxView.text = "${computerDeck.size} cards left"
    }
    private fun showScore() {
        playerScoreView.text = "Score: ${player.score}p"
        computerPlayerScoreView.text = "Score: ${computerPlayer.score}p"
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
            displayedComputerCardView.setBackgroundResource(R.drawable.covercard)
            displayedComputerCardView.isVisible = true

            showRemainingCards()
            showRobot()

        }, 3000)

    }
    private fun startStarPoint(){
        animationView.bringToFront()
        (animationView.parent as View).requestLayout()
        (animationView.parent as View).invalidate()
        animationView.isVisible = true
        animationView.setSpeed(0.5f)
        animationView.playAnimation()

//        Handler(Looper.getMainLooper()).postDelayed({
//            animationView.visibility = View.GONE
//        }, 2000)  // 3000 milliseconds = 3 seconds
    }

    private fun showPlayerNextCard() {
        if (playerDeck.isNotEmpty()) {

            val nextCard = playerDeck.removeAt(0)
            currentDisplayedPlayerCard = nextCard

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
            currentDisplayedComputerPlayerCard = nextCard

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

            currentDisplayedDealerCard = nextCard

            displayedComputerCardView.setImageResource(android.R.color.transparent)

            val imageName = nextCard.imageName
            val resID = resources.getIdentifier(imageName, "drawable", packageName)
            displayedComputerCardView.setBackgroundResource(resID)
            displayedComputerCardView.isVisible = true

            showRemainingCards()
            checkWin()
            showScore()

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
        }

        selectedLockedAnswerButton = null
        selectedLockedAnswerButtonComputerPlayer = null
        selectedAnswer = ""
        selectedAnswerComputerPlayer = ""

        //"Throw away" used cards, display new cover cards
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

//        Handler(Looper.getMainLooper()).postDelayed({
            showComputerPlayerCard()
//        }, 2000)
//
//        Handler(Looper.getMainLooper()).postDelayed({
            showPlayerNextCard()
//        }, 3000)

       handler.postDelayed({
            showNextCardByDealer()
        }, 500)



        if(computerDeck.isEmpty()){

            //Who is winner?
            var winner : Player

            if (player.score > computerPlayer.score){
                winner = player
            }
            else {
                winner = computerPlayer
            }

            //Checkout to next winner/highscore page
            val intent = Intent(this, WinnerActivity::class.java)
            intent.putExtra("playerName", winner.name)
            intent.putExtra("score", winner.score)

            //Flagga för att cleara minnet
            startActivity(intent)
        }
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