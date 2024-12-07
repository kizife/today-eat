package com.ssafy.smartstore_jetpack.ui.category

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssafy.smartstore_jetpack.R
import com.ssafy.smartstore_jetpack.data.model.dto.CommentTodayeat
import com.ssafy.smartstore_jetpack.data.remote.RetrofitUtil
import kotlinx.coroutines.launch

private const val TAG = "ReviewViewModel_싸피"

class ReviewViewModel : ViewModel() {

    private val _comments = MutableLiveData<MutableList<CommentTodayeat>>()
    val comments: LiveData<MutableList<CommentTodayeat>> get() = _comments

    fun loadComments(productId: Int) {
        viewModelScope.launch {
            try {
                val response = RetrofitUtil.productService.getProductWithComments(productId)
                _comments.postValue(response.comments)
                Log.d(TAG, "loadComments: ${response.comments}")
            } catch (e: Exception) {
                Log.d(TAG, "loadComments 실패: $e")
            }
        }
    }



    fun updateComment(comments: CommentTodayeat) {
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.commentService.update(comments)
                Log.d(TAG, "updateComment: $comments.commentId")
            }.onSuccess {
                Log.d(TAG, "updateComment: $it 이것은 코멘트")
                Log.d(TAG, "댓글 업데이트 성공")
            }.onFailure {
                Log.d(TAG, "댓글 업데이트 실패: ${it.message}")
            }
        }
    }

    fun addComment(comment: CommentTodayeat) {
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.commentService.insert(comment)
                Log.d(TAG, "addComment: $comment")
            }.onSuccess {
                Log.d(TAG, "addComment: $it")
                _comments.postValue(
                    (_comments.value ?: mutableListOf()).toMutableList().apply {
                        add(comment)
                    }
                )
            }.onFailure {
                Log.d(TAG, "댓글 추가 실패: ${it.message}")
            }
        }
    }


    fun removeComment(commentId: Int) {
        viewModelScope.launch {
            runCatching {
                RetrofitUtil.commentService.delete(commentId)
            }.onSuccess {
                Log.d(TAG, "removeComment: $it")
                _comments.value?.let {
                    it.removeAll { comment -> comment.commentId == commentId }
                    _comments.postValue(it)
                    Log.d(TAG, "removeComment: 삭제성공")
                }
            }.onFailure {
                Log.d(TAG, "댓글 삭제 실패: ${it.message}")
            }
        }
    }
}
