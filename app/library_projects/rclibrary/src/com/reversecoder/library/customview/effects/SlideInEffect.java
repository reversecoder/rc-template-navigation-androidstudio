package com.reversecoder.library.customview.effects;

import android.view.View;

import com.reversecoder.library.customview.utils.CustomEffect;
import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

public class SlideInEffect implements CustomEffect {
	@Override
	public void initView(View item, int position, int scrollDirection) {
		ViewHelper
				.setTranslationY(item, item.getHeight() / 2 * scrollDirection);
	}

	@Override
	public void setupAnimation(View item, int position, int scrollDirection,
			ViewPropertyAnimator animator) {
		animator.translationYBy(-item.getHeight() / 2 * scrollDirection);
	}
}
