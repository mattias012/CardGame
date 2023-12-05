package com.example.cardgame

import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView

class AnimationStart(): Fragment() {

    private lateinit var animationViewarrowPlayer: LottieAnimationView
    private lateinit var animationViewarrowComputerPlayer: LottieAnimationView
    private lateinit var playerAvatarView: ImageView

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_player, container, false)

        animationViewarrowPlayer = view.findViewById<LottieAnimationView>(R.id.my_animation_view_arrow)
        animationViewarrowComputerPlayer = view.findViewById<LottieAnimationView>(R.id.my_animation_view_arrow_robot)
        playerAvatarView = view.findViewById<ImageView>(R.id.playerAvatar)

        val avatarLink = arguments?.getString("avatar")

        val resID = resources.getIdentifier(avatarLink, "drawable", requireContext().packageName)
        playerAvatarView.setBackgroundResource(resID)
        playerAvatarView.isVisible = true

        animationViewarrowPlayer.playAnimation()
        animationViewarrowComputerPlayer.playAnimation()
        Handler(Looper.getMainLooper()).postDelayed({
            animationViewarrowPlayer.cancelAnimation()
            animationViewarrowComputerPlayer.cancelAnimation()
            animationViewarrowPlayer.isVisible = false
            animationViewarrowComputerPlayer.isVisible = false
        }, 2000)

        return view
    }
    fun startFadeOutAnimation() {
        val fadeOut = ObjectAnimator.ofFloat(view, "alpha",  1f, 0f)
        fadeOut.duration = 2000
        fadeOut.start()
    }
}