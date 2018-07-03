package tinypro.ir.shamsehmenulib;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

public class ShamsehMenu {

    private PopupWindow popupWindow = null;
    private Context context;
    private Callback callbackListener = null;
    private ShamsehMenuView shamsehMenuView = null;

// ____________________________________________________________________

    public ShamsehMenu(Context context) {

        this.context = context;

    }

// ____________________________________________________________________

    public void setCallbackListener(Callback callbackListener) {

        this.callbackListener = callbackListener;
    }

// ____________________________________________________________________

    public void close() {

        shamsehMenuView.close();
    }

// ____________________________________________________________________

    public void show(View parent, int x, int y, int expandedWidth, int expandedHeight, @Nullable Rect boundTranslation, int dimColor) {

        if(boundTranslation == null)
            boundTranslation = new Rect();

        int[] parentScreenLocation = new int[2];
        parent.getLocationOnScreen(parentScreenLocation);

        //Sizes the shamseh menu view
        int size = ContextCompat.getDrawable(context, R.drawable.shamseh_collapsed).getIntrinsicWidth();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size, size);

        shamsehMenuView = new ShamsehMenuView(context);
        shamsehMenuView.setLayoutParams(layoutParams);
        shamsehMenuView.callbackListener = shamsehView_callbackListener;

        expandedHeight = Math.max(expandedHeight, (int)(size * 1.5));
        expandedWidth = Math.max(expandedWidth, (int)(size * 1.5));


        //We want to position the center of menu on given x,y coordinates
        float xPos = (x - size / 2) - parentScreenLocation[0];
        float yPos = (y - size /2) - parentScreenLocation[1];

        //Then we care about the edges of parent view
        if(xPos < 0)
            xPos = 0;
        if(xPos + size > parent.getWidth())
            xPos -= (xPos + size) - parent.getWidth() - boundTranslation.right;

        if(yPos < 0)
            yPos = 0;
        if(yPos + size > parent.getHeight())
            yPos -= (yPos + size) - parent.getHeight() - boundTranslation.bottom;


        layoutParams.topMargin = (int)yPos;
        layoutParams.leftMargin = (int)xPos;


        ViewGroup wrapperView = new FrameLayout(context);
        wrapperView.addView(shamsehMenuView);

        popupWindow = new PopupWindow(parent.getWidth(), parent.getHeight());

        popupWindow.setContentView(wrapperView);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_FROM_FOCUSABLE);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(dimColor));
        popupWindow.setAnimationStyle(R.style.ShamsehMenu_PopUpWindowAnimation);


        popupWindow.showAtLocation(parent, Gravity.TOP|Gravity.LEFT, parentScreenLocation[0], parentScreenLocation[1]);

        shamsehMenuView.show(expandedWidth, expandedHeight);

        wrapperView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shamsehMenuView.close();
            }
        });
    }

// ____________________________________________________________________

    ShamsehMenuView.Callback shamsehView_callbackListener = new ShamsehMenuView.Callback() {
        @Override
        public void onSizeChanged(int width, int height) {}

        @Override
        public void onClosed() {

            shamsehMenuView.release();
            shamsehMenuView = null;
            popupWindow.dismiss();
        }

        @Override
        public View contentViewFor(ShamsehMenuView.MenuItemSpec menuItemSpec, ViewGroup parent) {

            if(callbackListener != null)
                return callbackListener.contentViewFor(menuItemSpec, parent);
            return null;
        }

        @Override
        public void onShareButtonClicked(ShamsehMenuView.MenuItemSpec spec, View contentView) {

            if(callbackListener != null)
                callbackListener.onShareButtonClicked(spec, contentView);
        }
    };
// ____________________________________________________________________

    public interface Callback {

        View contentViewFor(ShamsehMenuView.MenuItemSpec menuItemSpec, ViewGroup parent);
        void onShareButtonClicked(ShamsehMenuView.MenuItemSpec spec, View contentView);
    }

// ____________________________________________________________________
}
