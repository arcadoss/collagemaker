package ru.arcadoss.collagemaker;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import ru.arcadoss.collagemaker.web.Instagram;
import ru.arcadoss.collagemaker.web.InstagramAdapter;
import ru.arcadoss.collagemaker.web.User;


@EActivity(R.layout.activity_collage_creation)
public class CollageCreation extends ActionBarActivity {
	private static final String DT = "arcturus:CollageCreation";
	@Extra User selectedUser;

	@AfterViews
	void debug() {
		Instagram service = InstagramAdapter.getService();
		service.getTopPhotos(selectedUser.id, );
	}
}
