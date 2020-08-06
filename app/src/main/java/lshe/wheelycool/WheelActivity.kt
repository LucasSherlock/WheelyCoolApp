package lshe.wheelycool

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.animation.doOnEnd
import androidx.core.view.children
import lshe.wheelycool.MainActivity.Item
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

/**
 * Screen B in the wireframe
 * A user can press the spin button and the result of the spin will be shown above the wheel.
 */
class WheelActivity : AppCompatActivity() {
    private var items = ArrayList<Item>() // List of items to add to the wheel
    private var angleConst = 0f // the angle between items in the wheel
    private lateinit var layout : ConstraintLayout
    private val texts = ArrayList<TextView>() // List of TextViews in the wheel
    private lateinit var result : TextView // The result text

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wheel)

        items.addAll(intent.getParcelableArrayListExtra("items")) // populate items from intent

        layout = findViewById(R.id.root)
        result = findViewById(R.id.result)


        if (items.size > 0) {
            angleConst = 360f / items.size // calculate angleConst
        }

        arrangeOptions()
        findTextViews()

        // Assign button
        val spin = findViewById<Button>(R.id.spin_button)
        spin.setOnClickListener { spinWheel() }

    }

    /**
     * Takes the list of items from the intent and initialises the wheel, arranging them evenly
     * around it and rotating them to match their position.
     */
    private fun arrangeOptions() {
        var angle = 0f

        for (i in items) {
            var constraintSet = ConstraintSet()
            var text = TextView(this)
            text.id = View.generateViewId()
            text.text = i.getText()
            text.rotation = angle
            text.tag = "item"
            layout.addView(text)
            constraintSet.clone(layout)
            constraintSet.constrainCircle(text.id, R.id.centre, 300, angle + 90f)
            angle += angleConst
            constraintSet.applyTo(layout)

        }
    }

    /**
     * Find all the textviews in the wheel using the tag: "item" set in arrangeOptions()
     */
    private fun findTextViews() {
        for (c in layout.children) {
            if (c.tag == "item") {
                texts.add(c as TextView)
            }
        }
    }

    /**
     * Called when pressing the spin button
     * Animates the spinning of the wheel and shows the winner
     */
    private fun spinWheel() {
        result.visibility = View.INVISIBLE

        val endpoint = Random.nextInt(1080, 1800) // final rotation value
        val animators = ArrayList<ValueAnimator>()

        // Create ValueAnimators for all TextViews in the wheel
        for (t in texts) {
            val layoutParams = t.layoutParams as ConstraintLayout.LayoutParams

            val anim = ValueAnimator.ofFloat(layoutParams.circleAngle,
                endpoint.toFloat() + layoutParams.circleAngle)
            anim.addUpdateListener { valueAnimator: ValueAnimator ->
                val value = valueAnimator.animatedValue
                t.rotation = value as Float - 90f
                layoutParams.circleAngle = value
                t.layoutParams = layoutParams
            }
            anim.duration = 2000
            anim.interpolator = LinearInterpolator()

            anim.doOnEnd { // Display the winner
                if (layoutParams.circleAngle%360f > 90f - angleConst/2 &&
                    layoutParams.circleAngle%360f < 90f + angleConst/2) {
                    result.text = t.text
                    result.visibility = View.VISIBLE
                }
            }

            animators.add(anim)

        }


        animators.forEach {it.start()} // start the animations


    }

}