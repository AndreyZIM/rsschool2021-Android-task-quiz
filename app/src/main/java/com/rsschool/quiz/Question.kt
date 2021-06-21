package com.rsschool.quiz

class Question(
    private var question: String,
    private var option1: String,
    private var option2: String,
    private var option3: String,
    private var option4: String,
    private var option5: String,
    private var answer: Int
) {

    fun getQuestion(): String {
        return question
    }

    fun setQuestion(question: String) {
        this.question = question
    }

    fun getOption1(): String {
        return option1
    }

    fun setOption1(option1: String) {
        this.option1 = option1
    }

    fun getOption2(): String {
        return option2
    }

    fun setOption2(option2: String) {
        this.option2 = option2
    }

    fun getOption3(): String {
        return option3
    }

    fun setOption3(option3: String) {
        this.option3 = option3
    }

    fun getOption4(): String {
        return option4
    }

    fun setOption4(option4: String) {
        this.option4 = option4
    }

    fun getOption5(): String {
        return option5
    }

    fun setOption5(option5: String) {
        this.option5 = option5
    }

    fun getAnswer(): Int {
        return answer
    }

    fun setAnswer(answer: Int) {
        this.answer = answer
    }
}
