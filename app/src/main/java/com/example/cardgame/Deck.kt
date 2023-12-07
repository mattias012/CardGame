package com.example.cardgame

class Deck() {

    private val theDeck = mutableListOf<Card>()
    private val values = listOf(2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15)
    private val suits = listOf("hearts", "diamonds", "clubs", "spades")


    private var computerDeck = mutableListOf<Card>()
    private var computerPlayerDeck = mutableListOf<Card>()
    private var playerDeck = mutableListOf<Card>()

    init {
        createMyDeck()
        theDeck.shuffle()
        //Remove two cards after shuffle to be able to create 3 equal piles
        theDeck.removeAt(0)
        theDeck.removeAt(0)
        splitDeck()
    }

    private fun createMyDeck() {

        //Create the deck
        for (suit in suits){
            for (value in values){

                //Create path to card image
                var imageName = "$suit$value"

                var addThisCard = Card(suit, value, imageName)

                theDeck.add(addThisCard)
            }
        }
    }

    private fun splitDeck(){

        //Split deck into 3 equal piles

        val computerDeck = mutableListOf<Card>()
        val computerPlayerDeck = mutableListOf<Card>()
        val playerDeck = mutableListOf<Card>()

        var counter = 1

        while (theDeck.isNotEmpty()) {
            val card = theDeck.removeAt(0)

            when (counter) {
                1 -> playerDeck.add(card)
                2 -> computerDeck.add(card)
                3 -> computerPlayerDeck.add(card)
            }

            //Increase counter when it hits 3, ie start over
            counter++
            if (counter > 3) {
                counter = 1
            }
        }

        this.computerDeck = computerDeck
        this.computerPlayerDeck = computerPlayerDeck
        this.playerDeck = playerDeck
    }

    fun getComputerDeck() : MutableList<Card> {
        return this.computerDeck
    }
    fun getComputerPlayerDeck() : MutableList<Card> {
        return this.computerPlayerDeck
    }
    fun getPlayerDeck() : MutableList<Card> {
        return this.playerDeck
    }
}