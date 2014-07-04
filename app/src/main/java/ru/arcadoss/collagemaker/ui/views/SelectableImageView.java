package ru.arcadoss.collagemaker.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;
import ru.arcadoss.collagemaker.R;

/**
 * Created by arcturus at 04.07.14
 */
public class SelectableImageView extends ImageView implements Checkable {
	boolean selected = false;
	private Drawable circleDrawable;

	public SelectableImageView(Context context) {
		this(context, null);
	}

	public SelectableImageView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SelectableImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		circleDrawable = getResources().getDrawable(R.drawable.dark_circle);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (isChecked()) {
			circleDrawable.draw(canvas);
		}
	}

	@Override
	public void setChecked(boolean checked) {
		selected = checked;
		invalidate();
	}

	@Override
	public boolean isChecked() {
		return selected;
	}

	@Override
	public void toggle() {
		setChecked(!isChecked());
	}
}
