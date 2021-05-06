package com.example.proshop.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatRadioButton;

class MyButton extends AppCompatRadioButton{

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "Squarified.ttf");
        setTypeface(typeface);
    }
}