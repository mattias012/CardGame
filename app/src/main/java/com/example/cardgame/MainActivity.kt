package com.example.cardgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView

class MainActivity : AppCompatActivity() {

    //lateinit var playerNameView: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var playerNameView = findViewById<EditText>(R.id.enterNameView)
        val buttonPlay = findViewById<ImageView>(R.id.letsgobutton)

        buttonPlay.setOnClickListener{
            handleButtonClick(playerNameView.text.toString())
        }
    }

    private fun handleButtonClick(playerName : String){

        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("playerName", playerName)
        //Flagga för att cleara minnet
        startActivity(intent)
    }
}