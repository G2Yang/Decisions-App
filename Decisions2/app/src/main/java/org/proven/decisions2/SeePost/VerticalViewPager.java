package org.proven.decisions2.SeePost;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class VerticalViewPager extends ViewPager {

    // Constructor for VerticalViewPager class with a context parameter
    public VerticalViewPager(@NonNull Context context) {
        super(context);
        init();
    }

    // Constructor for VerticalViewPager class with context and attribute set parameters
    public VerticalViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    // Initialization method
    public void init(){
        setPageTransformer(true, new ViewPagerTransfom()); // Set the page transformer for the ViewPager
        setOverScrollMode(OVER_SCROLL_IF_CONTENT_SCROLLS); // Set the over-scroll mode to have a bounce effect when scrolling up and down
    }

    // Method to swap X and Y coordinates of a MotionEvent
    private MotionEvent SwapXY(MotionEvent event){
        float x = getWidth();
        float y = getHeight();

        float newX = (event.getY()/y)*y;
        float newY = (event.getX()/x)*x;

        event.setLocation(newX, newY);
        return event;
    }

    // Override method for intercepting touch events
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean intercept = super.onInterceptTouchEvent(SwapXY(ev));

        SwapXY(ev);

        return intercept;
    }

    // Override method for handling touch events
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(SwapXY(ev)); // Call the superclass's onTouchEvent method with the modified MotionEvent
    }

    // Inner class implementing the PageTransformer interface
    public class ViewPagerTransfom implements PageTransformer{

        private  static final float MIN_SCALE = 0.65f;
        // Method for transforming a page during animation
        @Override
        public void transformPage(@NonNull View page, float position) {
            if (position < -1){
                page.setAlpha(0);
            } else if (position <= 0){
                page.setAlpha(1 + position);
                page.setTranslationX(page.getWidth() * -position);
                page.setTranslationY(page.getHeight() * position);

                page.setScaleX(1);
                page.setScaleY(1);
            } else if (position <= 1){
                page.setAlpha(1 - position);
                page.setTranslationX(page.getWidth() * -position);
                page.setTranslationY(page.getHeight() * position);

                page.setScaleX(1);
                page.setScaleY(1);
            } else if (position > 1){
                page.setAlpha(0);
            }
        }
    }
}
