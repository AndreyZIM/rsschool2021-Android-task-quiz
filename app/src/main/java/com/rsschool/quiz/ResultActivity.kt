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
        val keys = listOf(
            "FIRST_ANSWER",
            "SECOND_ANSWER",
            "THIRD_ANSWER",
            "FOURTH_ANSWER",
            "FIFTH_ANSWER"
        )
        for (i in answers.indices)
            answers[i] = intent.getIntExtra(keys[i], 0)
        val dbHelper = QuizDbHelper(this)
        val questionList = dbHelper.getAllQuestions()
        var correctAnswers = 0

        var textForSharing = ""

        for (i in questionList.indices) {
            if (answers[i] == questionList[i].getAnswer()) correctAnswers++
            when (answers[i]) {
                0 -> textForSharing += "${questionList[i].getQuestion()}\nYour answer: ${questionList[i].getOption1()}\n\n"
                1 -> textForSharing += "${questionList[i].getQuestion()}\nYour answer: ${questionList[i].getOption2()}\n\n"
                2 -> textForSharing += "${questionList[i].getQuestion()}\nYour answer: ${questionList[i].getOption3()}\n\n"
                3 -> textForSharing += "${questionList[i].getQuestion()}\nYour answer: ${questionList[i].getOption4()}\n\n"
                4 -> textForSharing += "${questionList[i].getQuestion()}\nYour answer: ${questionList[i].getOption5()}\n\n"
            }
        }
        activity.resultText.text = "$correctAnswers / 5"

        textForSharing = """
            You have $correctAnswers correct
            answers of 5.
            
            
        """.trimIndent() + textForSharing

        activity.button.setOnClickListener {
            val sendIntent = Intent()
            sendIntent.action = Intent.ACTION_SEND
            sendIntent.putExtra(Intent.EXTRA_TEXT, textForSharing)
            sendIntent.type = "text/plain"
            if (sendIntent.resolveActivity(packageManager) != null) startActivity(sendIntent)
        }
        activity.buttonBack.setOnClickListener {
            finishAffinity()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        activity.buttonExit.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
//        finish()
//        exitProcess(0)
        finishAffinity()
    }
}
