package com.example.unscramble.data.ui.test

import com.example.unscramble.data.MAX_NO_OF_WORDS
import com.example.unscramble.data.SCORE_INCREASE
import com.example.unscramble.data.getUnscrambledWord
import com.example.unscramble.ui.GameViewModel
import org.junit.Test
import org.junit.Assert.assertFalse
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Assert.assertNotEquals


class GameViewModelTest {
    private val viewModel= GameViewModel()

    @Test
    fun gameviewmodel_CorrectWordGuessed_ScoredUpdateAndErrorFlagUnset(){
        var currentGameUiState=viewModel.uiState.value
        val correctPlayerWord= getUnscrambledWord(currentGameUiState.currentScrambleWord)
        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()

        currentGameUiState=viewModel.uiState.value
        assertFalse(currentGameUiState.isGuessedWordWrong)
        assertEquals(20,currentGameUiState.score)
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER,currentGameUiState.score)
    }
    companion object {
        private const val SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }
    @Test
    fun gameViewModel_IncorrectGuess_ErrorFlagSet(){
        //giving incorrect word
        val incorrectPlayerWord="and"
        viewModel.updateUserGuess(incorrectPlayerWord)
        viewModel.checkUserGuess()
        val currentGameUiState=viewModel.uiState.value
        assertEquals(0,currentGameUiState.score)
        assertTrue(currentGameUiState.isGuessedWordWrong)

    }
    @Test
    fun gameviewModel_Intilialization_FirstWordLoaded(){
        val gameUiState=viewModel.uiState.value
        val unscrambleword=getUnscrambledWord(gameUiState.currentScrambleWord)
        assertNotEquals(unscrambleword,gameUiState.currentScrambleWord)
        assertTrue(gameUiState.currentWordCount==1)
        assertTrue(gameUiState.score==0)
        assertFalse(gameUiState.isGuessedWordWrong)
        assertFalse(gameUiState.isGameOver)
    }
    @Test
    fun gameViewModel_AllWordGuessed_UiStateUpdatedCorrectly(){
        var expectedScore=0
        var currentGameUiState=viewModel.uiState.value
        var correctPlayerWord=getUnscrambledWord(currentGameUiState.currentScrambleWord)
        repeat(MAX_NO_OF_WORDS){
            expectedScore+=SCORE_INCREASE
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()
            currentGameUiState=viewModel.uiState.value
            correctPlayerWord=getUnscrambledWord(currentGameUiState.currentScrambleWord)
            assertEquals(expectedScore,currentGameUiState.score)
        }
        assertEquals(MAX_NO_OF_WORDS,currentGameUiState.currentWordCount)
        assertTrue(currentGameUiState.isGameOver)
    }
    @Test
    fun gameViewModel_WordSkipped_ScoredUnchangedAndWordCountIncreased(){
        var currentGameUiState=viewModel.uiState.value
        val correctPlayerWord=getUnscrambledWord(currentGameUiState.currentScrambleWord)
        viewModel.updateUserGuess(correctPlayerWord)
        viewModel.checkUserGuess()
        currentGameUiState=viewModel.uiState.value
        val lastWordCount=currentGameUiState.currentWordCount
        viewModel.skipWord()
        currentGameUiState=viewModel.uiState.value
        assertEquals(SCORE_AFTER_FIRST_CORRECT_ANSWER,currentGameUiState.score)
        assertEquals(lastWordCount+1,currentGameUiState.currentWordCount)


    }
}