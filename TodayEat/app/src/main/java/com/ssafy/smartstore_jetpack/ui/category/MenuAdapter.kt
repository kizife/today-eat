import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.data.model.dto.ProductTodayeat
import com.ssafy.smartstore_jetpack.ui.MainActivity

private const val TAG = "MenuAdapter_싸피"

class MenuAdapter(private val productList: List<ProductTodayeat>, private val activity: FragmentActivity) :
    RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.setOnClickListener {

                val product = productList[adapterPosition]

                (activity as MainActivity).openDetailFragment("detailMain",product)
            }
        }

        fun bind(product: ProductTodayeat) {
            itemView.findViewById<TextView>(R.id.textMenuNames).text = product.name
            itemView.findViewById<TextView>(R.id.textMenuPrice).text = "${product.price}원"

            val imageName = product.img
            val resId = itemView.context.resources.getIdentifier(
                imageName.replace(".png", ""),
                "drawable", itemView.context.packageName
            )

            Glide.with(itemView.context)
                .load(resId)
                .into(itemView.findViewById(R.id.menuImage))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val product = productList[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = productList.size
}
