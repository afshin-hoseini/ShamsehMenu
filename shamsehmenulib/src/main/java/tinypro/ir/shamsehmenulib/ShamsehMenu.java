package tinypro.ir.shamsehmenulib;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

public class ShamsehMenu {

    PopupWindow popupWindow = null;
    Context context;

// ____________________________________________________________________

    public ShamsehMenu(Context context) {

        this.context = context;

    }

// ____________________________________________________________________

    public void show(View parent, int x, int y) {


        //Sizes the shamseh menu view
        int size = ContextCompat.getDrawable(context, R.drawable.shamseh_collapsed).getIntrinsicWidth();
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(size, size);

        final ShamsehMenuView shamsehMenuView = new ShamsehMenuView(context);
        shamsehMenuView.setLayoutParams(layoutParams);
        shamsehMenuView.callbackListener = callbackListener;
//        shamsehMenuView.setBackgroundColor(0xffff0000);


        //We want to position the center of menu on given x,y coordinates
        float xPos = x - size / 2;
        float yPos = y - size /2;

        //Then we care about the edges of parent view
        if(xPos < 0)
            xPos = 0;
        if(xPos + size > parent.getWidth())
            xPos -= (xPos + size) - parent.getWidth();

        if(yPos < 0)
            yPos = 0;
        if(yPos + size > parent.getHeight())
            yPos -= (yPos + size) - parent.getHeight();


        layoutParams.topMargin = (int)yPos;
        layoutParams.leftMargin = (int)xPos;


        ViewGroup wrapperView = new FrameLayout(context);
        wrapperView.addView(shamsehMenuView);

        popupWindow = new PopupWindow(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        popupWindow.setBackgroundDrawable(new ColorDrawable(0xff00ff00));

        popupWindow.setContentView(wrapperView);

        popupWindow.showAtLocation(parent, Gravity.TOP|Gravity.LEFT, 0, 0);

        shamsehMenuView.show();

        wrapperView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                shamsehMenuView.close();
            }
        });
    }

// ____________________________________________________________________

    ShamsehMenuView.Callback callbackListener = new ShamsehMenuView.Callback() {
        @Override
        public void onSizeChanged(int width, int height) {

//            popupWindow.update(width, height);
        }

        @Override
        public void onClosed() {
            popupWindow.dismiss();
        }
    };

// ____________________________________________________________________
}
