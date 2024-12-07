import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.base.ApplicationClass
import com.ssafy.smartstore_jetpack.data.model.dto.Combination
import com.ssafy.smartstore_jetpack.data.model.dto.ShoppingCartTodayeat
import com.ssafy.smartstore_jetpack.data.remote.ProductService
import com.ssafy.smartstore_jetpack.data.remote.RetrofitUtil.Companion.productService
import com.ssafy.smartstore_jetpack.databinding.ListItemShoppingListBinding
import com.ssafy.smartstore_jetpack.ui.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "ShoppingCombiAdapter_싸피"

class ShoppingCombiAdapter(
//    var list: MutableList<Combination>,
    private val viewModel: MainActivityViewModel,
    private val itemClickListener: (Combination) -> Unit

) : RecyclerView.Adapter<ShoppingCombiAdapter.ShoppingCartViewHolder>() {


    inner class ShoppingCartViewHolder(private val binding: ListItemShoppingListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindInfo(item: Combination, position: Int) {

            if (position<viewModel.quantityList.size) {
                val quantity = viewModel.quantityList[position]
                binding.quantityTv.text = quantity.toString()
            } else {
                viewModel.quantityList.add(1)
                binding.quantityTv.text = "1"
            }

            Log.d(TAG, "bindInfo: 도시락 바인드 합니다.")


            Glide.with(itemView)
                .load("${ApplicationClass.MENU_IMGS_URL}${item.main}")  // 임의로 메인 메뉴 이미지 사용
                .into(binding.menuImage)

            //정보 가져오기
            getItemInfo(item, binding)



            binding.root.setOnClickListener {
                Log.d(TAG, "bindInfo: 아이템 클릭됨: $item")
                itemClickListener(item)
            }

            binding.addBtn.setOnClickListener {
                var q = binding.quantityTv.text.toString().toInt() + 1
                if (q > 15) {
                    q = 15
                }
                binding.quantityTv.text = q.toString()
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    viewModel.quantityList[position] = q // 수량 업데이트
                    viewModel.updateCartTotalPrice()
                }
            }

            binding.minusBtn.setOnClickListener {
                val position = adapterPosition
                var q = binding.quantityTv.text.toString().toInt()

                if (position != RecyclerView.NO_POSITION) {
                    // 수량 감소 (최소값 1)
                    if (q > 1) {
                        q -= 1
                        binding.quantityTv.text = q.toString() // UI 업데이트
                        viewModel.quantityList[position] = q // ViewModel 갱신
                        viewModel.updateCartTotalPrice() // 총 가격 업데이트
                    } else {
                        // 최소값 방어
                        Log.d(TAG, "수량은 최소 1이어야 합니다.")
                    }
                } else {
                    Log.e(TAG, "잘못된 adapterPosition: $position")
                }
            }



            binding.btnDelete.setOnClickListener {
                val position = adapterPosition
                var q = binding.quantityTv.text.toString().toInt()
                if (position != RecyclerView.NO_POSITION) {
                    Log.d(TAG, "bindInfo: ${viewModel.completedCombi}")

                    // 리스트에 데이터가 있는지 확인하고 삭제 진행
                    if (viewModel.completedCombi.isNotEmpty() && position < viewModel.completedCombi.size
                        && viewModel.quantityList.isNotEmpty() && viewModel.quantityList.size > position
                    ) {
                        // 아이템 삭제
                        viewModel.completedCombi.removeAt(position)
                        viewModel.quantityList.removeAt(position)
                        // LiveData 갱신 또는 UI 갱신을 위해 updateCartTotalPrice() 호출
                        viewModel.updateCartTotalPrice()
                        notifyItemRemoved(position)  // 아이템 삭제 후, UI에서 해당 항목을 제거
                    } else {
                        if (position != RecyclerView.NO_POSITION) {
                            viewModel.quantityList[position] = q
                        }
                        Log.e(TAG, "삭제할 아이템이 존재하지 않거나, 잘못된 위치입니다.")
                    }
                }
            }

            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                viewModel.quantityList[position] = binding.quantityTv.text.toString().toInt()
            }
        }
    }

    private fun getItemInfo(item: Combination, binding: ListItemShoppingListBinding) {
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val productService = ApplicationClass.retrofit.create(ProductService::class.java)
                val product = productService.getProduct(item.sideDish1)
                val main = productService.getProduct((item.main))
                Log.d(TAG, "getItemInfo: $main")
                val side1 = productService.getProduct((item.sideDish1))
                val side2 = productService.getProduct((item.sideDish2))
                val soup = productService.getProduct((item.soup))

                // UI 업데이트는 Main Dispatcher에서 처리
                withContext(Dispatchers.Main) {
                    val title = product.name
                    val imgSrc = product.img.removeSuffix(".png")
                    val imgResId = binding.root.context.resources.getIdentifier(
                        imgSrc,
                        "drawable",
                        binding.root.context.packageName
                    ) // context를 통해 resources 접근

                    val moreInfo =
                        "${main.name}, ${side1.name}, ${side2.name}, ${soup.name}"
                    Log.d(TAG, "getItemInfo: $moreInfo")

                    val titleMaxLength = 6
                    val truncatedTitle = if (title.length > titleMaxLength) {
                        title.substring(0, titleMaxLength) + "..."
                    } else title
                    val infoMaxLength = 13
                    val truncatedInfo = if (moreInfo.length > infoMaxLength) {
                        moreInfo.substring(0, infoMaxLength) + "..."
                    } else moreInfo

                    binding.textShoppingMenuName.text = "$truncatedTitle 도시락"
                    binding.textShoppingMenuMoney.text = "${item.dosirackPrice}원"  // 세트 가격
                    binding.textShoppingNum.text = truncatedInfo  // 세트 상세 내용

                    Glide.with(binding.root.context)
                        .load(imgResId)
                        .placeholder(R.drawable.loading_animation)
                        .into(binding.menuImage)
                }
            } catch (e: Exception) {
                // 예외 처리
                Log.e(TAG, "Failed to fetch product data", e)
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingCartViewHolder {
        val binding =
            ListItemShoppingListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShoppingCartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ShoppingCartViewHolder, position: Int) {
        val item = viewModel.completedCombi[position]
        holder.bindInfo(item, position)
    }

    override fun getItemCount(): Int {
        return viewModel.completedCombi.size
    }
}
