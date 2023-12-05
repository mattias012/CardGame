package com.example.cardgame

import android.animation.ObjectAnimator
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.airbnb.lottie.LottieAnimationView

class AnimationStart() : Fragment() {

    private lateinit var animationViewarrowPlayer: LottieAnimationView
    private lateinit var animationViewarrowComputerPlayer: LottieAnimationView
    private lateinit var playerAvatarView: ImageView
    private lateinit var computerPlayerAvatarView: ImageView
    private lateinit var playerTextView: TextView

    override fun onCreateView(

        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_player, container, false)

        animationViewarrowPlayer =
            view.findViewById<LottieAnimationView>(R.id.my_animation_view_arrow)
        animationViewarrowComputerPlayer =
            view.findViewById<LottieAnimationView>(R.id.my_animation_view_arrow_robot)
        playerAvatarView = view.findViewById<ImageView>(R.id.playerAvatar)
        computerPlayerAvatarView = view.findViewById<ImageView>(R.id.computerPlayerAvatar)
        playerTextView = view.findViewById<TextView>(R.id.playerTextView)

        val avatarLink = arguments?.getString("avatar")
        val playerName = arguments?.getString("playerName")

        playerTextView.text = playerName

        setFrameAndAvatar(playerAvatarView, avatarLink)
        setFrameAndAvatar(computerPlayerAvatarView, "avatarrobot")

        playerAvatarView.isVisible = true
        startAnimation()

        return view
    }
    private fun setFrameAndAvatar(view: ImageView, avatarLink: String?){

        view.setImageResource(android.R.color.transparent)

        val resID = resources.getIdentifier(avatarLink, "drawable", requireContext().packageName)
        val imageDrawable = ResourcesCompat.getDrawable(resources, resID, null)
        val frameDrawable = ResourcesCompat.getDrawable(resources, R.drawable.image_border, null)

        if (imageDrawable != null && frameDrawable != null) {

            val layers = arrayOf<Drawable>(imageDrawable, frameDrawable)
            val layerDrawable = LayerDrawable(layers)
            view.setImageDrawable(layerDrawable)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            startFadeOutAnimation()
        }, 4000)
    }

    private fun startAnimation() {

        animationViewarrowPlayer.playAnimation()
        animationViewarrowComputerPlayer.playAnimation()
        Handler(Looper.getMainLooper()).postDelayed({
            animationViewarrowPlayer.cancelAnimation()
            animationViewarrowComputerPlayer.cancelAnimation()
            animationViewarrowPlayer.isVisible = false
            animationViewarrowComputerPlayer.isVisible = false
        }, 2000)
    }

    private fun startFadeOutAnimation() {
        view?.let {
            val fadeOut = ObjectAnimator.ofFloat(it, "alpha", 1f, 0f)
            fadeOut.duration = 4000
            fadeOut.start()
        }
    }
}