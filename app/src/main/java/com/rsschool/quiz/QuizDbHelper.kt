package com.rsschool.quiz

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import android.provider.BaseColumns
import androidx.annotation.RequiresApi


@RequiresApi(Build.VERSION_CODES.P)
class QuizDbHelper(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private lateinit var db: SQLiteDatabase

    override fun onCreate(db: SQLiteDatabase?) {
        if (db != null) {
            this.db = db
        }
        this.db.execSQL(SQL_CREATE_ENTRIES)
        fillQuestionTable()
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if (db != null) {
            this.db = db
        }
        this.db.execSQL(SQL_DELETE_ENTRIES)
        onCreate(this.db)
    }

    private fun fillQuestionTable() {
        val q1 = Question(
            "What was the 16th U.S. president's first name?",
            "George",
            "James",
            "William",
            "Abraham",
            "Putin",
            3
        )
        val q2 = Question(
            "What was Albert Einstein's I.Q.?",
            "Unknown",
            "210",
            "190",
            "102",
            "91",
            0
        )
        val q3 = Question(
            "What was the 34th U.S. president's first name?",
            "Franklin",
            "Calvin",
            "Dwight",
            "Harry",
            "Putin",
            2
        )
        val q4 = Question(
            "What is the correct spelling?",
            "Boiogle",
            "Boggal",
            "Boglle",
            "Boggle",
            "Bogdan",
            3
        )
        val q5 = Question(
            "What is another name for the North Star?",
            "Polaris",
            "Paiel",
            "Power",
            "Pollux",
            "Putin",
            0
        )
        addQuestion(q1)
        addQuestion(q2)
        addQuestion(q3)
        addQuestion(q4)
        addQuestion(q5)
    }

    private fun addQuestion(question: Question) {
        val cv = ContentValues()
        cv.put(QuizContract.QuestionTable.COLUMN_QUESTION, question.getQuestion())
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION1, question.getOption1())
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION2, question.getOption2())
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION3, question.getOption3())
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION4, question.getOption4())
        cv.put(QuizContract.QuestionTable.COLUMN_OPTION5, question.getOption5())
        cv.put(QuizContract.QuestionTable.COLUMN_ANSWER_NR, question.getAnswer())
        db.insert(QuizContract.QuestionTable.TABLE_NAME, null, cv)
    }

    companion object {
        const val DATABASE_NAME: String = "Quiz.db"
        const val DATABASE_VERSION: Int = 1
        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE ${QuizContract.QuestionTable.TABLE_NAME} (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "${QuizContract.QuestionTable.COLUMN_QUESTION} TEXT," +
                    "${QuizContract.QuestionTable.COLUMN_OPTION1} TEXT," +
                    "${QuizContract.QuestionTable.COLUMN_OPTION2} TEXT," +
                    "${QuizContract.QuestionTable.COLUMN_OPTION3} TEXT," +
                    "${QuizContract.QuestionTable.COLUMN_OPTION4} TEXT," +
                    "${QuizContract.QuestionTable.COLUMN_OPTION5} TEXT," +
                    "${QuizContract.QuestionTable.COLUMN_ANSWER_NR} TEXT)"
        private const val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS ${QuizContract.QuestionTable.TABLE_NAME}"
    }

    @SuppressLint("Recycle")
    fun getAllQuestions(): List<Question> {
        val questionList: MutableList<Question> = mutableListOf()
        db = readableDatabase
        val cursor: Cursor =
            db.rawQuery("SELECT * FROM " + QuizContract.QuestionTable.TABLE_NAME, null)

        if (cursor.moveToFirst())
            do {
                val question = Question("", "", "", "", "", "", 0)
                question.setQuestion(cursor.getString(cursor.getColumnIndex(QuizContract.QuestionTable.COLUMN_QUESTION)))
                question.setOption1(cursor.getString(cursor.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION1)))
                question.setOption2(cursor.getString(cursor.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION2)))
                question.setOption3(cursor.getString(cursor.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION3)))
                question.setOption4(cursor.getString(cursor.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION4)))
                question.setOption5(cursor.getString(cursor.getColumnIndex(QuizContract.QuestionTable.COLUMN_OPTION5)))
                question.setAnswer(cursor.getInt(cursor.getColumnIndex(QuizContract.QuestionTable.COLUMN_ANSWER_NR)))
                questionList.add(question)
            } while (cursor.moveToNext())

        cursor.close()
        return questionList
    }
}