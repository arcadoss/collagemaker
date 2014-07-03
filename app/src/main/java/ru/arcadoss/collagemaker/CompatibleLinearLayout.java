package ru.arcadoss.collagemaker;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by arcturus at 28.08.13
 */
public class CompatibleLinearLayout extends LinearLayout {
	public CompatibleLinearLayout(Context context) {
		super(context);
	}

	public CompatibleLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CompatibleLinearLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
	}

	@Override
	public void setBackground(Drawable background) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			super.setBackground(background);
		}
		else {
			super.setBackgroundDrawable(background);
		}
	}
}
