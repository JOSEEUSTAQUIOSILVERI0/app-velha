package com.br.eustaquio.jogovelha

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var status: TextView
    private lateinit var gridLayout: Array<Button?>
    private var playerTurn = 'X'
    private var gameActive = true
    private var boardState = Array(3) { CharArray(3) { ' ' } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        status = findViewById(R.id.status)
        gridLayout = arrayOf(
            findViewById(R.id.button1),
            findViewById(R.id.button2),
            findViewById(R.id.button3),
            findViewById(R.id.button4),
            findViewById(R.id.button5),
            findViewById(R.id.button6),
            findViewById(R.id.button7),
            findViewById(R.id.button8),
            findViewById(R.id.button9)
        )

        // Define o clique dos botões do tabuleiro
        for (i in gridLayout.indices) {
            gridLayout[i]?.setOnClickListener { onButtonClick(it) }
        }

        // Define o clique do botão de reiniciar jogo
        findViewById<Button>(R.id.resetButton).setOnClickListener { resetGame() }
    }

    // Função chamada ao clicar em um botão do tabuleiro
    private fun onButtonClick(view: View) {
        if (!gameActive) return
        val button = view as Button
        val index = gridLayout.indexOf(button)
        val row = index / 3
        val col = index % 3

        // Se a célula estiver vazia, faz a jogada
        if (boardState[row][col] == ' ') {
            boardState[row][col] = playerTurn
            button.text = playerTurn.toString()
            checkForWinner()  // Verifica se há um vencedor
            if (gameActive) {
                // Alterna entre os jogadores somente se o jogo ainda estiver ativo
                playerTurn = if (playerTurn == 'X') 'O' else 'X'
                status.text = "Jogador $playerTurn na vez"
            }
        }
    }

    // Função que verifica se há um vencedor
    private fun checkForWinner() {
        val winningPositions = arrayOf(
            // Linhas
            arrayOf(0, 1, 2), arrayOf(3, 4, 5), arrayOf(6, 7, 8),
            // Colunas
            arrayOf(0, 3, 6), arrayOf(1, 4, 7), arrayOf(2, 5, 8),
            // Diagonais
            arrayOf(0, 4, 8), arrayOf(2, 4, 6)
        )

        for (positions in winningPositions) {
            val (a, b, c) = positions
            if (boardState[a / 3][a % 3] == boardState[b / 3][b % 3] &&
                boardState[b / 3][b % 3] == boardState[c / 3][c % 3] &&
                boardState[a / 3][a % 3] != ' '
            ) {
                gameActive = false
                val winner = boardState[a / 3][a % 3]
                Log.d("Game", "Vencedor: $winner")
                status.text = "Campeão $winner!"  // Mensagem de vitória como "Campeão X" ou "Campeão O"
                return
            }
        }

        // Verifica se o jogo terminou em empate
        if (boardState.all { row -> row.all { cell -> cell != ' ' } }) {
            gameActive = false
            status.text = "Empate!"
        }
    }

    // Função para reiniciar o jogo
    private fun resetGame() {
        // Reinicializa o estado do tabuleiro
        boardState = Array(3) { CharArray(3) { ' ' } }
        playerTurn = 'X'
        gameActive = true
        status.text = "Jogador X na vez"

        // Limpa o texto dos botões
        for (button in gridLayout) {
            button?.text = ""
        }
    }
}
