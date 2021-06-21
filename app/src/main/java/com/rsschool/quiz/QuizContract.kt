package com.rsschool.quiz

import android.provider.BaseColumns

object QuizContract {
    object QuestionTable : BaseColumns {
        const val TABLE_NAME = "quiz_question"
        const val COLUMN_QUESTION = "question"
        const val COLUMN_OPTION1 = "option1"
        const val COLUMN_OPTION2 = "option2"
        const val COLUMN_OPTION3 = "option3"
        const val COLUMN_OPTION4 = "option4"
        const val COLUMN_OPTION5 = "option5"
        const val COLUMN_ANSWER_NR = "answer_nr"
    }
}