package com.ssafy.smartstore_jetpack.ui.category

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.data.local.SharedPreferencesUtil
import com.ssafy.smartstore_jetpack.data.model.dto.CommentTodayeat
import com.ssafy.smartstore_jetpack.data.model.dto.ProductTodayeat
import com.ssafy.smartstore_jetpack.data.model.dto.ShoppingCartTodayeat
import com.ssafy.smartstore_jetpack.databinding.FragmentDetailmainBinding
import com.ssafy.smartstore_jetpack.ui.MainActivity
import com.ssafy.smartstore_jetpack.ui.MainActivityViewModel


private const val TAG = "DetailMain_싸피"

class DetailMain : Fragment() {

    private var _binding: FragmentDetailmainBinding? = null
    private val binding get() = _binding!!
    private lateinit var reviewViewModel: ReviewViewModel

    private lateinit var product: ProductTodayeat
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            product = it.getParcelable(ARG_PRODUCT)!!  // 안전하게 받아오기
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        reviewViewModel = ViewModelProvider(this).get(ReviewViewModel::class.java)
        _binding = FragmentDetailmainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.selectedItemId = R.id.blank

        addReview()

        // product가 null인 경우 예외 처리
        product?.let { product ->
            // 첫 화면에 ProductDetail 프래그먼트를 표시
            if (savedInstanceState == null) {
                binding.productDetailBtn.isSelected = true
                binding.productReviewBtn.isSelected = false
                (activity as MainActivity ).openDetailFragment("productDetail",product)
            }

            binding.productReviewBtn.setOnClickListener {
                binding.productDetailBtn.isSelected = false
                binding.productReviewBtn.isSelected = true
                binding.reviewBtn.visibility = View.VISIBLE

                (activity as MainActivity ).openDetailFragment("productReview",product)

            }

            binding.productDetailBtn.setOnClickListener {
                binding.productDetailBtn.isSelected = true
                binding.productReviewBtn.isSelected = false
                binding.reviewBtn.visibility = View.GONE
                (activity as MainActivity ).openDetailFragment("productDetail",product)
            }
        }

        binding.orderBtn.setOnClickListener {
            product?.let { product ->
                val shoppingCartItem = ShoppingCartTodayeat(
                    productId = product.productId,
                    productImg = product.img,
                    productName = product.name,
                    productCnt = 1,  // 기본 수량 1로 설정
                    productPrice = product.price,
                    type = product.type
                )

                // ViewModel을 통해 장바구니에 추가
                activityViewModel.addShoppingList(shoppingCartItem)
                 Toast.makeText(requireContext(), "장바구니에 담겼습니다.", Toast.LENGTH_SHORT).show()
            }

            if(activityViewModel.sideDish.size == 1){
                Toast.makeText(requireContext(), "사이드 메뉴를 하나더 추가해 주세요.", Toast.LENGTH_SHORT).show()
                (activity as MainActivity).opneFragmentBanner("Side")
            }else{
                (activity as MainActivity).opneFragmentBanner("Category")
            }


        }
    }

    fun addReview() {
        binding.reviewBtn.setOnClickListener {
            // 다이얼로그의 View 설정
            val dialogView = LayoutInflater.from(binding.root.context)
                .inflate(R.layout.dialog_write_review, null)

            val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
            val reviewEditText = dialogView.findViewById<EditText>(R.id.reviewEditText)
            val registerButton = dialogView.findViewById<Button>(R.id.resister_button) // 등록 버튼
            val cancelButton = dialogView.findViewById<Button>(R.id.cancel_button) // 취소 버튼

            // 다이얼로그 생성
            val dialog = AlertDialog.Builder(binding.root.context)
                .setView(dialogView)
                .create()

            dialog.window?.apply {
                setBackgroundDrawableResource(R.drawable.rounded_dialog_background) // 배경 둥글게 설정
                val params = attributes
                params.width =
                    (resources.displayMetrics.widthPixels * 0.6).toInt() // 화면 너비의 60%로 설정
                params.height = WindowManager.LayoutParams.WRAP_CONTENT // 높이는 콘텐츠에 맞게 설정
                attributes = params
            }

            // 등록 버튼 클릭 이벤트 처리
            registerButton.setOnClickListener {
                val rating = ratingBar.rating
                val review = reviewEditText.text.toString()

                // ViewModel에 데이터 전달 (혹은 서버에 POST 요청)
                reviewViewModel.addComment(
                    CommentTodayeat(
                        commentId = -1,
                        userId = SharedPreferencesUtil(binding.root.context).getUser().id,
                        productId = product!!.productId,
                        rating = rating.toInt(),
                        comment = review
                    )
                )

                (activity as MainActivity).openDetailFragment("productReview", product)
                // 다이얼로그 닫기
                dialog.dismiss()
            }

            // 취소 버튼 클릭 이벤트 처리
            cancelButton.setOnClickListener {
                dialog.dismiss() // 다이얼로그 닫기
            }

            // 다이얼로그 표시
            dialog.show()
        }
    }



    override fun onResume() {
        super.onResume()
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.GONE
        activity?.findViewById<View>(R.id.category_fab)?.visibility = View.GONE
    }

    override fun onPause() {
        super.onPause()
        activity?.findViewById<View>(R.id.bottom_navigation)?.visibility = View.VISIBLE
        activity?.findViewById<View>(R.id.category_fab)?.visibility = View.VISIBLE
    }

    companion object {
        private const val ARG_PRODUCT = "product"
    }
}
