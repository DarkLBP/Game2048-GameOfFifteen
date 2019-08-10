package games.gameOfFifteen

/*
 * This function should return the parity of the permutation.
 * true - the permutation is even
 * false - the permutation is odd
 * https://en.wikipedia.org/wiki/Parity_of_a_permutation

 * If the game of fifteen is started with the wrong parity, you can't get the correct result
 *   (numbers sorted in the right order, empty cell at last).
 * Thus the initial permutation should be correct.
 */
fun isEven(permutation: List<Int>): Boolean {
    var inverted = 0
    val mutableValues = permutation.toMutableList()
    var keepGoing = true
    while (keepGoing) {
        keepGoing = false
        for (index in 1 until mutableValues.size) {
            if (mutableValues[index - 1] > mutableValues[index]) {
                mutableValues[index - 1] = mutableValues[index].also { mutableValues[index] = mutableValues[index - 1] }
                keepGoing = true
                inverted++
            }
        }
    }
    return inverted % 2 == 0
}