package com.example.cardgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView

class AnimationFragment() : Fragment() {

    private lateinit var animationView: LottieAnimationView

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_pointscore, container, false)

        animationView = view.findViewById<LottieAnimationView>(R.id.my_animation_view)

        animationView.progress = 1f
        animationView.speed = -1f
        animationView.playAnimation()

        return view
    }

}