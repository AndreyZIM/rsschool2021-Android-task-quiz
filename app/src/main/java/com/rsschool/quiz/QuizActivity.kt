package com.rsschool.quiz

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
            ContextCompat.getColor(this, R.color.light_green_100_light),
            ContextCompat.getColor(this, R.color.deep_orange_100_light),
            ContextCompat.getColor(this, R.color.yellow_100_light),
            ContextCompat.getColor(this, R.color.cyan_100_light),
            ContextCompat.getColor(this, R.color.deep_purple_100_light)
        )
        val dbHelper = QuizDbHelper(this)
        val questionList = dbHelper.getAllQuestions()
        questionCountTotal = questionList.size
        openQuestion(0)
        setTheme(stylesList[0])
        window.statusBarColor = colorsList[0]
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun openNextQuestion(selectedOption: Int) {
        answerBuffer[questionCounter] = selectedOption
        questionCounter++
        if (questionCounter < questionCountTotal) {
            setTheme(stylesList[questionCounter])
            openQuestion(questionCounter)
            window.statusBarColor = colorsList[questionCounter]
        } else showResult()
    }

    override fun openPreviousQuestion(selectedOption: Int) {
        answerBuffer[questionCounter] = selectedOption
        questionCounter--
        if (questionCounter >= 0) {
            setTheme(stylesList[questionCounter])
            openQuestion(questionCounter)
            window.statusBarColor = colorsList[questionCounter]
        }
    }

    private fun openQuestion(questionNumber: Int) {
        val fragment: Fragment =
            FragmentQuiz.newInstance(questionNumber, answerBuffer[questionNumber])
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun showResult() {
        val intent = Intent(this, ResultActivity::class.java)
        val mBundle = bundleOf(
            "FIRST_ANSWER" to answerBuffer[0],
            "SECOND_ANSWER" to answerBuffer[1],
            "THIRD_ANSWER" to answerBuffer[2],
            "FOURTH_ANSWER" to answerBuffer[3],
            "FIFTH_ANSWER" to answerBuffer[4]
        )
        intent.putExtras(mBundle)
        startActivity(intent)
    }
}