package com.example.cardgame

import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    var currentSelectedImage: ImageView? = null
    var avatar: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val avatarOne = findViewById<ImageView>(R.id.avatarHumanOne)
        val avatarTwo = findViewById<ImageView>(R.id.avatarHumanTwo)
        val avatarThree = findViewById<ImageView>(R.id.avatarHumanThree)
        val avatarFour = findViewById<ImageView>(R.id.avatarHumanFour)

        val rule = findViewById<ImageView>(R.id.ruleImageView)

        avatarOne.setBackgroundResource(R.drawable.image_border)
        avatarTwo.setBackgroundResource(R.drawable.image_border)
        avatarThree.setBackgroundResource(R.drawable.image_border)
        avatarFour.setBackgroundResource(R.drawable.image_border)

        avatarOne.setOnClickListener {
            onImageSelected(it as ImageView)
            avatar = "avatarhumanone"
        }
        avatarTwo.setOnClickListener {
            onImageSelected(it as ImageView)
            avatar = "avatarhumantwo"
        }
        avatarThree.setOnClickListener {
            onImageSelected(it as ImageView)
            avatar = "avatarhumanthree"
        }
        avatarFour.setOnClickListener {
            onImageSelected(it as ImageView)
            avatar = "avatarhumanfour"
        }


        var playerNameView = findViewById<EditText>(R.id.enterNameView)
        val buttonPlay = findViewById<Button>(R.id.letsgobutton)

        var playerName = intent.getStringExtra("playerName") ?: "Unknown Player"

        //Let playName remain the same if user plays more than one time
        if (playerName.isNotEmpty() && !playerName.equals("Unknown Player")) {
            playerNameView.setText(playerName)
        }

        //Lets play
        buttonPlay.setOnClickListener {
            handleButtonClick(playerNameView.text.toString())
        }

        rule.setOnClickListener {
         showRules()
        }
    }

    private fun onImageSelected(newImage: ImageView) {
        // Återställ den tidigare valda bilden
        currentSelectedImage?.setBackgroundResource(R.drawable.image_border)

        // Uppdatera den valda bilden
        newImage.setBackgroundResource(R.drawable.image_border_selected)
        currentSelectedImage = newImage
    }
    private fun showRules(){
        val ruleDialog = Rule()
        ruleDialog.show(supportFragmentManager, "RuleDialog")
    }

    private fun handleButtonClick(playerName: String) {

        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("playerName", playerName)
        intent.putExtra("avatar", avatar)

        startActivity(intent)
    }
}