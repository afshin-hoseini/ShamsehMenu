package tinypro.ir.shamsehmenu

import android.graphics.Rect
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import kotlinx.android.synthetic.main.activity_example_asctivity.*
import tinypro.ir.shamsehmenulib.ShamsehMenu

class ExampleAsctivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example_asctivity)

        root.setOnTouchListener { v, e ->

            if( e.action == MotionEvent.ACTION_UP) {
                var rect = Rect();
                windowManager.defaultDisplay.getRectSize(rect)
                var shamsehMenu = ShamsehMenu(this);
                shamsehMenu.show(root, e.rawX.toInt(), e.rawY.toInt());
            }

            true;
        }
    }

    public fun btnClicked(btn : View) {


    }
}
