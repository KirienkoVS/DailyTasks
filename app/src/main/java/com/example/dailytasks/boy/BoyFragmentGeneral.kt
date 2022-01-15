package com.example.dailytasks.boy

import android.os.Bundle
import android.view.View
import android.widget.RatingBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dailytasks.DailyTasksModelFactory
import com.example.dailytasks.DailyTasksViewModel
import com.example.dailytasks.R
import com.example.dailytasks.Results
import com.example.dailytasks.databinding.FragmentBoyGeneralBinding
import com.example.dailytasks.room.DailyTasksRepository
import com.example.dailytasks.room.DailyTasksRoomDatabase
import java.text.SimpleDateFormat
import java.util.*

class BoyFragmentGeneral : Fragment(R.layout.fragment_boy_general) {

    private var fragmentBinding: FragmentBoyGeneralBinding? = null
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

        val binding = FragmentBoyGeneralBinding.bind(view)
        fragmentBinding = binding

        (activity as AppCompatActivity).supportActionBar?.title = "Список результатов по дням - Boy"

        dailyTasksViewModel.getBoyTasks.observe(viewLifecycleOwner) { tasksDB ->
            if (tasksDB.isEmpty())
                binding.attention.visibility = View.VISIBLE
        }

        val day = SimpleDateFormat("EE", Locale.getDefault())
        val date = SimpleDateFormat("dd MMMM yyyy 'г.'", Locale.getDefault())

        dailyTasksViewModel.getBoyResults.observe(viewLifecycleOwner) { resultsDB ->
            val resultsList = mutableListOf<Results>()
            for (i in resultsDB) {
                val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(i.date)
                val shortDate = day.format(formatter!!).uppercase(Locale.getDefault())
                val fullDate = date.format(formatter)
                val invisibleDate = i.date

                val emoji: Int = when(i.result) {
                    "Excellent" -> R.drawable.cool
                    "Good" -> R.drawable.smile
                    else -> R.drawable.surprised
                }

                val ratingBar = RatingBar(context)
                val rating = i.rating
                ratingBar.rating = rating

                resultsList.add(Results(emoji, fullDate, shortDate, invisibleDate, ratingBar, rating))
            }
            val adapter = BoyRecyclerAdapter(resultsList)
            binding.recyclerView.adapter = adapter
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_boyFragmentGeneral_to_boyFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        fragmentBinding = null
    }
}