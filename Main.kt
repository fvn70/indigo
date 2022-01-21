package indigo

var gameOn = true

data class Card(val rank: String, val suit: String) {
    override fun toString(): String {
        return "$rank$suit"
    }

    override fun equals(other: Any?): Boolean {
        when (other) {
            is Card -> {
                return this.rank == other.rank ||
                        this.suit == other.suit
            }
            else -> return false
        }
    }
}

class Player(val name: String) {
    var cards = mutableListOf<Card>()
    var winCards = mutableListOf<Card>()
    var score = 0

    fun get(num: Int, src: Deck) {
        cards = src.get(num)
    }

    fun setScore() {
        score = winCards.filter { it.rank in "A, 10, J, Q, K" }.size
    }

    fun turn(): Card {
        var idx = cards.lastIndex
        if (name == "Computer") {
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
    var first = 0
    var lastWin = -1
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

    fun calcScore() {
        if (lastWin >= 0) {
            players[lastWin].winCards.addAll(table)
        }
        for (p in players) p.setScore()
        val s0 = players[0].winCards.size
        val s1 = players[1].winCards.size
        if (s0 * s1 > 0) {
            val win = when {
                s0 > s1 -> 0
                s0 < s1 -> 1
                else -> first
            }
            players[win].score += 3
            showScore()
        }
    }

    fun showScore() {
        println("Score: Player ${players[0].score} - Computer ${players[1].score}")
        println("Cards: Player ${players[0].winCards.size} - Computer ${players[1].winCards.size}")
    }

    fun doTurn(p: Player): Boolean {
        if (table.isEmpty()) {
            println("\nNo cards on the table")
        } else {
            val top = table.last()
            println("\n${table.size} cards on the table, and the top card is $top")
            if (table.size == 52) {
                return false
            }
        }
        if (p.cards.size == 0) {
            return false
        }
        val card = p.turn()
        if (card.rank == "E") {
            return false
        }
        table.add(card)
        val idx = table.size - 2

        if (idx >= 0 && card == table[idx]) {
            p.score += table.filter { it.rank in "A, 10, J, Q, K" }.size
            p.winCards.addAll(table)
            lastWin = players.indexOf(p)
            table.clear()
            println("${p.name} wins cards")
            showScore()
        }
        return true
    }

    fun addCards() {
        if (players[0].cards.isEmpty()) {
            players[0].get(6, this)
        }
        if (players[1].cards.isEmpty()) {
            players[1].get(6, this)
        }
    }

    fun game() {
        println("Indigo Card Game")
        while (true) {
            println("Play first?")
            val answer = readLine()!!.lowercase()
            if (answer in "yes no") {
                first = if (answer == "yes") 0 else 1
                players.add(Player("Player"))
                players.add(Player("Computer"))
                break
            }
        }

        shuffle()
        table = get(4)
        println("Initial cards on the table: ${table.joinToString(" ")}")
        var current = first
        while (table.size <= 52) {
            addCards()
            if (!doTurn(players[current])) {
                break
            }
            current = ++current % 2
            if (!doTurn(players[current])) {
                break
            }
            current = ++current % 2
        }

        calcScore()
        println("Game Over")
    }
}

fun main() {
    val deck = Deck()
    deck.game()
}