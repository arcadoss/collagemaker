package ru.arcadoss.collagemaker.ui;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ListView;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import org.androidannotations.annotations.*;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.arcadoss.collagemaker.R;
import ru.arcadoss.collagemaker.json.Envelope;
import ru.arcadoss.collagemaker.ui.adapter.AccountListAdapter;
import ru.arcadoss.collagemaker.web.Instagram;
import ru.arcadoss.collagemaker.web.InstagramAdapter;
import ru.arcadoss.collagemaker.json.User;

import java.util.List;


@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity {
	private static final String DT = "arcturus:MainActivity";
	@ViewById EditText nickname;
	@ViewById ListView accountList;
	@Bean AccountListAdapter adapter;
	@NonConfigurationInstance List<User> lastQueuedUsers;

	private final Callback<Envelope<List<User>>> findUserCallback = new Callback<Envelope<List<User>>>() {
		@Override
		public void success(Envelope<List<User>> users, Response response) {
			int size;
			if (users == null) {
				size = 0;
				lastQueuedUsers = null;
			} else {
				size = users.data.size();
				lastQueuedUsers = users.data;
			}

			switch (size) {
				case 0:
					Crouton.showText(MainActivity.this, getString(R.string.user_not_found), Style.INFO);
					return;

				case 1:
					CollageCreation_.intent(MainActivity.this).selectedUser(users.data.get(0)).start();
					break;

				default:
					adapter.setUserAccounts(users.data);
					break;
			}
		}

		@Override
		public void failure(RetrofitError error) {
			Crouton.showText(MainActivity.this, getString(R.string.comm_failed), Style.ALERT);
		}
	};

	@AfterViews
	void checkNetwork() {
	}

	@AfterViews
	void bindAdapter() {
		accountList.setAdapter(adapter);
		if (lastQueuedUsers != null && lastQueuedUsers.size() > 1) {
			adapter.setUserAccounts(lastQueuedUsers);
		}
	}

	@ItemClick
	void accountListItemClicked(User user) {
		CollageCreation_.intent(MainActivity.this).selectedUser(user).start();
	}

	@Click
	void createCollage() {
		Crouton.clearCroutonsForActivity(this);
		lastQueuedUsers = null;
		adapter.setUserAccounts(null);

		if (isInputInvalid()) {
			Crouton.showText(this, getString(R.string.enter_username), Style.INFO);
			return;
		}

		Instagram service = InstagramAdapter.getService();

		service.findUser(nickname.getText().toString(), findUserCallback);
	}

	@Override
	protected void onDestroy() {
		Crouton.cancelAllCroutons();
		super.onDestroy();
	}

	private boolean isInputInvalid() {
		Editable text = nickname.getText();
		return TextUtils.isEmpty(text);
	}
}
