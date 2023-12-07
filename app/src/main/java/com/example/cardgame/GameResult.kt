package com.example.cardgame

//Data class to save game result as an object, later save as json string to sharedperfernces using gson() library
data class GameResult(val playerName: String, val score: Int, val avatar: String, val timestamp: Long = System.currentTimeMillis()){


}