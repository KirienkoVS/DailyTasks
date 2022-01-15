package com.example.dailytasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.dailytasks.databinding.FragmentDailyTasksBinding
import com.example.dailytasks.room.DailyTasksRepository
import com.example.dailytasks.room.DailyTasksRoomDatabase

class DailyTasksFragment : Fragment() {

    private var _binding: FragmentDailyTasksBinding? = null
    private val binding get() = _binding!!

    private val dailyTasksViewModel: DailyTasksViewModel by viewModels {
        DailyTasksModelFactory(repository)
    }
    private val database by lazy { DailyTasksRoomDatabase.getDatabase(requireContext()) }
    private val repository by lazy { DailyTasksRepository(
        database.boyTasksDao(), database.boyResultsDao(), database.boyTasksListDao(),
        database.girlTasksDao(), database.girlResultsDao(), database.girlTasksListDao())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDailyTasksBinding.inflate(inflater, container, false)

        (activity as AppCompatActivity).supportActionBar?.title = "Главный экран"

        dailyTasksViewModel.getBoyTasksList.observe(viewLifecycleOwner) { tasksList ->
            if (tasksList.isEmpty()) {
                binding.createBoyTasksListButton.visibility = View.VISIBLE
            } else binding.boyImage.setOnClickListener {
                findNavController().navigate(R.id.action_dailyTasksFragment_to_boyFragmentGeneral)
            }
        }

        binding.createBoyTasksListButton.setOnClickListener {
            findNavController().navigate(R.id.action_dailyTasksFragment_to_tasksFragment)
        }

        binding.boyImage.setOnLongClickListener {
            findNavController().navigate(R.id.action_dailyTasksFragment_to_tasksFragment)
            true
        }

        //////////////////////////////////////////////////////////////////////////////////////////
        dailyTasksViewModel.getGirlTasksList.observe(viewLifecycleOwner) { tasksList ->
            if (tasksList.isEmpty()) {
                binding.createGirlTasksListButton.visibility = View.VISIBLE
            } else binding.girlImage.setOnClickListener {
                findNavController().navigate(R.id.action_dailyTasksFragment_to_girlFragmentGeneral)
            }
        }

        binding.createGirlTasksListButton.setOnClickListener {
            findNavController().navigate(R.id.action_dailyTasksFragment_to_girlTasksFragment)
        }

        binding.girlImage.setOnLongClickListener {
            findNavController().navigate(R.id.action_dailyTasksFragment_to_girlTasksFragment)
            true
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}