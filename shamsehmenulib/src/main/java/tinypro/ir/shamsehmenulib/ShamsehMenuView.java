package tinypro.ir.shamsehmenulib;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.util.ArrayList;

public class ShamsehMenuView extends FrameLayout {

    private enum Direction { left, right }
    public enum Angle {

        East(0), North_East(45), North(90), North_West(135), West(180), South_West(225), South(270), South_East(315);

        public float degree = 0;
        Angle (float degree) {

            this.degree = degree;
        }

        public Angle next() {

            return this.values()[(ordinal()+1)%this.values().length];
        }

        PointF getPosition(float radius, float radiusTranslate, float objectTranslate) {

            return getPosition(radius, radiusTranslate, objectTranslate, this.degree);
        }

        static PointF getPosition(float radius, float radiusTranslate, float objectTranslate, float degrees) {

            degrees = -(degrees * (float) Math.PI) / 180;
            float cos = (float)Math.cos(degrees);
            float sin = (float)Math.sin(degrees);
            float X = ( (radius + radiusTranslate) * cos ) + radius + objectTranslate;
            float Y = ( (radius + radiusTranslate) * sin ) + radius + objectTranslate;

            return new PointF(X,Y);
        }

        static Angle BASE_ANGLE = North_West;
    }
    public enum MenuItemSpec {

        facebook(R.drawable.ic_facebook, R.drawable.ic_facebook_box, Angle.North),
        google(R.drawable.ic_google_plus, R.drawable.ic_google_plus_box, Angle.West),
        twitter(R.drawable.ic_twitter, R.drawable.ic_twitter_box, Angle.South),
        share(R.drawable.ic_share_variant, 0, Angle.East)
        ;

        int buttonIcon;
        int headerIcon;
        Angle angle;

        MenuItemSpec(int buttonIcon, int headerIcon, Angle angle) {

            this.buttonIcon = buttonIcon;
            this.headerIcon = headerIcon;
            this.angle = angle;
        }
    }

    private ArrayList<MenuItem> menuItems = new ArrayList<>();
    private ImageView img_bkg_collapsed = null;
    private ImageView img_bkg_expanded = null;
    private ImageButton btn_close = null;

    private float radius = 0;


    private float RADIUS_TRANSLATE = -60;
    private float BTN_TRANSLATE = - 40;
    private float MENU_ITEM_SIZE = 0;
    private final long ROTATE_ANIM_DURATION = 450;
    private final long EXPAND_ANIM_DURATION = 450;
    private final long CLOSE_ANIM_DURATION = 800;


    public Callback callbackListener = null;
    private boolean isExpanded = false;

// ____________________________________________________________________
    public ShamsehMenuView(@NonNull Context context) {
        super(context);
        init();
    }

    public ShamsehMenuView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShamsehMenuView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(21)
    public ShamsehMenuView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

// ____________________________________________________________________

    private void init() {


    }

// ____________________________________________________________________

    public void close() {

        closeAnimation();
    }
// ____________________________________________________________________

    public void show() {

        BTN_TRANSLATE =  - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getContext().getResources().getDisplayMetrics());
        RADIUS_TRANSLATE = - TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 35, getContext().getResources().getDisplayMetrics());
        MENU_ITEM_SIZE = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 40, getContext().getResources().getDisplayMetrics());

        initShamsehCollapsedBkgImg();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            boolean initialized = false;

            @Override
            public void onGlobalLayout() {

                if(initialized)
                    return;
                initialized = true;


                radius = getLayoutParams().width/2;
                initButtons();
                initCloseBtn();

                openAnimation();

                initShamsehExpandedBkgImg();
            }
        });

    }

// ____________________________________________________________________

    private void initCloseBtn() {

        btn_close = new ImageButton(getContext());
        LayoutParams layoutParams = new LayoutParams((int)MENU_ITEM_SIZE, (int)MENU_ITEM_SIZE);
        btn_close.setLayoutParams(layoutParams);
        btn_close.setImageResource(R.drawable.ic_cross);
        btn_close.setBackground(null);

        ViewGroup.LayoutParams lp = getLayoutParams();
        btn_close.setX(lp.width / 2 - (int)MENU_ITEM_SIZE / 2);
        btn_close.setY(lp.height / 2 - (int)MENU_ITEM_SIZE / 2);
        btn_close.setScaleType(ImageView.ScaleType.FIT_CENTER);

        addView(btn_close);
    }

// ____________________________________________________________________

    private void initButtons() {


        for(MenuItemSpec spec : MenuItemSpec.values()) {

            MenuItem menuItem = new MenuItem(getContext());
            this.addView(menuItem);
            menuItem.config((int)MENU_ITEM_SIZE,(int)MENU_ITEM_SIZE, radius, RADIUS_TRANSLATE, BTN_TRANSLATE, spec);
            menuItems.add(menuItem);
            menuItem.setCallbackListener(menuItemCallbackListener);
        }
    }

// ____________________________________________________________________

    private void openAnimation() {

        this.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.menu_open));
    }

// ____________________________________________________________________

    private void closeAnimation() {

        Animation animation = AnimationUtils.loadAnimation(getContext(), isExpanded ? R.anim.menu_close_expanded : R.anim.menu_close);
        animation.setFillAfter(true);
        this.startAnimation(animation);
        postDelayed(new Runnable() {
            @Override
            public void run() {

                if(callbackListener != null)
                    callbackListener.onClosed();
            }
        }, animation.getDuration());
    }

// ____________________________________________________________________

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        return true;
    }


// ____________________________________________________________________

    private void initShamsehCollapsedBkgImg() {

        img_bkg_collapsed = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        img_bkg_collapsed.setLayoutParams(layoutParams);

        img_bkg_collapsed.setImageResource(R.drawable.shamseh_collapsed);

        this.addView(img_bkg_collapsed);
    }

// ____________________________________________________________________

    private void initShamsehExpandedBkgImg() {

        img_bkg_expanded  = new ImageView(getContext());
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        img_bkg_expanded.setLayoutParams(layoutParams);

//        img_bkg_expanded.setBackgroundColor(0xffff0000);
        img_bkg_expanded.setImageResource(R.drawable.shamseh_expanded);
        img_bkg_expanded.setVisibility(INVISIBLE);

//        img_bkg_expanded.setBackgroundResource(R.drawable.shamseh_expanded);
        img_bkg_expanded.setScaleType(ImageView.ScaleType.FIT_XY);

        this.addView(img_bkg_expanded);
    }

// ____________________________________________________________________

    private void rotate(Angle toAngle) {

        float toDegree = (Angle.BASE_ANGLE.degree - toAngle.degree);

        ValueAnimator va = ValueAnimator.ofFloat(img_bkg_collapsed.getRotation(), toDegree);
        va.setDuration(ROTATE_ANIM_DURATION);
        va.setInterpolator(new DecelerateInterpolator());

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {


                float degrees = (float)valueAnimator.getAnimatedValue();

                img_bkg_collapsed.setRotation(-degrees);


                for(MenuItem menuItem : menuItems) {

                    float total_degrees = degrees + menuItem.getAngle().degree;
                    PointF point = Angle.getPosition(radius, RADIUS_TRANSLATE, BTN_TRANSLATE, total_degrees);
                    menuItem.setX(point.x);
                    menuItem.setY(point.y);
                }

            }
        });

        va.start();

        postDelayed(new Runnable() {
            @Override
            public void run() {

                expand();
            }
        }, ROTATE_ANIM_DURATION);

    }

// ____________________________________________________________________

    private void expand() {


        isExpanded = true;
        img_bkg_expanded.setAlpha(1f);
        img_bkg_expanded.setVisibility(VISIBLE);

        ViewGroup.LayoutParams lp = getLayoutParams();
        final int minValue = lp.width;
        final int maxValue = (int)((float)lp.width * 1.5);

        ValueAnimator va = ValueAnimator.ofInt(minValue, maxValue);
        va.setDuration(EXPAND_ANIM_DURATION);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                int size = (int)valueAnimator.getAnimatedValue();
                float alpha = ((float)size) / (float)maxValue;
                LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();

                img_bkg_expanded.setAlpha(alpha);
                layoutParams.width = size;
                layoutParams.height = size;

                if(layoutParams.leftMargin + layoutParams.width > ((View)getParent()).getWidth())
                    layoutParams.leftMargin -= (layoutParams.leftMargin + layoutParams.width - ((View)getParent()).getWidth());

                if(layoutParams.topMargin + layoutParams.height > ((View)getParent()).getHeight())
                    layoutParams.topMargin -= (layoutParams.topMargin + layoutParams.height - ((View)getParent()).getHeight());

                if(callbackListener != null)
                    callbackListener.onSizeChanged(size, size);

                requestLayout();

            }
        });

        va.start();

    }

// ____________________________________________________________________

    MenuItem.Callback menuItemCallbackListener = new MenuItem.Callback() {
        @Override
        public void onSelected(MenuItem menuItem, Angle angle) {

            rotate(angle);
        }
    }  ;

// ____________________________________________________________________

    public interface Callback {

        void onSizeChanged(int width, int height);
        void onClosed();
    }

// ____________________________________________________________________

}