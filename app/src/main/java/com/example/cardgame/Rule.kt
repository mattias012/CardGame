package com.example.cardgame

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class Rule() : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            builder.setTitle("Game Rules")
            builder.setMessage(
                "These are the rules for the game.\n\n" +
                        "A single deck + 4 JOKER cards are divided into 3 equal piles, 2 cards are thrown away after shuffle to ensure 18 cards in each pile.\n\n" +
                        "The game is a simple card game where you guess if YOUR next cover card will be lower or higher than the dealer's next card.\n\n" +
                        "You can also guess whether your next card will be a JOKER or EQUAL to dealer card.\n\n" +
                        "Joker also has the card value of 15, i.e it is the highest value in the deck\n" +
                        "These are obvious less likely to occur and therefore more valuable guesses\n\n" +
                        "• HIGHER/LOWER guess equals 1p\n" +
                        "• JOKER guess equals 3p\n" +
                        "• EQUAL guess gives 4p\n\n" +
                        "You play against CardioBOT, a true gambler, no one really knows his next move, it is almost random..\n\n" +
                        "Best of luck in you game, and if CardioBOT wins too often, you can reset the highscore. \u263A"
            )

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}