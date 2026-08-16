package com.example.unscramble.ui

import androidx.lifecycle.ViewModel
import com.example.unscramble.data.allWords
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.unscramble.data.allWords

class GameViewModel:ViewModel() {
    private val _uiState =MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()
    private lateinit var currentWord:String
    private var usedWords: MutableSet<String>= mutuableSetOf()
    private fun shuffleCurrentWord(word:String):String{
        val tempword=word.toCharArray()
        tempword.shuffle()
        while(String(tempword).equals(word)){
            tempword.shuffle()
        }
        return String(tempword)
    }
    private fun pickRandomWordAndShuffle(): String{
        currentWord= allWords.random()
        if(usedWords.contains(currentWord)){
            return pickRandomWordAndShuffle()
        }
        else{
            usedWords.add(currentWord)
            return shuffleCurrentWord(currentWord)
        }
    }
    fun resetGame(){
        usedWords.clear()
        _uiState.value= GameUiState(currentScrambleWord = pickRandomWordAndShuffle())
    }
    init {
        resetGame()
    }
}