package com.ssafy.smartstore_jetpack.ui.order

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.base.ApplicationClass
import com.ssafy.smartstore_jetpack.base.BaseFragment
import com.ssafy.smartstore_jetpack.data.local.SharedPreferencesUtil
import com.ssafy.smartstore_jetpack.data.model.dto.Combination
import com.ssafy.smartstore_jetpack.data.remote.ProductService
import com.ssafy.smartstore_jetpack.databinding.FragmentDosirockBinding
import com.ssafy.smartstore_jetpack.ui.MainActivity
import kotlinx.coroutines.launch

private const val TAG = "ShoppingDetailFragment_싸피"

class ShoppingDetailFragment : BaseFragment<FragmentDosirockBinding>(
    FragmentDosirockBinding::bind,
    R.layout.fragment_dosirock
) {

    private lateinit var combination: Combination
    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            combination = it.getParcelable("combination")
                ?: throw IllegalArgumentException("Combination is required")
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dosirockTitle.text =
            "${SharedPreferencesUtil(requireContext()).getUser().name}님의 도시락"

        Log.d(TAG, "onViewCreated: 도시락번호!!!!! ${combination.dosirockId}")

        fetchProductDetails(
            combination.main,
            combination.sideDish1,
            combination.sideDish2,
            combination.soup
        )

        binding.btnOrder.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout_main, ShoppingListFragment()).commit()
        }
    }

  private fun fetchProductDetails(mainId: Int, side1Id: Int, side2Id: Int, soupId: Int) {
        lifecycleScope.launch {
            try {
                val productService = ApplicationClass.retrofit.create(ProductService::class.java)
                val main = productService.getProduct(mainId)
                val side1 = productService.getProduct(side1Id)
                val side2 = productService.getProduct(side2Id)
                val soup = productService.getProduct(soupId)

                binding.mainItemName.text = main.name
                binding.mainItemPrice.text = "${main.price}원"
                binding.side1ItemName.text = side1.name
                binding.side1ItemPrice.text = "${side1.price}원"
                binding.side2ItemName.text = side2.name
                binding.side2ItemPrice.text = "${side2.price}원"
                binding.soupItemName.text = soup.name
                binding.soupItemPrice.text = "${soup.price}원"

                val mainImg = main.img.removeSuffix(".png")
                val side1Img = side1.img.removeSuffix(".png")
                val side2Img = side2.img.removeSuffix(".png")
                val soupImg = soup.img.removeSuffix(".png")
                val mainImgResId =
                    resources.getIdentifier(mainImg, "drawable", requireContext().packageName)
                val side1ImgResId =
                    resources.getIdentifier(side1Img, "drawable", requireContext().packageName)
                val side2ImgResId =
                    resources.getIdentifier(side2Img, "drawable", requireContext().packageName)
                val soupImgResId =
                    resources.getIdentifier(soupImg, "drawable", requireContext().packageName)

                Glide.with(requireContext())
                    .load(mainImgResId)
                    .placeholder(R.drawable.loading_animation)
                    .into(binding.mainMenuImage)

                Glide.with(requireContext())
                    .load(side1ImgResId)
                    .placeholder(R.drawable.loading_animation)
                    .into(binding.side1MenuImage)

                Glide.with(requireContext())
                    .load(side2ImgResId)
                    .placeholder(R.drawable.loading_animation)
                    .into(binding.side2MenuImage)

                Glide.with(requireContext())
                    .load(soupImgResId)
                    .placeholder(R.drawable.loading_animation)
                    .into(binding.soupMenuImage)

            } catch (e: Exception) {

            }
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


}



