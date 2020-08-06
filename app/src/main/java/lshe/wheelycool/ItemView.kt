package lshe.wheelycool

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.item_view.view.*
import lshe.wheelycool.MainActivity.Item

/**
 * A custom view for an Item
 */
class ItemView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0,
    defStyleRes: Int = 0
) : LinearLayout(context, attrs, defStyle, defStyleRes) {

    init {
        LayoutInflater.from(context).inflate(R.layout.item_view, this, true)
    }

    interface OnItemAction {
        fun onDeleteClicked(item : Item)
    }

    fun setItemProps(item : Item, itemAction: OnItemAction) {
        text.text = item.getText()
        delete_button.setOnClickListener { itemAction.onDeleteClicked(item) }
    }

}