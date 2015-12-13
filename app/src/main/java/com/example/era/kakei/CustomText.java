package com.example.era.kakei;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

/**
 * Created by era on 15/12/11.
 */
public class CustomText extends TextView {

    public CustomText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        String get = text.toString();
        String parseText="";
        for (int i = 0; i < get.length(); i++) {
            parseText += get.charAt(i)+"\n";
        }
        super.setText(parseText, type);
    }

    @Override
    public CharSequence getText() {
        String value = super.getText().toString();
        value = value.replace("\n","");
        return value;
    }
}
