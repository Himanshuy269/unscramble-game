package com.example.unscramble.ui

import androidx.lifecycle.ViewModel
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.unscramble.data.allWords
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import kotlinx.coroutines.flow.update

class GameViewModel:ViewModel() {
    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    private lateinit var currentWord: String
    private var usedWords: MutableSet<String> = mutableSetOf()
    private fun shuffleCurrentWord(word: String): String {
        val tempword = word.toCharArray()
        tempword.shuffle()
        while (String(tempword).equals(word)) {
            tempword.shuffle()
        }
        return String(tempword)
    }

    private fun pickRandomWordAndShuffle(): String {
        currentWord = allWords.random()
        if (usedWords.contains(currentWord)) {
            return pickRandomWordAndShuffle()
        } else {
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord)
        }
    }

    fun resetGame() {
        usedWords.clear()
        _uiState.value = GameUiState(currentScrambleWord = pickRandomWordAndShuffle())
    }

    init {
        resetGame()
    }

    var userGuess by mutableStateOf("")
        private set

    fun updateUserGuess(guessedWord: String) {
        userGuess = guessedWord
    }
    fun checkUserGuess() {
        if (userGuess.equals(currentWord, ignoreCase = true)) {
            //user case is correct,increase the score
            // and call updateGameState() to prepare for next round
            val updatedScore = _uiState.value.score.plus(SCORE_INCREASE)
            updateGameState(updatedScore)
        } else {
            //user guess is wrong,show an error
            _uiState.update { currentState ->
                currentState.copy(isGuessedWordWrong=true)
            }
        }
        // Reset user guess
        updateUserGuess("")
    }
    private fun updateGameState(updateScore: Int){
        if(usedWords.size== MAX_NO_OF_WORDS){
            //Last round
            _uiState.update{currentState->
                currentState.copy(
                    isGuessedWordWrong = false,
                    score=updateScore,
                    isGameOver=true
                )

            }
        }
        else{_uiState.update { currentState ->
            currentState.copy(
                isGuessedWordWrong = false,
                currentScrambleWord = pickRandomWordAndShuffle(),
                score = updateScore,
                currentWordCount = currentState.currentWordCount.inc()
            )
        }
        }
    }
    fun skipWord(){
        updateGameState(_uiState.value.score)
        //reset user guess
        updateUserGuess("")
    }
}