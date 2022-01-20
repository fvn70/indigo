package indigo

data class Card(val rank: String, val suit: String) {
    override fun toString(): String {
        return "$rank$suit"
    }
}

class Deck {
    val ranks = listOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
    val suits = listOf("♦", "♥", "♠", "♣")
    val cards = mutableListOf<Card>()
    init {
        reset()
    }

    fun reset() {
        cards.clear()
        for (s in suits) {
            for (r in ranks) {
                cards.add(Card(r, s))
            }
        }
    }

    fun shuffle() {
        cards.shuffle()
    }

    fun get(num: Int) {
        if (num <= cards.size) {
            repeat(num) {
                print("${cards[cards.size - 1]} ")
                cards.removeAt(cards.size - 1)
            }
            println()
        } else {
            println("The remaining cards are insufficient to meet the request.")
        }
    }
}
fun main() {
    val deck = Deck()

    while (true) {
        println("Choose an action (reset, shuffle, get, exit):")
        val cmd = readLine()!!
        when (cmd) {
            "reset" -> {
                deck.reset()
                println("Card deck is reset.")
            }
            "shuffle" -> {
                deck.shuffle()
                println("Card deck is shuffled.")
            }
            "get" -> {
                println("Number of cards:")
                try {
                    val n = readLine()!!.toInt()
                    if (n in 1..52) {
                        deck.get(n)
                    } else {
                        throw Exception("Invalid number of cards.")
                    }
                } catch (e: Exception) {
                    println("Invalid number of cards.")
                }
            }
            "exit" -> {
                println("Bye")
                break
            }
            else -> println("Wrong action.")
        }
    }
}