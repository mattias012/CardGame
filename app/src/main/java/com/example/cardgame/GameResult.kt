package com.example.cardgame

data class GameResult(val playerName: String, val score: Int, val avatar: String, val timestamp: Long = System.currentTimeMillis()){


}