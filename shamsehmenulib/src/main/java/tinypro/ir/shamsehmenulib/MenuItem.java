package tinypro.ir.shamsehmenulib;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class MenuItem extends LinearLayout {

    private Callback callbackListener = null;
    private ImageButton menuItemBtn = null;
    ShamsehMenuView.MenuItemSpec spec = null;

// ____________________________________________________________________
    public MenuItem(Context context) {
        super(context);
        init();
    }

    public MenuItem(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MenuItem(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public MenuItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

// ____________________________________________________________________

    private void init() {

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
    }

// ____________________________________________________________________

    ShamsehMenuView.Angle getAngle() {

        return this.spec.angle;
    }
// ____________________________________________________________________

    void config(int width, int height, float radius, float radiusTranslate, float menuItemTranslate, ShamsehMenuView.MenuItemSpec spec) {

        this.spec = spec;

        menuItemBtn = new ImageButton(getContext());
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(width, height);
        menuItemBtn.setLayoutParams(layoutParams);
        menuItemBtn.setImageResource(spec.buttonIcon);
        menuItemBtn.setBackground(null);

        PointF point = spec.angle.getPosition(radius, radiusTranslate, menuItemTranslate);
        this.setX(point.x);
        this.setY(point.y);


        addView(menuItemBtn);

        menuItemBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(callbackListener != null)
                    callbackListener.onSelected(MenuItem.this, MenuItem.this.spec.angle);
            }
        });
    }

// ____________________________________________________________________

    private void expand() {


    }

// ____________________________________________________________________

    void setCallbackListener(Callback callbackListener) {

        this.callbackListener = callbackListener;
    }

// ____________________________________________________________________

    interface Callback {

        void onSelected(MenuItem menuItem, ShamsehMenuView.Angle angle);
    }

// ____________________________________________________________________
}
