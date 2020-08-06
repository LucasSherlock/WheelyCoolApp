package lshe.wheelycool

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.parcel.Parcelize

/**
 * Screen A in the wireframe.
 * A user can add items to the list which will appear on the wheel in the next screen.
 */
class MainActivity : AppCompatActivity() {

    private var items = ArrayList<Item>() // List of items to add to the wheel
    private lateinit var recyclerView : RecyclerView
    private lateinit var viewAdapter : ItemListAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var id = 0 // tracks id of ListItems


    private val itemAction = object : ItemView.OnItemAction { // Implement Delete functionality
        override fun onDeleteClicked(item: Item) {
            val newList = ArrayList<Item>()
            newList.addAll(items)
            newList.remove(item)
            viewAdapter.submitList(newList)
            items = newList
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(
            this, LinearLayoutManager.VERTICAL, false)
        viewAdapter = ItemListAdapter(itemAction)

        // Setup RecyclerView
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        // Assign buttons
        var doneButton = findViewById<Button>(R.id.done_button)
        doneButton.setOnClickListener { navToWheel() }


        var addButton = findViewById<Button>(R.id.add_button)
        addButton.setOnClickListener{ getInput() }
    }

    /**
     * Called when pressing the 'Done' button
     * Navigate to the wheel activity, pass the list of items with the intent.
     */
    private fun navToWheel() {
        val intent = Intent(this, WheelActivity::class.java)
        intent.putExtra("items", items)
        startActivity(intent)
    }

    /**
     * Add a new item to the list, updating the RecyclerView using the adapter
     */
    private fun getInput() {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("New Item")

        val input = EditText(this)
        builder.setView(input)


        builder.setPositiveButton("Add"){
                _,_ ->
            val newList = ArrayList<Item>()
            newList.addAll(items)
            newList.add(Item(id, input.text.toString()))
            id++
            viewAdapter.submitList(newList)
            items = newList
        }

        builder.setNegativeButton("Cancel") {
                dialog,_ -> dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()


    }


    /**
     * An item_view in the list. Has a unique id and text.
     */
    @Parcelize
    class Item(private val id : Int, private var text : String) : Parcelable {
        fun getText() : String {
            return text
        }

        fun setText(newText : String) {
            text = newText
        }

        fun getId() : Int {
            return id
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false

            other as Item

            if (id != other.id) return false

            return true
        }

        override fun hashCode(): Int {
            return id
        }
    }

}