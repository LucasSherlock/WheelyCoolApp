package lshe.wheelycool

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.preference.PreferenceManager
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
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
    private val SAVED_ITEMS = "saved_items"




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

        val sharedPrefs = getPreferences(Context.MODE_PRIVATE)

        if (sharedPrefs.contains(SAVED_ITEMS)) {
            Log.d("DEBUG", "has items")
            val gson = Gson()
            var json = sharedPrefs.getString(SAVED_ITEMS, "")
            val itemType = object : TypeToken<List<Item>>() {}.type
            items = gson.fromJson<List<Item>>(json, itemType) as ArrayList<Item>
            viewAdapter.submitList(items)
        }

        // Assign buttons
        var doneButton = findViewById<Button>(R.id.done_button)
        doneButton.setOnClickListener { navToWheel() }


        var addButton = findViewById<Button>(R.id.add_button)
        addButton.setOnClickListener{ getInput() }
    }

    private val itemAction = object : ItemView.OnItemAction { // Implement Delete functionality
        override fun onDeleteClicked(item: Item) {
            val newList = ArrayList<Item>()
            newList.addAll(items)
            newList.remove(item)
            viewAdapter.submitList(newList)
            items = newList
            saveItems()
        }
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
            saveItems()
        }

        builder.setNegativeButton("Cancel") {
                dialog,_ -> dialog.cancel()
        }
        val alertDialog = builder.create()
        alertDialog.show()
    }

    fun saveItems() {
        val sharedPrefs = getPreferences(Context.MODE_PRIVATE)
        val editor = sharedPrefs.edit()
        val gson = Gson()
        var json = gson.toJson(items)
        editor.putString(SAVED_ITEMS, json)
        editor.commit()
        if (sharedPrefs.contains(SAVED_ITEMS)) {
            Log.d("DEBUG", "has items")
        } else {
            Log.d("DEBUG", "has no items")
        }
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