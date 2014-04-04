package info.hoang8f.android.segmented;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class SegmentedGroup extends RadioGroup {

    private int oneDP;
    private Resources resources;
    private int mTintColor;
    private int mCheckedTextColor = Color.WHITE;

    public SegmentedGroup(Context context) {
        super(context);
        resources = getResources();
        mTintColor = resources.getColor(R.color.radio_button_selected_color);
        oneDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, resources.getDisplayMetrics());

    }

    public SegmentedGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        resources = getResources();
        mTintColor = resources.getColor(R.color.radio_button_selected_color);
        oneDP = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, resources.getDisplayMetrics());
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //Use holo light for default
        updateBackground();
    }

    public void setTintColor(int tintColor) {
        mTintColor = tintColor;
        updateBackground();
    }

    public void setTintColor(int tintColor, int checkedTextColor) {
        mTintColor = tintColor;
        mCheckedTextColor = checkedTextColor;
        updateBackground();
    }

    private void updateBackground() {
        int count = super.getChildCount();
        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        if(count == 1){
            updateBackground(getChildAt(0), R.drawable.radio_checked_default, R.drawable.radio_unchecked_default);
        }else if (count > 1) {
            int orientation = getOrientation();
            if(orientation == HORIZONTAL){
                params.setMargins(0, 0, -oneDP, 0);
            }else if(orientation == VERTICAL){
                params.setMargins(0, 0, 0, -oneDP);
            }

            for(int i = 0; i < count; i++){
                View child = getChildAt(i);
                if(i == 0){
                    if(orientation == HORIZONTAL){
                        updateBackground(child, R.drawable.radio_horizontal_checked_left, R.drawable.radio_horizontal_unchecked_left);
                    }else if(orientation == VERTICAL){
                        updateBackground(child, R.drawable.radio_vertical_checked_top, R.drawable.radio_vertical_unchecked_top);
                    }
                    child.setLayoutParams(params);
                }else if(i == count -1){
                    if(orientation == HORIZONTAL){
                        updateBackground(child, R.drawable.radio_horizontal_checked_right, R.drawable.radio_horizontal_unchecked_right);
                    }else if(orientation == VERTICAL){
                        updateBackground(child, R.drawable.radio_vertical_checked_bottom, R.drawable.radio_vertical_unchecked_bottom);
                    }
                }else{
                    updateBackground(child, R.drawable.radio_checked_middle,  R.drawable.radio_unchecked_middle);
                    child.setLayoutParams(params);
                }
            }
        }
    }

    private void updateBackground(View view, int checked, int unchecked) {
        //Set text color
        ColorStateList colorStateList = new ColorStateList(new int[][]{
                {android.R.attr.state_pressed},
                {-android.R.attr.state_pressed, -android.R.attr.state_checked},
                {-android.R.attr.state_pressed, android.R.attr.state_checked}},
                new int[]{Color.GRAY, mTintColor, mCheckedTextColor});
        ((Button) view).setTextColor(colorStateList);

        //Redraw with tint color
        Drawable checkedDrawable = resources.getDrawable(checked).mutate();
        Drawable uncheckedDrawable = resources.getDrawable(unchecked).mutate();
        ((GradientDrawable) checkedDrawable).setColor(mTintColor);
        ((GradientDrawable) uncheckedDrawable).setStroke(oneDP, mTintColor);

        //Create drawable
        StateListDrawable stateListDrawable = new StateListDrawable();
        stateListDrawable.addState(new int[]{-android.R.attr.state_checked}, uncheckedDrawable);
        stateListDrawable.addState(new int[]{android.R.attr.state_checked}, checkedDrawable);

        view.setBackgroundDrawable(stateListDrawable);
    }
}
