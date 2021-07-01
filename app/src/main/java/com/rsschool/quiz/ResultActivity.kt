package com.rsschool.quiz

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.rsschool.quiz.databinding.ActivityResultBinding

class ResultActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.P)
    @SuppressLint("SetTextI18n", "QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activity = ActivityResultBinding.inflate(layoutInflater)
        setContentView(activity.root)

        val answers: Array<Int> = Array(5) { 0 }
        val mBundle = intent.extras
        if (mBundle != null) {
            answers[0] = mBundle.getInt(FIRST_ANSWER_KEY)
            answers[1] = mBundle.getInt(SECOND_ANSWER_KEY)
            answers[2] = mBundle.getInt(THIRD_ANSWER_KEY)
            answers[3] = mBundle.getInt(FOURTH_ANSWER_KEY)
            answers[4] = mBundle.getInt(FIFTH_ANSWER_KEY)
        }
        val correctAnswers = countCorrectAnswers(answers)

        activity.progressCircularBg.progress = 100f / 3 * 2
        activity.resultText.text = "$correctAnswers / 5"

        activity.progressCircular.apply {
            progressMax = 150f
            setProgressWithAnimation(20f * correctAnswers, 1000)
        }
        activity.button.setOnClickListener {
            shareResult(prepareForSharing(answers, correctAnswers))
        }
        activity.buttonBack.setOnClickListener {
            backToMainActivity()
        }
        activity.buttonExit.setOnClickListener {
            onBackPressed()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun countCorrectAnswers(answers: Array<Int>): Int {
        val dbHelper = QuizDbHelper(this)
        val questionList = dbHelper.getAllQuestions()

        var correctAnswers = 0

        for (i in questionList.indices) {
            if (answers[i] == questionList[i].getAnswer()) correctAnswers++
        }
        return correctAnswers
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun prepareForSharing(answers: Array<Int>, correctAnswers: Int): String {
        var textForSharing = ""
        val dbHelper = QuizDbHelper(this)
        val questionList = dbHelper.getAllQuestions()

        for (i in questionList.indices) {
            textForSharing += "\n\n"
            when (answers[i]) {
                0 -> textForSharing += "${questionList[i].getQuestion()}\nYour answer: ${questionList[i].getOption1()}"
                1 -> textForSharing += "${questionList[i].getQuestion()}\nYour answer: ${questionList[i].getOption2()}"
                2 -> textForSharing += "${questionList[i].getQuestion()}\nYour answer: ${questionList[i].getOption3()}"
                3 -> textForSharing += "${questionList[i].getQuestion()}\nYour answer: ${questionList[i].getOption4()}"
                4 -> textForSharing += "${questionList[i].getQuestion()}\nYour answer: ${questionList[i].getOption5()}"
            }
        }

        textForSharing = """
            You have $correctAnswers correct
            answers of 5.
        """.trimIndent() + textForSharing

        return textForSharing
    }

    private fun backToMainActivity() {
        finishAffinity()
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun shareResult(result: String) {
        val sendIntent = Intent()
        sendIntent.action = Intent.ACTION_SEND
        sendIntent.putExtra(Intent.EXTRA_TEXT, result)
        sendIntent.type = "text/plain"
        if (sendIntent.resolveActivity(packageManager) != null) startActivity(sendIntent)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }

    companion object {
        const val FIRST_ANSWER_KEY = "FIRST_ANSWER"
        const val SECOND_ANSWER_KEY = "SECOND_ANSWER"
        const val THIRD_ANSWER_KEY = "THIRD_ANSWER"
        const val FOURTH_ANSWER_KEY = "FOURTH_ANSWER"
        const val FIFTH_ANSWER_KEY = "FIFTH_ANSWER"
    }
}