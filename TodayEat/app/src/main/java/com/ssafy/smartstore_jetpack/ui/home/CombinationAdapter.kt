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
import com.ssafy.smartstore_jetpack.ui.ProductServiceObject

class CombinationAdapter(private val combiList: List<Combination>,
                         private val activity: FragmentActivity,
                         private val onItemClicked: (Combination, Int) -> Unit // 클릭 이벤트 콜백
                         ) :
    RecyclerView.Adapter<CombinationAdapter.CombinationViewHolder>() {

    inner class CombinationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val img = itemView.findViewById<ImageView>(R.id.orderMenuImg)
        val name = itemView.findViewById<TextView>(R.id.orderMenuName)
        val price = itemView.findViewById<TextView>(R.id.orderMenuPrice)
        val discription = itemView.findViewById<TextView>(R.id.orderMenuDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CombinationViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_order_detail_list, parent, false)
        return CombinationViewHolder(view)
    }

    override fun onBindViewHolder(holder: CombinationViewHolder, position: Int) {
        val combi = combiList[combiList.size - 1 - position]
        holder.price.text = "${combi.dosirackPrice}원"

        var side1: ProductTodayeat? = null
        var main: ProductTodayeat? = null

        // 콜백 카운트로 비동기 완료 여부 체크
        var callbacksCompleted = 0
        val totalCallbacks = 2

        fun updateUIIfReady() {
            if (callbacksCompleted == totalCallbacks && side1 != null && main != null) {
                holder.name.text = "${side1!!.name} 도시락!"
                holder.discription.text = "${main!!.name}과 함께 먹는 ${side1!!.name}도시락"

                val resId = activity.resources.getIdentifier(
                    side1!!.img.replace(".png", ""),
                    "drawable", activity.packageName
                )
                Glide.with(activity)
                    .load(resId)
                    .into(holder.img)
            }
        }

        holder.itemView.setOnClickListener {
            onItemClicked(combi, 1)
        }

        // 사이드1 데이터 로드
        ProductServiceObject.findProduct(combi.sideDish1) { product ->
            side1 = product
            callbacksCompleted++
            updateUIIfReady()
        }

        // 메인 데이터 로드
        ProductServiceObject.findProduct(combi.main) { product ->
            main = product
            callbacksCompleted++
            updateUIIfReady()
        }
    }

    override fun getItemCount(): Int = combiList.size
}


