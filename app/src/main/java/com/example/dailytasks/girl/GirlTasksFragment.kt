package com.example.dailytasks.girl

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.children
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dailytasks.DailyTasksModelFactory
import com.example.dailytasks.DailyTasksViewModel
import com.example.dailytasks.R
import com.example.dailytasks.databinding.FragmentTasksBinding
import com.example.dailytasks.room.DailyTasksRepository
import com.example.dailytasks.room.DailyTasksRoomDatabase
import com.example.dailytasks.room.GirlTasksList

class GirlTasksFragment : Fragment(R.layout.fragment_tasks) {

    private var tasksBinding: FragmentTasksBinding? = null

    private val dailyTasksViewModel: DailyTasksViewModel by viewModels {
        DailyTasksModelFactory(repository)
    }
    private val database by lazy { DailyTasksRoomDatabase.getDatabase(requireContext()) }
    private val repository by lazy { DailyTasksRepository(
        database.boyTasksDao(), database.boyResultsDao(), database.boyTasksListDao(),
        database.girlTasksDao(), database.girlResultsDao(), database.girlTasksListDao())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTasksBinding.bind(view)
        tasksBinding = binding

        (activity as AppCompatActivity).supportActionBar?.title = "Редактирование списка задач - Girl"

        val linearLayout = binding.tasksLinearLayout
        val addCategoryButton = binding.addCategoryButton
        val addSubcategoryButton = binding.addSubcategoryButton
        val deleteButton = binding.deleteButton
        val saveButton = binding.saveButton

        var categoryID: Long = 1

        dailyTasksViewModel.getGirlTasksList.observe(viewLifecycleOwner) { taskList ->
            val databaseIDs = mutableListOf<Long>()
            if (taskList.isEmpty()) {
                databaseIDs.clear()
            } else
                for (task in taskList) {
                    databaseIDs.add(task.number)
                }
            databaseIDs.sort()
            categoryID = if (databaseIDs.isEmpty()) {
                1
            } else databaseIDs.last() + 1
            Toast.makeText(activity, categoryID.toString(), Toast.LENGTH_SHORT).show()
        }

        deleteButton.setOnClickListener {
            dailyTasksViewModel.deleteGirlTasksList()
            linearLayout.removeAllViewsInLayout()
            categoryID = 1
        }

        saveButton.setOnClickListener {
            findNavController().navigate(R.id.action_girlTasksFragment_to_dailyTasksFragment)
        }

        fun setTextViewStyle(view: TextView) {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(36, 16, 36, 16)
            with(view) {
                setTextAppearance(R.style.textview_style)
                setBackgroundColor(resources.getColor(R.color.secondaryColor))
                setPadding(24, 14, 24, 14)
                isClickable = true
                view.layoutParams = params
            }
        }

        fun deleteCategory(textView: TextView) {
            for (i in linearLayout.children) {
                if (i == textView) {
                    linearLayout.removeView(i)
                }
            }
        }

        fun renameDialog(textView: TextView) {
            val builder = AlertDialog.Builder(context)
            val inflater: LayoutInflater = layoutInflater
            val dialogLayout: View = inflater.inflate(R.layout.rename_edit_text, null)
            val editText: EditText = dialogLayout.findViewById(R.id.rename_edit_text)

            dailyTasksViewModel.sortGirlTaskListByName(textView.text.toString())?.observe(viewLifecycleOwner) { taskInDB ->
                for (i in taskInDB) {
                    with(builder) {
                        setTitle("Rename")
                        setPositiveButton("OK") { _, _ ->
                            textView.text = editText.text
                            dailyTasksViewModel.updateGirlTaskList(GirlTasksList(i.number, editText.text.toString(), i.viewType))
                        }
                        setNegativeButton("CANCEL") { _, _ -> }
                        setView(dialogLayout)
                        show()
                    }
                }
            }
        }

        fun editCategoryDialog(textView: TextView) {
            val builder = AlertDialog.Builder(context)
                .setTitle("Edit")
                .setMessage("Choose action")
                .setPositiveButton("Delete") { _, _ ->
                    deleteCategory(textView)
                    dailyTasksViewModel.deleteGirlTask(textView.text.toString())
                    linearLayout.removeAllViews()
                }
                .setNegativeButton("Rename") { _, _ ->
                    renameDialog(textView)
                }

            val dialog: AlertDialog = builder.create()
            dialog.show()
        }

        fun addCategoryDialog() {
            val builder = AlertDialog.Builder(context)
            val inflater: LayoutInflater = layoutInflater
            val dialogLayout: View = inflater.inflate(R.layout.rename_edit_text, null)
            val editText: EditText = dialogLayout.findViewById(R.id.rename_edit_text)

            with(builder) {
                setTitle("Add category")
                setPositiveButton("OK") { _, _ ->
                    val textView = TextView(context)
                    textView.text = editText.text
                    setTextViewStyle(textView)
                    if (textView.text.isNotEmpty()) {
                        dailyTasksViewModel.insertGirlTaskList(GirlTasksList(
                            categoryID,
                            textView.text.toString(),
                            textView::class.simpleName!!)
                        )
                    }
                    categoryID++
                    addSubcategoryButton.visibility = View.VISIBLE
                    linearLayout.removeAllViews()
                }
                setNegativeButton("CANCEL") { _, _ -> }
                setView(dialogLayout)
                show()
            }
        }

        addCategoryButton.setOnClickListener {
            addCategoryDialog()
        }

        fun setCheckBoxStyle(view: CheckBox) {
            val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            params.setMargins(56, 16, 36, 16)
            with(view) {
                setTextAppearance(R.style.checkbox_style)
                setPadding(54, 14, 54, 14)
                isClickable = true
                setOnClickListener {
                    editCategoryDialog(it as CheckBox)
                }
                layoutDirection = View.LAYOUT_DIRECTION_RTL
                view.layoutParams = params
            }
        }

        fun addSubCategoryDialog() {
            val builder = AlertDialog.Builder(context)
            val inflater: LayoutInflater = layoutInflater
            val dialogLayout: View = inflater.inflate(R.layout.rename_edit_text, null)
            val editText: EditText = dialogLayout.findViewById(R.id.rename_edit_text)

            with(builder) {
                setTitle("Add subcategory")
                setPositiveButton("OK") { _, _ ->
                    val checkBox = CheckBox(context)
                    val text = "- " + editText.text
                    checkBox.text = text
                    checkBox.setOnClickListener {
                        editCategoryDialog(it as CheckBox)
                    }
                    setCheckBoxStyle(checkBox)
                    if (!checkBox.text.equals("- ")) {
                        dailyTasksViewModel.insertGirlTaskList(GirlTasksList(
                            categoryID,
                            checkBox.text.toString(),
                            checkBox::class.simpleName!!)
                        )
                    }
                    categoryID++
                    linearLayout.removeAllViews()
                }
                setNegativeButton("CANCEL") { _, _ -> }
                setView(dialogLayout)
                show()
            }
        }

        addSubcategoryButton.setOnClickListener {
            addSubCategoryDialog()
        }

        //DRAWING THE LAYOUT
        dailyTasksViewModel.getGirlTasksList.observe(viewLifecycleOwner) { tasksList ->
            for (task in tasksList) {
                if (task.viewType == "TextView") {
                    val textView = TextView(context)
                    textView.text = task.categoryName
                    setTextViewStyle(textView)
                    textView.setOnClickListener {
                        editCategoryDialog(it as TextView)
                    }
                    linearLayout.addView(textView)
                } else if (task.viewType == "CheckBox") {
                    val checkBox = CheckBox(context)
                    checkBox.text = task.categoryName
                    setCheckBoxStyle(checkBox)
                    linearLayout.addView(checkBox)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tasksBinding = null
    }
}