package com.frank.tic_tac_toe_app

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.clickable
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun TicTacToeGame() {
    var board by remember { mutableStateOf(List(3) { MutableList(3) { "" } }) }
    var xTurn by remember { mutableStateOf(true) }
    var status by remember { mutableStateOf("Player X's turn") }
    var gameOver by remember { mutableStateOf(false) }
    var winningCells by remember { mutableStateOf(emptyList<Pair<Int, Int>>()) }
    var xScore by remember { mutableStateOf(0) }
    var oScore by remember { mutableStateOf(0) }

    fun checkWinner(): Pair<String?, List<Pair<Int, Int>>> {
        // Rows
        for (i in 0..2) {
            if (board[i][0] != "" && board[i][0] == board[i][1] && board[i][1] == board[i][2])
                return board[i][0] to listOf(i to 0, i to 1, i to 2)
        }
        // Columns
        for (i in 0..2) {
            if (board[0][i] != "" && board[0][i] == board[1][i] && board[1][i] == board[2][i])
                return board[0][i] to listOf(0 to i, 1 to i, 2 to i)
        }
        // Diagonal
        if (board[0][0] != "" && board[0][0] == board[1][1] && board[1][1] == board[2][2])
            return board[0][0] to listOf(0 to 0, 1 to 1, 2 to 2)
        if (board[0][2] != "" && board[0][2] == board[1][1] && board[1][1] == board[2][0])
            return board[0][2] to listOf(0 to 2, 1 to 1, 2 to 0)
        // Draw
        if (board.all { row -> row.all { it != "" } }) return "Draw" to emptyList()
        return null to emptyList()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = status, fontSize = 24.sp, modifier = Modifier.padding(16.dp))
        Spacer(modifier = Modifier.height(16.dp))
        LazyColumn {
            itemsIndexed(board) { rowIndex, row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    for (colIndex in 0..2) {
                        val cell = row[colIndex]
                        val isWinningCell = winningCells.contains(rowIndex to colIndex)
                        val cellBg = if (isWinningCell) Color(0xFF388E3C) else Color.LightGray
                        val cellText = if (isWinningCell) Color.White else Color.Black
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .padding(4.dp)
                                .background(cellBg, shape = RoundedCornerShape(10.dp))
                                .clickable(enabled = cell == "" && !gameOver) {
                                    if (cell == "" && !gameOver) {
                                        board = board.mapIndexed { r, rowList ->
                                            rowList.mapIndexed { c, value ->
                                                if (r == rowIndex && c == colIndex) if (xTurn) "X" else "O" else value
                                            }.toMutableList()
                                        }
                                        val (winner, cells) = checkWinner()
                                        if (winner != null) {
                                            gameOver = true
                                            winningCells = cells
                                            status = if (winner == "Draw") "It's a draw!!" else "Player $winner wins!"
                                            if (winner == "X") xScore++
                                            if (winner == "O") oScore++
                                        } else {
                                            xTurn = !xTurn
                                            status = if (xTurn) "Player X's turn" else "Player O's turn"
                                        }
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = cell,
                                fontSize = 32.sp,
                                color = cellText,
                                textAlign = TextAlign.Center,
                                fontWeight = if (isWinningCell) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            board = List(3) { MutableList(3) { "" } }
            xTurn = true
            status = "Player X's turn"
            gameOver = false
            winningCells = emptyList()
        },
            modifier = Modifier.padding(bottom = 16.dp),
            shape = RoundedCornerShape(32.dp)
        ) {
            Text("Reset Game", fontSize = 18.sp)
        }
        // Score Counter
        Row(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Player X: $xScore", fontSize = 20.sp, modifier = Modifier.padding(end = 24.dp))
            Text("Player O: $oScore", fontSize = 20.sp)
        }
        Button(
            onClick = {
                xScore = 0
                oScore = 0
                board = List(3) { MutableList(3) { "" } }
                xTurn = true
                status = "Player X's turn"
                gameOver = false
                winningCells = emptyList()
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFE0E0E0), // light gray
                contentColor = Color(0xFF424242) // dark gray
            ),
            shape = RoundedCornerShape(32.dp)
        ) {
            Text("Reset Score")
        }
    }
} 