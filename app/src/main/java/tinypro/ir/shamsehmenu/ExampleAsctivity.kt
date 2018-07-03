package tinypro.ir.shamsehmenu

import android.content.Context
import android.graphics.Rect
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.*
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_example_asctivity.*
import tinypro.ir.shamsehmenulib.ShamsehMenu
import tinypro.ir.shamsehmenulib.ShamsehMenuView

class ExampleAsctivity : AppCompatActivity(), ShamsehMenu.Callback {

    var softkeyWidth = 0;
    var softkeyHeight = 0;

    override fun contentViewFor(menuItemSpec: ShamsehMenuView.MenuItemSpec?, parent: ViewGroup?): View {

        return LayoutInflater.from(this).inflate(R.layout.menu_content, parent, false);
    }

    override fun onShareButtonClicked(spec: ShamsehMenuView.MenuItemSpec?, contentView: View?) {

        Toast.makeText(this, "Hello", Toast.LENGTH_LONG).show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example_asctivity)

        root.setOnTouchListener { v, e ->

            if( e.action == MotionEvent.ACTION_DOWN) {
                var rect = Rect();
                windowManager.defaultDisplay.getRectSize(rect)
                var shamsehMenu = ShamsehMenu(this);

                val _24dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24f, resources.displayMetrics);
                shamsehMenu.show(root, (e.rawX).toInt(), (e.rawY).toInt(), (rect.width() / 1.5).toInt(), rect.height() / 2, null, 0x55000000);
                shamsehMenu.setCallbackListener(this)
            }

            true;
        }
    }

    public fun btnClicked(btn : View) {


    }

    fun hasSoftKeys(context: Context): Boolean {

        val hasSoftwareKeys: Boolean

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val d = (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay

            val realDisplayMetrics = DisplayMetrics()
            d.getRealMetrics(realDisplayMetrics)

            val realHeight = realDisplayMetrics.heightPixels
            val realWidth = realDisplayMetrics.widthPixels

            val displayMetrics = DisplayMetrics()
            d.getMetrics(displayMetrics)

            val displayHeight = displayMetrics.heightPixels
            val displayWidth = displayMetrics.widthPixels

            this.softkeyWidth = realWidth - displayWidth
            this.softkeyHeight = realHeight - displayHeight

            hasSoftwareKeys = this.softkeyWidth > 0 || this.softkeyHeight > 0
        } else {

            val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
            val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
            hasSoftwareKeys = !hasMenuKey && !hasBackKey
        }
        return hasSoftwareKeys
    }


}
