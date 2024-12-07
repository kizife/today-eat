
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.data.local.SharedPreferencesUtil
import com.ssafy.smartstore_jetpack.data.model.dto.CommentTodayeat
import com.ssafy.smartstore_jetpack.ui.category.ReviewViewModel

private const val TAG = "ReviewAdapter_싸피"
class ReviewAdapter(
    private var list: MutableList<CommentTodayeat>,
    private val activity: FragmentActivity,
    private val reviewViewModel: ReviewViewModel
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name = itemView.findViewById<TextView>(R.id.text_id)
        val content = itemView.findViewById<TextView>(R.id.text_review)
        val rating = itemView.findViewById<TextView>(R.id.rating_text)
        val deleteBtn: ImageView = itemView.findViewById(R.id.delete_btn)
        val save: ImageView = itemView.findViewById(R.id.confirm_button)
        val back: ImageView = itemView.findViewById(R.id.cancel_button)
        val updateBtn: ImageView = itemView.findViewById(R.id.update_btn)
        val edit = itemView.findViewById<EditText>(R.id.edit_text_review)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val item = list[position]

        if (SharedPreferencesUtil(activity).getUser().id == item.userId) {
            holder.deleteBtn.visibility = View.VISIBLE
            holder.updateBtn.visibility = View.VISIBLE
        }



        holder.name.text = item.userId
        holder.content.text = item.comment
        holder.rating.text = item.rating.toString()
        Log.d(TAG, "onBindViewHolder: ${item.productId}")
        Log.d(TAG, "onBindViewHolder: ${item.commentId}")

        holder.deleteBtn.setOnClickListener {

            reviewViewModel.removeComment(item.commentId)
            removeItem(item.commentId)

        }

        holder.updateBtn.setOnClickListener {
            holder.edit.visibility = View.VISIBLE
            holder.content.visibility = View.GONE
            holder.save.visibility = View.VISIBLE
            holder.back.visibility = View.VISIBLE
            holder.deleteBtn.visibility = View.GONE
            holder.updateBtn.visibility = View.GONE
        }

        holder.save.setOnClickListener {
            val newComment = holder.edit.text.toString()
            reviewViewModel.updateComment(
                CommentTodayeat(
                    comment = newComment, commentId = item.commentId, productId = item.productId,
                    rating = item.rating, userId = item.userId
                )
                
            )
            holder.content.text = newComment
            holder.edit.visibility = View.GONE
            holder.content.visibility = View.VISIBLE
            holder.save.visibility = View.GONE
            holder.back.visibility = View.GONE
            holder.deleteBtn.visibility = View.VISIBLE
            holder.updateBtn.visibility = View.VISIBLE

        }

        holder.back.setOnClickListener {
            holder.edit.visibility = View.GONE
            holder.content.visibility = View.VISIBLE
            holder.save.visibility = View.GONE
            holder.back.visibility = View.GONE
            holder.deleteBtn.visibility = View.VISIBLE
            holder.updateBtn.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int = list.size

    fun removeItem(commentId: Int) {
        val index = list.indexOfFirst { it.commentId == commentId }
        Log.d(TAG, "Item removed at index: $index")
        if (index != -1) {
            list.removeAt(index)  // list에서 항목을 제거
            notifyItemRemoved(index)  // RecyclerView에서 항목 제거를 알림
            notifyItemRangeChanged(index, itemCount)  // 나머지 항목들에 대한 데이터 업데이트
        }
    }

    fun updateData(newList: List<CommentTodayeat>) {
        list.clear()  // 기존 리스트를 비우고
        list.addAll(newList)  // 새로운 데이터로 채움
        notifyDataSetChanged()  // RecyclerView에 데이터가 변경되었음을 알림
    }
}
