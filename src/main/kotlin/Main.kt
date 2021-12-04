import java.io.File

data class Board(val rows: MutableList<Array<Int>>) {

    fun addRow(row: Array<Int>) {
        this.rows.add(row)
    }

    fun mark(number: Int) {
        for (row in this.rows) {
            if (row.contains(number)) {
                val loc = row.indexOf(number)
                row[loc] = -1
                break
            }
        }
    }

    fun isWinning(): Boolean {
        // Since width and height is the same, we can check both by rows and columns together.
        for (i in this.rows.indices) {
            if (this.rows[i].all { it == -1 } || this.rows.map { it[i] }.all { it == -1 }) {
                return true
            }
        }

        return false
    }

    fun sumUnmarked(): Int {
        return this.rows.flatMap { it.filter { n -> n != -1 } }.sum()
    }
}

fun solvePart1(numbers: List<Int>, boards: Set<Board>) {
    numbers.forEach { number ->
        // Mark the chosen number.
        boards.forEach { board -> board.mark(number) }

        // Now let's check for any winning board.
        val winningBoards = boards.filter { it.isWinning() }
        if (winningBoards.isNotEmpty()) {
            // We find a winning board.
            val sum = winningBoards[0].sumUnmarked()
            val score = sum * number

            // Print the result
            println("PART 1 ANSWER")
            println(score)
            return
        }
    }
}

fun solvePart2(numbers: List<Int>, boards: Set<Board>) {
    var losingBoard = Board(mutableListOf())
    var finalNumber = -1

    for (number in numbers) {
        // Mark the chosen number.
        boards.forEach { board -> board.mark(number) }

        // Now let's check for any winning board.
        val losingBoards = boards.filter { !it.isWinning() }
        if (losingBoards.size == 1) {
            losingBoard = losingBoards[0]
        } else if (losingBoards.isEmpty()) {
            finalNumber = number
            break
        }
    }

    // We find a losing board.
    val sum = losingBoard.sumUnmarked()
    val score = sum * finalNumber

    // Print the result
    println("PART 2 ANSWER")
    println(score)
}

fun main() {
    val data = File("input.txt").readLines()

    // Now, we need to get the list of input and also the boards.
    // First line is the list of chosen numbers.
    // Start reading the board data at third line. Or easier, every blank line, treat it as the start of a board.
    val chosenNumbers = data[0].split(",").map { it.toInt() }

    // We drop the first 2 lines since we are no longer need it. The first line is the list of numbers to choose,
    // which we have read above. The second line is a blank line, which gives us nothing.
    val boards = mutableSetOf<Board>()
    var board = Board(mutableListOf())
    data.drop(2).forEach {
        if (it == "") {
            boards.add(board)
            board = Board(mutableListOf())

            return@forEach
        }

        board.addRow(it.split(" ").filter { n -> n.isNotBlank() }.map { n -> n.toInt() }.toTypedArray())
    }

    // Add the last board since there is no blank line at the end of the input.
    boards.add(board)

    solvePart1(numbers = chosenNumbers, boards = boards)
    solvePart2(numbers = chosenNumbers, boards = boards)
}