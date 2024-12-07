package com.ssafy.smartstore_jetpack.ui.category

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.databinding.FragmentCategoryBinding
import com.ssafy.smartstore_jetpack.ui.MainActivity
import com.ssafy.smartstore_jetpack.ui.MainActivityViewModel
import com.ssafy.smartstore_jetpack.ui.ai.CategoryRecommend

private const val TAG = "Category_싸피"
class Category : Fragment() {

    private var _binding: FragmentCategoryBinding? = null
    private val binding get() = _binding!!

    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as MainActivity).opneFragmentBanner("Home")
            }
        })

        (activity as MainActivity).updateBottomNavigationSelection(R.id.blank)
        _binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.cardViewMain.setOnClickListener {
            Log.d(TAG, "onViewCreated: main 눌림 ")
            (activity as MainActivity).opneFragmentBanner("Main")
        }

        binding.cardViewSide.setOnClickListener {
            Log.d(TAG, "onViewCreated: side 눌림 ")
            (activity as MainActivity).opneFragmentBanner("Side")
        }

        binding.cardViewSoup.setOnClickListener {
            Log.d(TAG, "onViewCreated: soup 눌림 ")
            (activity as MainActivity).opneFragmentBanner("Soup")
        }

        binding.cardViewRecommend.setOnClickListener {
            Log.d(TAG, "onViewCreated: recommend 눌림 ")
            (activity as MainActivity).opneFragmentBanner("CategoryRecommend")
        }
        changeCardView()
    }

    fun changeCardView(){

        if (activityViewModel.mainDish.size >= 1){
            binding.cardViewMain.setImageResource(R.drawable.catemain2)
            binding.cardViewMain.isEnabled = false
            binding.cardViewMain.invalidate()
        }

        if (activityViewModel.sideDish.size == 1) {
            binding.cardViewSide.invalidate()
        }else if (activityViewModel.sideDish.size == 2) {
            binding.cardViewSide.setImageResource(R.drawable.cateside2)
            binding.cardViewSide.isEnabled = false
            binding.cardViewSide.invalidate()
        }

        if (activityViewModel.soupDish.size >=1){
            binding.cardViewSoup.setImageResource(R.drawable.catesoup2)
            binding.cardViewSoup.isEnabled = false
            binding.cardViewSoup.invalidate()
        }
    }
}
