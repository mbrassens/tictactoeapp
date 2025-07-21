package com.frank.tic_tac_toe_app

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TicTacToeGame() {
    var board by remember { mutableStateOf(List(3) { MutableList(3) { "" } }) }
    var xTurn by remember { mutableStateOf(true) }
    var status by remember { mutableStateOf("X's turn") }
    var gameOver by remember { mutableStateOf(false) }

    fun checkWinner(): String? {
        // Rows and columns
        for (i in 0..2) {
            if (board[i][0] != "" && board[i][0] == board[i][1] && board[i][1] == board[i][2]) return board[i][0]
            if (board[0][i] != "" && board[0][i] == board[1][i] && board[1][i] == board[2][i]) return board[0][i]
        }
        // Diagonals
        if (board[0][0] != "" && board[0][0] == board[1][1] && board[1][1] == board[2][2]) return board[0][0]
        if (board[0][2] != "" && board[0][2] == board[1][1] && board[1][1] == board[2][0]) return board[0][2]
        // Draw
        if (board.all { row -> row.all { it != "" } }) return "Draw"
        return null
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
                        Button(
                            onClick = {
                                if (cell == "" && !gameOver) {
                                    board = board.mapIndexed { r, rowList ->
                                        rowList.mapIndexed { c, value ->
                                            if (r == rowIndex && c == colIndex) if (xTurn) "X" else "O" else value
                                        }.toMutableList()
                                    }
                                    val winner = checkWinner()
                                    if (winner != null) {
                                        gameOver = true
                                        status = if (winner == "Draw") "It's a draw!" else "$winner wins!"
                                    } else {
                                        xTurn = !xTurn
                                        status = if (xTurn) "X's turn" else "O's turn"
                                    }
                                }
                            },
                            modifier = Modifier
                                .size(80.dp)
                                .padding(4.dp),
                            enabled = cell == "" && !gameOver
                        ) {
                            Text(text = cell, fontSize = 32.sp)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = {
            board = List(3) { MutableList(3) { "" } }
            xTurn = true
            status = "X's turn"
            gameOver = false
        }) {
            Text("Reset Game")
        }
    }
} 