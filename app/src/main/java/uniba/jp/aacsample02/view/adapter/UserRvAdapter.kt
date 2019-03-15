package uniba.jp.aacsample02.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_view.view.*
import uniba.jp.aacsample02.R
import uniba.jp.aacsample02.models.User
import androidx.recyclerview.widget.DiffUtil


class UserRvAdapter : RecyclerView.Adapter<UserRvAdapter.ViewHolder>() {

    private lateinit var listener: OnItemClickListener
    private lateinit var longListener: OnItemLongClickListener
    private var listData: ArrayList<User> = ArrayList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        setOnItemClickListener(listener)
        setOnItemLongClickListener(longListener)

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.name.text = "[" + listData[position].uid + "] - " + listData[position].name

        // TODO: アイテムを削除したときに最終行をタップするとIndexOutOfBoundsException
        holder.itemView.setOnClickListener {
            listener.onClick(it, listData[position])
        }

        holder.itemView.setOnLongClickListener {
            longListener.onLongClick(it, listData[position])
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount() = listData.size

    fun setData(newData: List<User>) {
        val postDiffCallback = PostDiffCallback(listData, newData)
        val diffResult = DiffUtil.calculateDiff(postDiffCallback)

        listData.clear()
        listData.addAll(newData)
        diffResult.dispatchUpdatesTo(this)
    }

    interface OnItemClickListener {
        fun onClick(view: View, data: User)
    }

    interface OnItemLongClickListener {
        fun onLongClick(view: View, data: User)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    fun setOnItemLongClickListener(listener: OnItemLongClickListener) {
        this.longListener = listener
    }


    class PostDiffCallback(private val oldPosts: List<User>, private val newPosts: List<User>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldPosts.size
        }

        override fun getNewListSize(): Int {
            return newPosts.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldPosts[oldItemPosition].uid == newPosts[newItemPosition].uid
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldPosts[oldItemPosition] == newPosts[newItemPosition]
        }
    }
}