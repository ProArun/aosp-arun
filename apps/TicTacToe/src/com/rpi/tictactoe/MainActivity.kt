package com.rpi.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- 1. Game State Definitions ---

enum class Player { X, O }
enum class GameState { RUNNING, X_WINS, O_WINS, DRAW }

// Represents a single cell on the board
typealias Board = List<MutableList<Player?>>

// --- 2. Game Logic ---

/**
 * Checks all rows, columns, and diagonals for a winner.
 */
fun checkWinner(board: Board): GameState {
    val size = board.size

    // Check Rows and Columns
    for (i in 0 until size) {
        // Check Row i
        if (board[i][0] != null && board[i].all { it == board[i][0] }) {
            return if (board[i][0] == Player.X) GameState.X_WINS else GameState.O_WINS
        }
        // Check Column i
        if (board[0][i] != null && (0 until size).all { board[it][i] == board[0][i] }) {
            return if (board[0][i] == Player.X) GameState.X_WINS else GameState.O_WINS
        }
    }

    // Check Diagonals
    // Top-Left to Bottom-Right
    if (board[0][0] != null && (0 until size).all { board[it][it] == board[0][0] }) {
        return if (board[0][0] == Player.X) GameState.X_WINS else GameState.O_WINS
    }
    // Top-Right to Bottom-Left
    if (board[0][size - 1] != null && (0 until size).all { board[it][size - 1 - it] == board[0][size - 1] }) {
        return if (board[0][size - 1] == Player.X) GameState.X_WINS else GameState.O_WINS
    }

    // Check for Draw (if all cells are filled)
    if (board.all { row -> row.all { cell -> cell != null } }) {
        return GameState.DRAW
    }

    return GameState.RUNNING
}

// --- 3. Compose UI Components ---

@Composable
fun TicTacToeBoard(board: Board, onCellClicked: (row: Int, col: Int) -> Unit) {
    val boardSize = 300.dp
    Box {
        // Canvas to draw the grid lines
        Canvas(modifier = Modifier
            .size(boardSize)
            .padding(8.dp)
        ) {
            val lineThickness = 5.dp.toPx()
            val cellSize = size.width / 3f

            // Draw horizontal lines
            for (i in 1..2) {
                drawLine(
                    color = Color.Black,
                    start = Offset(0f, cellSize * i),
                    end = Offset(size.width, cellSize * i),
                    strokeWidth = lineThickness
                )
            }

            // Draw vertical lines
            for (i in 1..2) {
                drawLine(
                    color = Color.Black,
                    start = Offset(cellSize * i, 0f),
                    end = Offset(cellSize * i, size.height),
                    strokeWidth = lineThickness
                )
            }
        }

        // Grid layout for clickable cells
        Column(modifier = Modifier.size(boardSize)) {
            board.forEachIndexed { row, rowData ->
                Row(modifier = Modifier.weight(1f)) {
                    rowData.forEachIndexed { col, player ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight()
                                .clickable { onCellClicked(row, col) },
                            contentAlignment = Alignment.Center
                        ) {
                            if (player != null) {
                                PlayerSymbol(player = player, modifier = Modifier.fillMaxSize(0.8f))
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun PlayerSymbol(player: Player, modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        if (player == Player.X) {
            val strokeWidth = 8.dp.toPx()
            // Draw 'X'
            drawLine(
                color = Color.Red,
                start = Offset(0f, 0f),
                end = Offset(size.width, size.height),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
            drawLine(
                color = Color.Red,
                start = Offset(size.width, 0f),
                end = Offset(0f, size.height),
                strokeWidth = strokeWidth,
                cap = StrokeCap.Round
            )
        } else {
            // Draw 'O'
            drawOval(
                color = Color.Blue,
                topLeft = Offset(10f, 10f),
                size = Size(size.width - 20f, size.height - 20f),
                style = Stroke(width = 8.dp.toPx())
            )
        }
    }
}

// --- 4. Main Game Screen ---

@Composable
fun TicTacToeGame() {
    // Game State Management
    var board by remember { mutableStateOf(List(3) { MutableList<Player?>(3) { null } }) }
    var currentPlayer by remember { mutableStateOf(Player.X) }
    var gameState by remember { mutableStateOf(GameState.RUNNING) }

    // Logic for making a move
    val onCellClicked: (Int, Int) -> Unit = { row, col ->
        if (gameState == GameState.RUNNING && board[row][col] == null) {
            // 1. Make the move
            board = board.toMutableList().apply {
                this[row] = this[row].toMutableList().apply {
                    this[col] = currentPlayer
                }
            }

            // 2. Check for winner
            gameState = checkWinner(board)

            // 3. Switch player only if game is still running
            if (gameState == GameState.RUNNING) {
                currentPlayer = if (currentPlayer == Player.X) Player.O else Player.X
            }
        }
    }

    // Logic for resetting the game
    val resetGame: () -> Unit = {
        board = List(3) { MutableList<Player?>(3) { null } }
        currentPlayer = Player.X
        gameState = GameState.RUNNING
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        // Status Text
        val statusText = when (gameState) {
            GameState.RUNNING -> "Turn: ${currentPlayer.name}"
            GameState.X_WINS -> "X WINS!"
            GameState.O_WINS -> "O WINS!"
            GameState.DRAW -> "IT'S A DRAW!"
        }
        Row(
            modifier = Modifier.heightIn(50.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = statusText, fontSize = 24.sp)
            // Reset Button
            Button(onClick = resetGame, enabled = gameState != GameState.RUNNING) {
                Text("Play Again")
            }
        }



        // Game Board
        TicTacToeBoard(board = board, onCellClicked = onCellClicked)

        Spacer(modifier = Modifier.height(40.dp))


    }
}

// --- 5. Activity and Theme ---

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            androidx.compose.material3.MaterialTheme { // Use the standard MaterialTheme composable
                androidx.compose.material3.Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TicTacToeGame()
                }
            }
        }
    }
}