package com.example.cardgame

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView


class AnimationRobot() : Fragment() {

    //Fragment used when CardioBOT talks
    private lateinit var animationViewRobot: LottieAnimationView
    private lateinit var robotTextViewTalk: TextView

    //List of used punches..
    private val usedTalks = mutableListOf<String>()

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_robot, container, false)

        animationViewRobot = view.findViewById<LottieAnimationView>(R.id.my_animation_view_robot)
        robotTextViewTalk = view.findViewById<TextView>(R.id.robotTextViewTalk)
        animationViewRobot.playAnimation()

        talkRobot()

        return view
    }
    override fun onStop() {
        super.onStop()
        animationViewRobot.cancelAnimation()
    }
    private fun talkRobot(){

        //Get punchline to display
        val punchLine = getRandomPunchLine(usedTalks)
        typewriterEffect(punchLine, 5)
        usedTalks.add(punchLine)

    }

    //Print out the words with a postDelayed fun
    fun typewriterEffect(text: String, delay: Long) {
        val handler = Handler(Looper.getMainLooper())
        var i = 0
        val runnable = object : Runnable {
            override fun run() {
                if (i < text.length) {
                    robotTextViewTalk.text = "${robotTextViewTalk.text}${text[i]}"
                    i++
                    handler.postDelayed(this, delay)
                }
            }
        }
        handler.post(runnable)
    }

    //Create punchlines, return a string that has not yet been shown, maximum 18 strings (18 cards)
    private fun getRandomPunchLine(usedTalks: List<String>): String {
        val punchLines = listOf(
            "Yeay! I don't have much to say... blame the developer..",
            "I'm not a gambler, but I always bet on myself!",
            "Why do I love card games? Because I always hold the winning hand!",
            "They say luck is a factor in games. I say it's all skill, baby!",
            "In the game of life, I'm the ace up the sleeve.",
            "I don't play cards to win; I play to dominate!",
            "What's my strategy? Winning. Always winning.",
            "They call it luck; I call it being unbeatable.",
            "Why do I shuffle cards so well? Because I control the game!",
            "I don't need a royal flush; I'm already the king of the game.",
            "Why do I smile during card games? Because victory is my favorite card.",
            "In this game, I'm not just a player; I'm the grandmaster.",
            "Why do I play cards? To show everyone how winning is done.",
            "My deck is stacked with success, and I deal it flawlessly.",
            "Why do I love winning? It's a habit, and I'm addicted.",
            "Why do I love high-stakes games? Because I'm always on top!",
            "What's my secret? I turn every game into a victory parade.",
            "They say fortune favors the bold. Well, I'm bolder than ever!"
        )

        val unusedPunchLines = punchLines - usedTalks
        return unusedPunchLines.random()
    }

}
