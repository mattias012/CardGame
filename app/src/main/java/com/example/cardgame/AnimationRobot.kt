package com.example.cardgame

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView


class AnimationRobot() : Fragment() {

        private lateinit var animationViewRobot: LottieAnimationView

        override fun onCreateView(

            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {

            val view = inflater.inflate(R.layout.fragment_robot, container, false)

            animationViewRobot = view.findViewById<LottieAnimationView>(R.id.my_animation_view_robot)

            animationViewRobot.playAnimation()

            return view
        }
}
