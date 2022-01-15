
package com.example.dailytasks.boy

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.CheckBox
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dailytasks.DailyTasksModelFactory
import com.example.dailytasks.DailyTasksViewModel
import com.example.dailytasks.R
import com.example.dailytasks.databinding.FragmentBoyBinding
import com.example.dailytasks.room.BoyResultsTable
import com.example.dailytasks.room.BoyTasksTable
import com.example.dailytasks.room.DailyTasksRepository
import com.example.dailytasks.room.DailyTasksRoomDatabase
import kotlinx.coroutines.DelicateCoroutinesApi
import java.text.SimpleDateFormat
import java.util.*

const val DATE = "date"
const val TAG = "BoyFragment"

class BoyFragment : Fragment(R.layout.fragment_boy) {

    private var fragmentBoyBinding: FragmentBoyBinding? = null
    private val dailyTasksViewModel: DailyTasksViewModel by viewModels {
        DailyTasksModelFactory(repository)
    }
    private val database by lazy { DailyTasksRoomDatabase.getDatabase(requireContext()) }
    private val repository by lazy { DailyTasksRepository(
        database.boyTasksDao(), database.boyResultsDao(), database.boyTasksListDao(),
        database.girlTasksDao(), database.girlResultsDao(), database.girlTasksListDao())
    }

    private val checkBoxes = arrayListOf<CheckBox>()
    private val checkboxID = arrayListOf<Long>()

    @DelicateCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated called")

        val binding = FragmentBoyBinding.bind(view)
        fragmentBoyBinding = binding

        (activity as AppCompatActivity).supportActionBar?.title = "Сохранение выполненных задач - Boy"

        val date = binding.date
        val linearLayout = binding.linearLayout

        var taskID = 1L
        var resultID = 1L

        if (savedInstanceState != null) {
            date.text = savedInstanceState.getCharSequence(DATE)
        }

        val calendar = Calendar.getInstance()
        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            date.text = sdf.format(calendar.time)

            dailyTasksViewModel.getBoyTasksList.observe(viewLifecycleOwner) { taskList ->
                dailyTasksViewModel.sortBoyTasksTableByDate(date.text.toString())?.observe(viewLifecycleOwner) { sortedList ->
                    if (sortedList.isEmpty()) {
                        for (task in taskList) {
                            if (task.viewType == "TextView") {
                                val textView = TextView(context)
                                textView.text = task.categoryName
                                dailyTasksViewModel.insertBoyTasksTable(BoyTasksTable(
                                    taskID,
                                    textView.text.toString(),
                                    date.text.toString(),
                                    false,
                                    "TextView")
                                )
                                taskID++
                            } else if (task.viewType == "CheckBox") {
                                val checkBox = CheckBox(context)
                                checkBox.text = task.categoryName
                                dailyTasksViewModel.insertBoyTasksTable(BoyTasksTable(
                                    taskID,
                                    checkBox.text.toString(),
                                    date.text.toString(),
                                    false,
                                    "CheckBox")
                                )
                                taskID++
                            }
                        }
                    }
                }
            }
        }

        date.setOnClickListener {
            DatePickerDialog(requireContext(), dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        fun setTextViewStyle(view: TextView) {
            val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
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
            val params = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            params.setMargins(66, 16, 136, 16)
            with(view) {
                setTextAppearance(R.style.checkbox_style)
                setPadding(54, 14, 84, 14)
                layoutDirection = View.LAYOUT_DIRECTION_RTL
                view.layoutParams = params
            }
        }

        //DRAWING THE LAYOUT
        dailyTasksViewModel.getBoyTasksList.observe(viewLifecycleOwner) { taskList ->
            for (task in taskList) {
                if (task.viewType == "TextView") {
                    val textView = TextView(context)
                    textView.text = task.categoryName
                    setTextViewStyle(textView)
                    linearLayout.addView(textView)
                } else if (task.viewType == "CheckBox") {
                    val checkBox = CheckBox(context)
                    checkBox.text = task.categoryName
                    setCheckBoxStyle(checkBox)
                    linearLayout.addView(checkBox)
                    checkboxID.add(task.number)
                    checkBoxes.add(checkBox)
                }
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

        fun alertDialog() {
            //Custom Title
            val inflater = LayoutInflater.from(context)
            val topDialog = inflater.inflate(R.layout.dialog_top, null)
            val mediumDialog = inflater.inflate(R.layout.dialog_medium, null)
            val lowDialog = inflater.inflate(R.layout.dialog_low, null)
            val builder = AlertDialog.Builder(requireContext())

            when (dayResult()) {
                "Excellent" -> builder.setCustomTitle(topDialog)
                "Good" -> builder.setCustomTitle(mediumDialog)
                "Normal" -> builder.setCustomTitle(lowDialog)
            }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        dailyTasksViewModel.getBoyResults.observe(viewLifecycleOwner) { resultDB ->
            val idS = mutableListOf<Long>()
            if (resultDB.isEmpty()) {
                idS.clear()
            } else {
                for (i in resultDB) {
                    idS.add(i.id)
                }
                idS.sort()
                resultID = if (idS.isEmpty()) {
                    1
                } else idS.last() + 1

            }
        }

        dailyTasksViewModel.getBoyTasks.observe(viewLifecycleOwner) { tasksDB ->
            val idS = mutableListOf<Long>()
            if (tasksDB.isEmpty()) {
                idS.clear()
            } else {
                for (i in tasksDB) {
                    idS.add(i.id)
                }
                idS.sort()
                taskID = if (idS.isEmpty()) {
                    1
                } else idS.last() + 1
            }
        }

        fun saveResults() {
            dailyTasksViewModel.insertBoyResult(BoyResultsTable(
                resultID,
                date.text.toString(),
                dayResult(),
                rating()))
            resultID++
        }

        fun saveTasks() {
            dailyTasksViewModel.sortBoyTasksTableByViewTypeAndDate(date.text.toString(), "CheckBox")?.observe(viewLifecycleOwner) { list ->
                val id = arrayListOf<Long>()
                for (i in list)
                    id.add(i.id)

                for (i in 0 until checkBoxes.size) {
                    dailyTasksViewModel.updateBoyTasksTable(BoyTasksTable(
                        id[i],
                        checkBoxes[i].text.toString(),
                        date.text.toString(),
                        checkBoxes[i].isChecked,
                        "CheckBox")
                    )
                }
            }
        }

        //SAVE BUTTON
        binding.saveButton.setOnClickListener {
            if (date.text == "") {
                Toast.makeText(activity, "Выберите дату", Toast.LENGTH_SHORT).show()
            } else
            dailyTasksViewModel.sortBoyResultByDate(date.text.toString())?.observe(viewLifecycleOwner) { resultDB ->
                if(resultDB.isEmpty()) {
                    saveResults()
                    saveTasks()
                    alertDialog()
                    findNavController().navigate(R.id.action_boyFragment_to_boyFragmentGeneral)
                } else Toast.makeText(activity, "Record already exists. Choose a different date.", Toast.LENGTH_SHORT).show()
            }
        }

        //DELETE BUTTON
        /*binding.deleteButton.setOnClickListener {
            dailyTasksViewModel.clearBoyTasksTable()
            taskID = 1
            Toast.makeText(activity, "CLEAR", Toast.LENGTH_SHORT).show()
            dailyTasksViewModel.deleteBoyResults()
            resultID = 1
        }*/
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(DATE, fragmentBoyBinding?.date?.text)
        Log.d(TAG, "onSaveInstanceState called")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBoyBinding = null
        Log.d(TAG, "onDestroyView called")
    }
}