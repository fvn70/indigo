package indigo

var gameOn = true

data class Card(val rank: String, val suit: String) {
    override fun toString(): String {
        return "$rank$suit"
    }
}

class Player(val name: String) {
    var cards = mutableListOf<Card>()

    fun get(num: Int, src: Deck) {
        cards = src.get(num)
    }

    fun turn(): Card {
        var idx = cards.lastIndex
        if (name == "comp") {
            println("Computer plays ${cards.last()}")
        } else {
            val s = cards.mapIndexed { i, card ->  "${i + 1})$card" }
            println("Cards in hand: ${s.joinToString(" ")}")
            while (true) {
                println("Choose a card to play (1-${cards.size}):")
                val cmd = readLine()!!
                if (cmd == "exit") {
                    return Card("E", "X")
                }
                if (cmd in "1"..cards.size.toString()) {
                    idx = cmd.toInt() - 1
                    break
                }
            }
        }
        val card = cards[idx]
        cards.removeAt(idx)
        return card
    }
}

class Deck {
    val ranks = listOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
    val suits = listOf("♦", "♥", "♠", "♣")
    val cards = mutableListOf<Card>()
    var table = mutableListOf<Card>()
    val players = mutableListOf<Player>()
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

    fun get(num: Int): MutableList<Card> {
        val list = mutableListOf<Card>()
        if (num <= cards.size) {
            repeat(num) {
                list.add(cards[cards.size - 1])
                cards.removeAt(cards.size - 1)
            }
        }
        return list
    }

    fun game() {
        println("Indigo Card Game")
        while (true) {
            println("Play first?")
            val answer = readLine()!!.lowercase()
            if (answer !in "yes no") {
                continue
            } else {
                if (answer == "yes") {
                    players.add(Player("man"))
                    players.add(Player("comp"))
                } else {
                    players.add(Player("comp"))
                    players.add(Player("man"))
                }
            }
            break
        }

        shuffle()
        table = get(4)
        println("Initial cards on the table: ${table.joinToString(" ")}")

        while (table.size <= 52) {
            if (players[0].cards.isEmpty()) {
                players[0].get(6, this)
            }
            if (players[1].cards.isEmpty()) {
                players[1].get(6, this)
            }
            println("\n${table.size} cards on the table, and the top card is ${table.last()}")
            if (table.size == 52) {
                break
            }

            var card = players[0].turn()
            if (card.rank == "E") {
                break
            }
            table.add(card)

            println("\n${table.size} cards on the table, and the top card is ${table.last()}")
            card = players[1].turn()
            if (card.rank == "E") {
                break
            }
            table.add(card)
        }

        println("Game Over")
    }
}

fun main() {
    val deck = Deck()
    deck.game()
}