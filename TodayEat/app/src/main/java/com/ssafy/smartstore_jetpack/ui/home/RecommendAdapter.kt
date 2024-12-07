import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.data.model.dto.Combination
import com.ssafy.smartstore_jetpack.data.model.dto.ProductTodayeat
import com.ssafy.smartstore_jetpack.data.remote.RetrofitUtil
import com.ssafy.smartstore_jetpack.ui.ProductServiceObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RecommendAdapter(
    private val list: List<Combination>,
    private val activity: FragmentActivity,
    private val onItemClicked: (Combination, Int) -> Unit
) : RecyclerView.Adapter<RecommendAdapter.CombinationViewHolder>() {

    inner class CombinationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name = itemView.findViewById<TextView>(R.id.textMenuNames)
        val price = itemView.findViewById<TextView>(R.id.textMenuPrice)
        val img = itemView.findViewById<ImageView>(R.id.menuImage)

        fun bind(combi: Combination) {
            // 기본값 설정
            price.text = "${combi.dosirackPrice}원"
            name.text = "로딩 중..."
            name.isSelected = true

            Glide.with(activity)
                .load(R.drawable.main3) // 기본 이미지
                .into(img)

            // 비동기 데이터 로드
            ProductServiceObject.findProduct(combi.sideDish1) { product ->
                if (product.name.isNotEmpty()) {
                    name.text = "${product.name} 도시락!"
                    val resId = activity.resources.getIdentifier(
                        product.img.replace(".png", ""),
                        "drawable", activity.packageName
                    )
                    Glide.with(activity)
                        .load(resId)
                        .into(img)
                } else {
                    name.text = "상품 정보 없음"
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CombinationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_order, parent, false)
        return CombinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: CombinationViewHolder, position: Int) {
        val combi = list[position]
        holder.bind(combi)
        holder.itemView.setOnClickListener {
            onItemClicked(combi, 0)
        }
    }

    override fun getItemCount(): Int = list.size

}
