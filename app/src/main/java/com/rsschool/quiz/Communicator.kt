package com.rsschool.quiz

interface Communicator {
    fun openNextQuestion(selectedOption: Int)
    fun openPreviousQuestion(selectedOption: Int)
}