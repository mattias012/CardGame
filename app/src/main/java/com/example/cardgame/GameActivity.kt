package com.example.cardgame

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment

class GameActivity : AppCompatActivity() {

    val handler = Handler(Looper.getMainLooper())

    private var selectedLockedAnswerButton: TextView? = null
    private var selectedAnswer: String = ""

    private var selectedLockedAnswerButtonComputerPlayer: TextView? = null
    private var selectedAnswerComputerPlayer: String = ""

    private lateinit var displayedComputerCardView: ImageView
    private lateinit var displayedPlayerCardView: ImageView
    private lateinit var displayedComputerPlayerCardView: ImageView
    private lateinit var cardsLeftBoxView: TextView

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
    private var currentDisplayedDealerCard: Card? = null
    private var currentDisplayedPlayerCard: Card? = null
    private var currentDisplayedComputerPlayerCard: Card? = null

    private val computerDeck = theDeck.getComputerDeck()
    private val computerPlayerDeck = theDeck.getComputerPlayerDeck()
    private val playerDeck = theDeck.getPlayerDeck()

    private lateinit var coverCardBottomView: ImageView
    private lateinit var coverCardMiddleView: ImageView
    private lateinit var coverCardTopView: ImageView
    private lateinit var computerCoverCardView: ImageView
    private lateinit var playerCoverCardView: ImageView
    private lateinit var computerCoverCardBottomView: ImageView
    private lateinit var playerCoverCardBottomView: ImageView

    private lateinit var dealerDeckCoverList: MutableList<ImageView>
    private lateinit var computerPlayerDeckCoverList: MutableList<ImageView>
    private lateinit var playerDeckCoverList: MutableList<ImageView>

    lateinit var player: Player

    private val computerPlayer = Player("CardioBot")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        //Initialize Views
        initImageViews()
        initTextViews()

        var playerName = intent.getStringExtra("playerName")?.trimEnd()
        player = Player(playerName)
        player.avatar = "avatarhumanone"

        //In case player wants to quit current game
        closeImageView.setOnClickListener {
            finish()
        }

        //Clear answer (set color tag first time)
        clearAnswer()

        //Show start fragment
        showStartFragment()

        //Lock in answer buttons
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
    private fun showStartFragment(){
        //Send player setting to fragmentview to change avatar and dispaly name
        val bundle = Bundle()
        bundle.putString("avatar", player.avatar)
        bundle.putString("playerName", player.name)

        val fragment = AnimationStart()
        fragment.arguments = bundle

        showAnimationFragment(R.id.container_robot, fragment)
        Handler(Looper.getMainLooper()).postDelayed({

            removeAnimationFragment(R.id.container_robot)
        }, 7000)
    }
    private fun showAnimationFragment(containerId: Int, fragment: Fragment) {

        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(containerId, fragment, "$containerId")
        transaction.commit()
    }

    private fun removeAnimationFragment(containerId: Int) {
        val animationFragment = supportFragmentManager.findFragmentByTag("$containerId")

        if (animationFragment != null) {
            val transaction = supportFragmentManager.beginTransaction()
            transaction.remove(animationFragment)
            transaction.commit()
        } else {
            Toast.makeText(this, "Animation not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculatePlayerPoints(answ: String, valuePlayer: Int?, valueDealer: Int?): Int {

        Log.d(
            "!!!",
            "calculatePlayerPoints called with answ: $answ, valuePlayer: $valuePlayer, valueDealer: $valueDealer"
        )

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
        val computerPlayerPoints =
            calculatePlayerPoints(selectedAnswerComputerPlayer, valueComputerPlayer, valueDealer)

        if (humanPlayerPoints > 0) {
            showAnimationFragment(R.id.containerPlayer, AnimationFragment())

            Handler(Looper.getMainLooper()).postDelayed({
                removeAnimationFragment(R.id.containerPlayer)
            }, 1150)
        }
        if (computerPlayerPoints > 0) {
            showAnimationFragment(R.id.containerComputer, AnimationFragment())
            showAnimationFragment(R.id.container_robot, AnimationRobot())
            Handler(Looper.getMainLooper()).postDelayed({
                removeAnimationFragment(R.id.containerComputer)
                removeAnimationFragment(R.id.container_robot)
            }, 1150)
        }

        // add points to each player
        player.score = player.score + humanPlayerPoints
        computerPlayer.score = computerPlayer.score + computerPlayerPoints
        Log.d(
            "!!!",
            "humanPlayerPoints: $humanPlayerPoints, computerPlayerPoints: $computerPlayerPoints"
        )
    }

    private fun showRemainingCards() {
        cardsLeftBoxView.text = "${computerDeck.size} cards left"
    }

    private fun showScore() {
        playerScoreView.text = "Score: ${player.score}p"
        computerPlayerScoreView.text = "Score: ${computerPlayer.score}p"
    }

    private fun showCard(deck: MutableList<Card>, displayedCardView: ImageView) {

        if (deck.isNotEmpty()) {

            val nextCard = deck.removeAt(0)

            when (displayedCardView) {
                displayedPlayerCardView -> {
                    currentDisplayedPlayerCard = nextCard
                }

                displayedComputerPlayerCardView -> {
                    currentDisplayedComputerPlayerCard = nextCard
                }

                displayedComputerCardView -> {
                    currentDisplayedDealerCard = nextCard
                }
            }

            displayedCardView.setImageResource(android.R.color.transparent)

            val imageName = nextCard.imageName
            val resID = resources.getIdentifier(imageName, "drawable", packageName)
            displayedCardView.setBackgroundResource(resID)
            displayedCardView.isVisible = true
        }
    }

    private fun showNextCardByDealer() {

        if (computerDeck.isNotEmpty()) {
            showCard(computerDeck, displayedComputerCardView)
            showRemainingCards()
            checkWin()
            showScore()
            removeCoverCardsInDeck(dealerDeckCoverList)
        }
    }

    private fun removeCoverCardsInDeck(listOfCoverCardsDeck: MutableList<ImageView>) {
        if (computerDeck.isNotEmpty()) {

            if (computerDeck.size == 2) {
                listOfCoverCardsDeck[0].isGone = true
            } else if (computerDeck.size == 1) {
                listOfCoverCardsDeck[1].isGone = true
            }
        }
    }

    private fun clearAnswer() {

        if (computerDeck.isNotEmpty()) {
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
        else {
            endOfGame()
        }
    }

    private fun throwAwayCards() {

        displayedComputerPlayerCardView.setImageResource(android.R.color.transparent)
        displayedPlayerCardView.setImageResource(android.R.color.transparent)
        displayedComputerCardView.setImageResource(android.R.color.transparent)

        displayedPlayerCardView.setBackgroundResource(R.drawable.covercard)
        displayedComputerPlayerCardView.setBackgroundResource(R.drawable.covercard)
        displayedComputerCardView.setBackgroundResource(R.drawable.covercard)
    }

    private fun displayCoverCards() {

        //Ok so now display both players cards
        showCard(computerPlayerDeck, displayedComputerPlayerCardView)
        showCard(playerDeck, displayedPlayerCardView)

        //Show dealar card 0,5 sek later
        handler.postDelayed({
            showNextCardByDealer()
        }, 500)

    }

    private fun endOfGame() {
        if (computerDeck.isEmpty()) {

            //Checkout to next winner/highscore page, send som data as well
            val intent = Intent(this, WinnerActivity::class.java)
            intent.putExtra("computerName", computerPlayer.name)
            intent.putExtra("playerName", player.name ?: "Unknown Player")
            intent.putExtra("playerScore", player.score)
            intent.putExtra("computerPlayerScore", computerPlayer.score)
            intent.putExtra("avatarHumanOne", player.avatar)

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

    private fun initImageViews() {
        displayedComputerCardView = findViewById<ImageView>(R.id.displayedComputerCard)

        closeImageView = findViewById<ImageView>(R.id.closeImageView)
        displayedComputerCardView = findViewById<ImageView>(R.id.displayedComputerCard)
        coverCardBottomView = findViewById<ImageView>(R.id.covercardBottom)
        coverCardMiddleView = findViewById<ImageView>(R.id.covercardMiddle)
        coverCardTopView = findViewById<ImageView>(R.id.covercardTop)
        computerCoverCardView = findViewById<ImageView>(R.id.computerPlayerCoverCard)
        playerCoverCardView = findViewById<ImageView>(R.id.playerCoverCard)
        computerCoverCardBottomView =
            findViewById<ImageView>(R.id.computerPlayerCoverCardBottom)
        playerCoverCardBottomView = findViewById<ImageView>(R.id.playerCoverCardBottom)
        displayedComputerPlayerCardView =
            findViewById<ImageView>(R.id.displayedComputerPlayerCard)
        displayedPlayerCardView = findViewById<ImageView>(R.id.displayedPlayerCard)

        dealerDeckCoverList = mutableListOf(coverCardBottomView, coverCardMiddleView)
        computerPlayerDeckCoverList =
            mutableListOf(computerCoverCardBottomView, computerCoverCardView)
        playerDeckCoverList = mutableListOf(playerCoverCardBottomView, playerCoverCardView)
    }

    private fun initTextViews() {
        playerLockedAnswerJokerView = findViewById<TextView>(R.id.playerLockedAnswerJoker)
        playerLockedAnswerLowerView = findViewById<TextView>(R.id.playerLockedAnswerLower)
        playerLockedAnswerHigherView = findViewById<TextView>(R.id.playerLockedAnswerHigher)
        playerLockedAnswerMatchView = findViewById<TextView>(R.id.playerLockedAnswerMatch)
        computerPlayerScoreView = findViewById<TextView>(R.id.scoreComputerTextView)
        cardsLeftBoxView = findViewById<TextView>(R.id.cardsLeftBox)
        computerPlayerLockedAnswerJokerView =
            findViewById<TextView>(R.id.computerLockedAnswerJoker)
        computerPlayerLockedAnswerLowerView =
            findViewById<TextView>(R.id.computerLockedAnswerLower)
        computerPlayerLockedAnswerHigherView =
            findViewById<TextView>(R.id.computerLockedAnswerHigher)
        computerLockedAnswerMatchView =
            findViewById<TextView>(R.id.computerPlayerLockedAnswerMatch)
        playerScoreView = findViewById<TextView>(R.id.scorePlayerTextView)

    }
}