package ru.arcadoss.collagemaker;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import ru.arcadoss.collagemaker.json.User;


@EViewGroup(R.layout.view_user_account)
public class UserAccountView extends CompatibleLinearLayout {
	@ViewById ImageView image;
	@ViewById TextView title;

	public UserAccountView(Context context) {
		super(context);
	}

	public UserAccountView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public UserAccountView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void bind(User data) {
		if (data != null) {
			ImageLoader.getInstance().displayImage(data.profile_picture, image);
			title.setText(data.username);
		}
	}

}
