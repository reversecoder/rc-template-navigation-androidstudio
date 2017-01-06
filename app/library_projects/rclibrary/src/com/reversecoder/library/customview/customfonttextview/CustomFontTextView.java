package com.reversecoder.library.customview.customfonttextview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import com.reversecoder.library.customview.application.CustomViewApplication;


public class CustomFontTextView extends TextView {
	public CustomFontTextView(Context context) {
		this(context, null);
	}

	public CustomFontTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CustomFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		setTypeface(CustomViewApplication.canaroExtraBold);
	}

}
