package com.example.dailytasks.boy

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dailytasks.DailyTasksModelFactory
import com.example.dailytasks.DailyTasksViewModel
import com.example.dailytasks.R
import com.example.dailytasks.databinding.FragmentBoyHistoryBinding
import com.example.dailytasks.room.BoyResultsTable
import com.example.dailytasks.room.BoyTasksTable
import com.example.dailytasks.room.DailyTasksRepository
import com.example.dailytasks.room.DailyTasksRoomDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.*

const val BFDATE = "date"

class BoyFragmentHistory : Fragment(R.layout.fragment_boy_history) {

    private var fragmentBinding: FragmentBoyHistoryBinding? = null
    private lateinit var dateID: String

    private val database by lazy { DailyTasksRoomDatabase.getDatabase(requireContext()) }
    private val repository by lazy { DailyTasksRepository(
        database.boyTasksDao(), database.boyResultsDao(), database.boyTasksListDao(),
        database.girlTasksDao(), database.girlResultsDao(), database.girlTasksListDao())
    }

    private val dailyTasksViewModel: DailyTasksViewModel by viewModels {
        DailyTasksModelFactory(repository)
    }

    private val checkBoxes = arrayListOf<CheckBox>()
    private val checkboxID = arrayListOf<Long>()
    private val resultID = arrayListOf<Long>()

    companion object {
        const val DATE = "bundleDate"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            dateID = it.getString(DATE).toString()
        }
    }

    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentBoyHistoryBinding.bind(view)
        fragmentBinding = binding

        val linearLayout = binding.linearLayout

        (activity as AppCompatActivity).supportActionBar?.title = "Просмотр выполненных задач - Boy"

        if (savedInstanceState != null) {
            binding.date.text = savedInstanceState.getCharSequence(BFDATE)
        }

        binding.date.text = dateID

        fun setTextViewStyle(view: TextView) {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(36, 16, 36, 16)
            with(view) {
                setTextAppearance(R.style.textview_style)
                setBackgroundColor(resources.getColor(R.color.secondaryColor))
                setPadding(36, 14, 24, 14)
                isClickable = true
                view.layoutParams = params
            }
        }

        fun setCheckBoxStyle(view: CheckBox) {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(66, 16, 136, 16)
            with(view) {
                setTextAppearance(R.style.checkbox_style)
                setPadding(54, 14, 84, 14)
                layoutDirection = View.LAYOUT_DIRECTION_RTL
                view.layoutParams = params
            }
        }

        fun dayResult(): String {
            var checkedBoxes = 0.0
            for (checkBox in checkBoxes) {
                if (checkBox.isChecked) {
                    checkedBoxes++
                }
            }

            return when (checkedBoxes / checkBoxes.size) {
                in 0.8..1.0 -> "Excellent"
                in 0.6..0.8 -> "Good"
                else -> "Normal"
            }
        }

        fun rating(): Float {
            var checkedBoxes = 0f
            for (checkBox in checkBoxes) {
                if (checkBox.isChecked) {
                    checkedBoxes++
                }
            }

            fun Float.roundTo(n : Int) : Float {
                return "%.${n}f".format(Locale.ENGLISH,this).toFloat()
            }

            return (checkedBoxes / checkBoxes.size * 5).roundTo(1)
        }

        //DRAWING THE LAYOUT
        dailyTasksViewModel.sortBoyTasksTableByDate(dateID)?.observe(viewLifecycleOwner) { sortedList ->
            for (task in sortedList) {
                if (task.viewType == "TextView") {
                    val textView = TextView(context)
                    textView.text = task.viewText
                    setTextViewStyle(textView)
                    linearLayout.addView(textView)
                } else if (task.viewType == "CheckBox") {
                    val checkBox = CheckBox(context)
                    checkBox.text = task.viewText
                    checkBox.isChecked = task.checkBoxStatus
                    setCheckBoxStyle(checkBox)
                    linearLayout.addView(checkBox)
                    checkboxID.add(task.id)
                    checkBoxes.add(checkBox)
                }
            }
        }

        dailyTasksViewModel.sortBoyResultByDate(dateID)?.observe(viewLifecycleOwner) { resultsDB ->
            for (i in resultsDB)
                resultID.add(i.id)
            }

        //UPDATE
        binding.updateButton.setOnClickListener {
            for (i in 0 until checkBoxes.size) {
                dailyTasksViewModel.updateBoyTasksTable(BoyTasksTable(
                    checkboxID[i],
                    checkBoxes[i].text.toString(),
                    dateID,
                    checkBoxes[i].isChecked,
                    "CheckBox")
                )
            }
            dailyTasksViewModel.updateBoyResult(BoyResultsTable(resultID[0], dateID, dayResult(), rating()))
            findNavController().navigate(R.id.action_boyFragmentHistory_to_boyFragmentGeneral)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(BFDATE, fragmentBinding?.date?.text)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
        Log.d("history", "onDestroyView() is called")
    }
}