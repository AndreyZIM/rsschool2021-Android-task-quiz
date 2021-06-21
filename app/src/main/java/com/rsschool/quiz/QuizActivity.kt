package com.rsschool.quiz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity(), Communicator {

    private var questionCounter: Int = 0
    private var questionCountTotal: Int = 0
    private val answerBuffer: Array<Int> = Array(5) { -1 }
    private val stylesList = listOf(
        R.style.Theme_Quiz_First,
        R.style.Theme_Quiz_Second,
        R.style.Theme_Quiz_Third,
        R.style.Theme_Quiz_Fourth,
        R.style.Theme_Quiz_Fifth
    )
    private lateinit var colorsList: List<Int>

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = ActivityQuizBinding.inflate(layoutInflater)
        setContentView(activity.root)
        colorsList = listOf(
            ContextCompat.getColor(this, R.color.deep_orange_100_dark),
            ContextCompat.getColor(this, R.color.yellow_100_dark),
            ContextCompat.getColor(this, R.color.light_green_100_dark),
            ContextCompat.getColor(this, R.color.cyan_100_dark),
            ContextCompat.getColor(this, R.color.deep_purple_100_dark),
        )
        val dbHelper = QuizDbHelper(this)
        val questionList = dbHelper.getAllQuestions()
        questionCountTotal = questionList.size
        openQuestion(0, -1)
        setTheme(stylesList[0])
        window.statusBarColor = colorsList[0]
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun openNextQuestion(selectedOption: Int) {
        answerBuffer[questionCounter] = selectedOption
        questionCounter++
        if (questionCounter < questionCountTotal) {
            setTheme(stylesList[questionCounter])
            openQuestion(questionCounter, answerBuffer[questionCounter])
            window.statusBarColor = colorsList[questionCounter]
        } else showResult()
    }

    override fun openPreviousQuestion(selectedOption: Int) {
        answerBuffer[questionCounter] = selectedOption
        questionCounter--
        if (questionCounter >= 0) {
            setTheme(stylesList[questionCounter])
            openQuestion(questionCounter, answerBuffer[questionCounter])
            window.statusBarColor = colorsList[questionCounter]
        }
    }

    private fun openQuestion(questionNumber: Int, currentOption: Int) {
        val fragment: Fragment = FragmentQuiz.newInstance(questionNumber, currentOption)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun showResult() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("FIRST_ANSWER", answerBuffer[0])
        intent.putExtra("SECOND_ANSWER", answerBuffer[1])
        intent.putExtra("THIRD_ANSWER", answerBuffer[2])
        intent.putExtra("FOURTH_ANSWER", answerBuffer[3])
        intent.putExtra("FIFTH_ANSWER", answerBuffer[4])
        startActivity(intent)
    }
}