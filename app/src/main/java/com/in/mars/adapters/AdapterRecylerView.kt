package com.`in`.mars.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.`in`.mars.ActivityForImages
import com.`in`.mars.R
import com.`in`.mars.databinding.RowImagesBinding
import com.`in`.mars.ui.Url
import com.bumptech.glide.Glide


public class AdapterRecylerView(private val context: Context) :
    RecyclerView.Adapter<AdapterRecylerView.ItemHolder>() {
    private var list: List<Url>? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemHolder {

        val binding = DataBindingUtil.inflate<RowImagesBinding>(
            LayoutInflater.from(parent.context),
            R.layout.row_images,
            parent,
            false
        )
        return ItemHolder(binding)

    }


    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        val item = list!![position]
        if (item != null) {
            Glide.with(context)
                .load(item.url)
                .into(holder.binding.ivImage)
        }
        holder.binding.ivImage.setOnClickListener {
            val intent = Intent(context, ActivityForImages::class.java)
            intent.putExtra("url", item.url)
            context.startActivity(intent)

        }

    }


    override fun getItemCount(): Int {
        return if (list != null)
            list!!.size
        else
            0
    }

    fun setData(lists: List<Url>) {
        if (list == null) {
            list = ArrayList<Url>()
        }
        val old = list
        val diff: DiffUtil.DiffResult = DiffUtil.calculateDiff(Diff(old!!, lists!!))
        list = lists
        diff.dispatchUpdatesTo(this)


    }

    class ItemHolder(val binding: RowImagesBinding) : RecyclerView.ViewHolder(binding.root)


    class Diff(
        private val oldList: List<Url>,
        private val newList: List<Url>

    ) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition].url == newList[newItemPosition].url
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val old = oldList[oldItemPosition]
            val new = newList[newItemPosition]
            val isSame = old.equals(new)
            return isSame
        }

        override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
            return super.getChangePayload(oldItemPosition, newItemPosition)
        }
    }

}
