package indigo
val ranks = listOf("A", "2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K")
val suits = listOf("♦", "♥", "♠", "♣")

fun main() {
    for (r in ranks) {
        print("$r ")
    }
    println()
    for (s in suits) {
        print("$s ")
    }
    println()
    for (s in suits) {
        for (r in ranks) {
            print("$r$s ")
        }
    }
}