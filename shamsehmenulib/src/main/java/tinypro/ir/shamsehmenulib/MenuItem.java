package tinypro.ir.shamsehmenulib;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuItem extends FrameLayout {

    private Callback callbackListener = null;
    private ImageButton menuItemBtn = null;
    private ImageView img_headerIcon = null;
    ShamsehMenuView.MenuItemSpec spec = null;
    private boolean isSelected = false;
    private boolean isExpanded = false;

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

        img_headerIcon = new ImageView(getContext());
        FrameLayout.LayoutParams headerIcon_layoutParams = new FrameLayout.LayoutParams(width, height);
        img_headerIcon.setLayoutParams(headerIcon_layoutParams);
        img_headerIcon.setImageResource(spec.headerIcon);
        img_headerIcon.setColorFilter(0xff000000);
        img_headerIcon.setAlpha(0f);


        PointF point = spec.angle.getPosition(radius, radiusTranslate, menuItemTranslate);
        this.setX(point.x);
        this.setY(point.y);


        addView(menuItemBtn);
        addView(img_headerIcon);

        menuItemBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                if(isExpanded)
                    return;

                isSelected = true;
                if(callbackListener != null)
                    callbackListener.onSelected(MenuItem.this, MenuItem.this.spec.angle);
            }
        });
    }

// ____________________________________________________________________

    @Override
    public void setAlpha(float alpha) {

        menuItemBtn.setAlpha(alpha);

        if(isSelected) {

            img_headerIcon.setAlpha(1f - alpha);
        }
    }


// ____________________________________________________________________

    public void expand(int width, int marginRight) {

        isExpanded = true;
        View header = LayoutInflater.from(getContext()).inflate(R.layout.expanded_header_title_and_share, this, false);
        FrameLayout.LayoutParams lp = new LayoutParams(width - img_headerIcon.getWidth() - marginRight - (int)getX() , ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMarginStart(img_headerIcon.getWidth());
        header.setLayoutParams(lp);

        TextView txtTitle = header.findViewById(R.id.txt_title);
        txtTitle.setText(spec.title);

        Button btnShare = header.findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(callbackListener != null)
                    callbackListener.onShareButtonClicked(MenuItem.this);
            }
        });

        addView(header);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(400);
        header.startAnimation(alphaAnimation);


    }

// ____________________________________________________________________

    void setCallbackListener(Callback callbackListener) {

        this.callbackListener = callbackListener;
    }

// ____________________________________________________________________

    interface Callback {

        void onSelected(MenuItem menuItem, ShamsehMenuView.Angle angle);
        void onShareButtonClicked(MenuItem menuItem);
    }

// ____________________________________________________________________
}
