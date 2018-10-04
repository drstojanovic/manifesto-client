package com.example.stefan.manifesto.utils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

public class UnfocusableEditText extends android.support.v7.widget.AppCompatEditText {
    public UnfocusableEditText(Context context) {
        super(context);
    }

    public UnfocusableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UnfocusableEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
            this.clearFocus();
        }

        return super.onKeyPreIme(keyCode, event);
    }
}
