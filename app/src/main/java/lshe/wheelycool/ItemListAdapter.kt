package lshe.wheelycool

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_container.view.*
import lshe.wheelycool.MainActivity.Item

/**
 * Implementation of ListAdapter for the Item class.
 * Adapted from:
 *      https://android.jlelse.eu/recylerview-list-adapter-template-in-kotlin-6b9814201458
 * and:
 *      https://medium.com/@pavelblotski/listadapter-with-custom-view-components-as-rows-in-kotlin-c1da56debb40
 */
class ItemListAdapter(private val itemAction: ItemView.OnItemAction) : ListAdapter<Item, ItemListAdapter.ItemViewHolder>(DiffCallback()) {

    private lateinit var inflater: LayoutInflater

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        if (!this::inflater.isInitialized) {
            inflater = LayoutInflater.from(parent.context)
        }

        val itemView = inflater.inflate(R.layout.item_container, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ItemListAdapter.ItemViewHolder, position: Int) {
        holder.bindItem(getItem(position), itemAction)
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        fun bindItem(item: Item, itemAction: ItemView.OnItemAction) {
            itemView.item.setItemProps(item, itemAction)
        }
    }
}

class DiffCallback : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.getId() == newItem.getId()

    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.getText() == newItem.getText()
    }
}