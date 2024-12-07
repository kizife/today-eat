package com.ssafy.smartstore_jetpack.ui.category

import ReviewAdapter
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.data.model.dto.CommentTodayeat
import com.ssafy.smartstore_jetpack.data.model.dto.ProductTodayeat
import com.ssafy.smartstore_jetpack.databinding.FragmentProductReviewBinding // 뷰 바인딩 임포트
import com.ssafy.smartstore_jetpack.ui.MainActivity
import com.ssafy.smartstore_jetpack.ui.category.ProductDetail.Companion.ARG_PRODUCT
import kotlinx.coroutines.launch

private const val TAG = "ProductReview_싸피"

class ProductReview : Fragment() {
    private var commentList: MutableList<CommentTodayeat> = mutableListOf()
    private var productId: Int = 0
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var reviewViewModel: ReviewViewModel
    private var product: ProductTodayeat? = null
    private var _binding: FragmentProductReviewBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
          product = it.getParcelable(ARG_PRODUCT)
            productId = product!!.productId
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                (activity as MainActivity).openDetailFragment("productDetail", product!!)
            }
        })
        _binding = FragmentProductReviewBinding.inflate(inflater, container, false)


        reviewViewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)

        reviewAdapter = ReviewAdapter(commentList, requireActivity(), reviewViewModel)

        binding.reviewList.layoutManager = LinearLayoutManager(context)
        binding.reviewList.adapter = reviewAdapter

        reviewViewModel.comments.observe(viewLifecycleOwner) { comments ->
            reviewAdapter.updateData(comments)
        }

        reviewViewModel.loadComments(productId)
        Log.d(TAG, "onCreateView: $productId")

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_PRODUCT_ID = "productId"

        @JvmStatic
        fun newInstance(productId: Int): ProductReview {
            val fragment = ProductReview()
            val args = Bundle()
            args.putInt(ARG_PRODUCT_ID, productId)  // productId를 전달
            fragment.arguments = args
            return fragment
        }
    }


}
