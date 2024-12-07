package com.ssafy.smartstore_jetpack.ui.home

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.base.ApplicationClass
import com.ssafy.smartstore_jetpack.data.local.SharedPreferencesUtil
import com.ssafy.smartstore_jetpack.data.model.dto.CartOnly
import com.ssafy.smartstore_jetpack.data.model.dto.Combination
import com.ssafy.smartstore_jetpack.data.remote.RetrofitUtil
import com.ssafy.smartstore_jetpack.databinding.FragmentClickCombi2Binding
import com.ssafy.smartstore_jetpack.ui.MainActivity
import com.ssafy.smartstore_jetpack.ui.MainActivityViewModel
import com.ssafy.smartstore_jetpack.ui.ProductServiceObject
import com.ssafy.smartstore_jetpack.ui.order.ShoppingListFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private const val TAG = "ClickCombiFragment_싸피"
class ClickCombiFragment : Fragment() {

    private lateinit var combi: Combination
    private  var type: Int = 0
    private var _binding: FragmentClickCombi2Binding? = null
    private val binding get() = _binding!!
    private val activityViewModel: MainActivityViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getInt("type",0)
            combi = it.getParcelable("clickedCombi")!!
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentClickCombi2Binding.inflate(inflater, container, false)

        val user = ApplicationClass.sharedPreferencesUtil.getUser()
        val userName = user.name
        binding.titlePage.text = "${userName}님을 위한 추천 조합!"

        binding.mainlayout.menuImage
        if (type==1){
            binding.titlePage.text = "맛있게 먹은 그 레시피!"
        }

        ProductServiceObject.findProduct(combi.main) { product ->
            binding.mainlayout.textShoppingMenuName.text = product.name
            binding.mainlayout.textShoppingMenuMoney.text = "${product.price} 원"
            binding.mainlayout.textShoppingNum.text = product.type

            val resId = requireActivity().resources.getIdentifier(
                product!!.img.replace(".png", ""),
                "drawable", requireActivity().packageName
            )
            Glide.with(this)
                .load(resId)
                .into(binding.mainlayout.menuImage)

            binding.mainlayout.root.setOnClickListener {
                (activity as MainActivity ).openDetailFragment("detailMain",product)
            }
        }

        ProductServiceObject.findProduct(combi.sideDish1) { product ->

            binding.side1layout.textShoppingMenuName.text = product.name
            binding.side1layout.textShoppingMenuMoney.text = "${product.price} 원"
            binding.side1layout.textShoppingNum.text = product.type

            val resId = requireActivity().resources.getIdentifier(
                product!!.img.replace(".png", ""),
                "drawable", requireActivity().packageName
            )
            // Glide로 이미지 로드
            Glide.with(this)
                .load(resId)
                .into(binding.side1layout.menuImage)

            binding.side1layout.root.setOnClickListener {
                (activity as MainActivity ).openDetailFragment("detailMain",product)
            }
        }

        ProductServiceObject.findProduct(combi.sideDish2) { product ->

            binding.side2layout.textShoppingMenuName.text = product.name
            binding.side2layout.textShoppingMenuMoney.text = "${product.price} 원"
            binding.side2layout.textShoppingNum.text = product.type

            val resId = requireActivity().resources.getIdentifier(
                product!!.img.replace(".png", ""),
                "drawable", requireActivity().packageName
            )
            // Glide로 이미지 로드
            Glide.with(this)
                .load(resId)
                .into(binding.side2layout.menuImage)

            binding.side2layout.root.setOnClickListener {
                (activity as MainActivity ).openDetailFragment("detailMain",product)
            }

        }

        ProductServiceObject.findProduct(combi.soup) { product ->
            binding.souplayout.textShoppingMenuName.text = product.name
            binding.souplayout.textShoppingMenuMoney.text = "${product.price} 원"
            binding.souplayout.textShoppingNum.text = product.type

            val resId = requireActivity().resources.getIdentifier(
                product!!.img.replace(".png", ""),
                "drawable", requireActivity().packageName
            )

            Glide.with(this)
                .load(resId)
                .into(binding.souplayout.menuImage)

            binding.souplayout.root.setOnClickListener {
                (activity as MainActivity ).openDetailFragment("detailMain",product)
            }

        }

        binding.tvTotalPrice.text = "총 ${combi.dosirackPrice} 원"

        binding.gotocartBtn.setOnClickListener {
            if (combi.userId != SharedPreferencesUtil(requireContext()).getUser().id){
                combi.userId  = SharedPreferencesUtil(requireContext()).getUser().id
                combi.dosirockId = -1
                makeMyCombi(combi)

            }else{
                insertCart()
            }

            // combi 객체를 Bundle에 담기
            val bundle = Bundle()
            bundle.putParcelable("combi", combi)
            activityViewModel.completedCombi.add(combi)

            val cartFragment = ShoppingListFragment()
            cartFragment.arguments = bundle  // 전달한 데이터를 CartFragment로 설정

            parentFragmentManager.beginTransaction()
                .replace(R.id.frame_layout_main, cartFragment)
                .commit()

            val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
            bottomNavigationView.selectedItemId = R.id.navigation_page_3

            val backStackCount = parentFragmentManager.backStackEntryCount
            Log.d("BackStack", "BackStack Count: $backStackCount")
        }
        return binding.root
    }


    private fun insertCart() {
        val cartItem = CartOnly(-1 ,combi.dosirockId,1,SharedPreferencesUtil(requireContext()).getUser().id)
        CoroutineScope(Dispatchers.Main).launch {
            runCatching {
                RetrofitUtil.cartService.addCartItem(cartItem)
            }.onSuccess {
                Log.d(TAG, "insertCart: 장바구니 조합 담기 성공!")
            }.onFailure {
            }
        }
    }

    private fun makeMyCombi(combo:Combination) {
        CoroutineScope(Dispatchers.Main).launch {
            runCatching {
                RetrofitUtil.combiService.insertCombi(combi)
            }.onSuccess {
                combi.dosirockId = it
                Log.d(TAG, "makeMyCombi: 새로운 콤비아이디 $it")
                insertCart()
            }.onFailure {

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