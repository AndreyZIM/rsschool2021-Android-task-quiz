package com.rsschool.quiz

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.rsschool.quiz.databinding.FragmentQuizBinding

class FragmentQuiz : Fragment() {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!
    private var communicator: Communicator? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is Communicator) communicator = context
    }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        val questionNumber = arguments?.getInt(QUESTION_NUMBER_KEY)
        val currentOption = arguments?.getInt(CURRENT_OPTION_KEY)
        binding.nextButton.isEnabled = false

        showQuestion(questionNumber, currentOption)

        binding.radioGroup.setOnCheckedChangeListener { _, _ ->
            binding.nextButton.isEnabled = true
        }
        binding.nextButton.setOnClickListener {
            communicator?.openNextQuestion(getCheckedOption())
        }
        binding.previousButton.setOnClickListener {
            communicator?.openPreviousQuestion(getCheckedOption())
        }
        binding.toolbar.setNavigationOnClickListener {
            communicator?.openPreviousQuestion(getCheckedOption())
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (questionNumber == 0) activity?.finish()
                else communicator?.openPreviousQuestion(getCheckedOption())
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        return binding.root
    }

    override fun onDetach() {
        super.onDetach()
        communicator = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCheckedOption(): Int {
        val optionList = listOf(
            binding.optionOne,
            binding.optionTwo,
            binding.optionThree,
            binding.optionFour,
            binding.optionFive
        )
        var checkedOptionIndex = -1
        for (i in optionList.indices)
            if (optionList[i].isChecked) checkedOptionIndex = i
        return checkedOptionIndex
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.P)
    private fun showQuestion(questionNumber: Int?, currentOption: Int?) {
        val dbHelper = QuizDbHelper(context)
        val questionList = dbHelper.getAllQuestions()
        val currentQuestion = questionNumber?.let { questionList[it] }
        binding.question.text = currentQuestion?.getQuestion()

        val optionList = listOf(
            binding.optionOne,
            binding.optionTwo,
            binding.optionThree,
            binding.optionFour,
            binding.optionFive
        )
        val optionTextList = listOf(
            currentQuestion?.getOption1(),
            currentQuestion?.getOption2(),
            currentQuestion?.getOption3(),
            currentQuestion?.getOption4(),
            currentQuestion?.getOption5()
        )

        for (i in optionList.indices) {
            optionList[i].text = optionTextList[i]
            if (currentOption == i) {
                optionList[i].isChecked = true
                binding.nextButton.isEnabled = true
            }
        }

        if (questionNumber == 0) {
            binding.previousButton.isEnabled = false
            binding.toolbar.navigationIcon = null
        }
        if (questionNumber != null) {
            binding.toolbar.title = "Question ${questionNumber + 1}"
        }
        if (questionNumber == 4) binding.nextButton.text = "Submit"
    }

    companion object {
        fun newInstance(questionNumber: Int, currentOption: Int): FragmentQuiz {
            val fragment = FragmentQuiz()
            fragment.arguments = bundleOf(
                QUESTION_NUMBER_KEY to questionNumber,
                CURRENT_OPTION_KEY to currentOption
            )
            return fragment
        }

        const val QUESTION_NUMBER_KEY = "QUESTION_NUMBER"
        const val CURRENT_OPTION_KEY = "CURRENT_OPTION"
    }
}