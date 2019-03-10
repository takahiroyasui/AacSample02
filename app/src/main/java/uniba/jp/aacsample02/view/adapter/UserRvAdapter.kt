package uniba.jp.aacsample02.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_view.view.*
import uniba.jp.aacsample02.R
import uniba.jp.aacsample02.models.User


class UserRvAdapter(private val myDataSet: ArrayList<User>) :
        RecyclerView.Adapter<UserRvAdapter.ViewHolder>() {

    private lateinit var listener: OnItemClickListener
    private lateinit var longListener: OnItemLongClickListener

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        setOnItemClickListener(listener)
        setOnItemLongClickListener(longListener)

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.name.text = myDataSet[position].name

        holder.itemView.setOnClickListener {
            listener.onClick(it, myDataSet[position])
        }

        holder.itemView.setOnLongClickListener {
            longListener.onLongClick(it, myDataSet[position])
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount() = myDataSet.size

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

    fun addData(start : Int, count: Int) {
        notifyItemRangeInserted(start, count)
    }

    fun update() {
        notifyDataSetChanged()
    }

    fun updateItem(position: Int) {
        notifyItemChanged(position)
    }

    fun removeItem(index: Int) {
        notifyItemRemoved(index)
    }
}