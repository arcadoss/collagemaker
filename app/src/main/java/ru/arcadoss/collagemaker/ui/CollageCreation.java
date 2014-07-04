package ru.arcadoss.collagemaker.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.*;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.*;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import org.androidannotations.annotations.*;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import ru.arcadoss.collagemaker.R;
import ru.arcadoss.collagemaker.json.EntriesListWrapper;
import ru.arcadoss.collagemaker.json.Entry;
import ru.arcadoss.collagemaker.json.Envelope;
import ru.arcadoss.collagemaker.json.User;
import ru.arcadoss.collagemaker.ui.adapter.GalleryAdapter;
import ru.arcadoss.collagemaker.ui.views.SelectableImageView;
import ru.arcadoss.collagemaker.web.Instagram;
import ru.arcadoss.collagemaker.web.InstagramAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Random;


@WindowFeature({Window.FEATURE_ACTION_BAR})
@EActivity(R.layout.activity_collage_creation)
@OptionsMenu(R.menu.collage)
public class CollageCreation extends ActionBarActivity {
	private static final String DT = "arcturus:CollageCreation";
	@Extra User selectedUser;
	@ViewById Gallery gallery;
	@ViewById SurfaceView surfaceView;
	GalleryAdapter adapter;
	@NonConfigurationInstance List<Entry> entries;
	@NonConfigurationInstance Bitmap collageBitmap = null;
	private ImageSize targetImageSize;
	private boolean couldSend = false;

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@AfterViews
	void modifyTitle() {
		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setTitle(String.format(getString(R.string.maker_title_temp), selectedUser.username));
		}
	}

	private final Callback<Envelope<EntriesListWrapper>> getPhotosCallback = new Callback<Envelope<EntriesListWrapper>>() {
		@Override
		public void success(Envelope<EntriesListWrapper> envelope, Response response) {
			entries = envelope.data.getSorted();
			adapter = new GalleryAdapter(getLayoutInflater(), entries);
			gallery.setAdapter(adapter);
			if (adapter.getCount() == 0) {
				Crouton.showText(CollageCreation.this, getString(R.string.user_without_photos), Style.INFO);
			} else {
				Crouton.showText(CollageCreation.this, getString(R.string.select_photo), Style.INFO);
			}
		}

		@Override
		public void failure(RetrofitError error) {
			Envelope<String> envelope = new Envelope<String>();

			try {
				envelope = (Envelope<String>) InstagramAdapter.getConverter()
						.fromBody(error.getResponse().getBody(), envelope.getClass());

				switch (envelope.meta.errorType) {
					case NOT_ALLOWED:
						Crouton.showText(CollageCreation.this, getString(R.string.close_access), Style.INFO);
						return;
					default:
						// ignore
				}
			} catch (ConversionException e) {
				Log.w(DT, "failed to read response error");
			}

			Crouton.showText(CollageCreation.this, getString(R.string.comm_failed), Style.ALERT);
		}
	};

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		super.onPrepareOptionsMenu(menu);
		if (couldSend) {
			MenuItem item = menu.findItem(R.id.send);
			if (item != null) {
				item.setVisible(true);
			}
		}

		return true;
	}

	@OptionsItem
	protected void sendSelected() {
//		String savedFileUri = saveBitmap();
//		if (savedFileUri != null) {
//			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
//
//			emailIntent.setType("image/jpeg");
//			emailIntent.putExtra(Intent.EXTRA_STREAM, savedFileUri);
//
//			startActivity(Intent.createChooser(emailIntent, getString(R.string.send_with)));
//		} else {
//			Crouton.showText(this, "Не удается отправить сообщение", Style.ALERT);
//		}
		sendInBackground();
	}

	@Background
	protected void sendInBackground() {
		Uri savedFileUri = saveBitmap();
		if (savedFileUri != null) {
			Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

			emailIntent.setType("application/image");
			emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{});
			emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"collage maker");
			emailIntent.putExtra(Intent.EXTRA_STREAM, savedFileUri);

			sendEmail(emailIntent);
		} else {
			Crouton.showText(this, "Не удается отправить сообщение", Style.ALERT);
		}
	}

	@UiThread
	protected void sendEmail(Intent emailIntent) {
		startActivity(Intent.createChooser(emailIntent, getString(R.string.send_with)));
	}

	private Uri saveBitmap() {
		File tmp;
		try {
			File parent = getExternalCacheDir();
			if (!parent.exists()) {
				if (!parent.mkdirs()) {
					return null;
				}
			}
			tmp = File.createTempFile("IMG", ".png", parent);
		} catch (IOException e) {
			return null;
		}

		FileOutputStream out = null;
		try {
			out = new FileOutputStream(tmp);
			collageBitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
		} catch (Exception e) {
			return null;
		} finally {
			try {
				if (out != null) {
					out.close();

				}
			} catch(Throwable ignore) {}
		}

		return Uri.fromFile(tmp);
	}

	@AfterViews
	protected void init() {
		Instagram service = InstagramAdapter.getService();
		service.getTopPhotos(selectedUser.id, getPhotosCallback);

		gallery.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				SelectableImageView img = (SelectableImageView) ((FrameLayout)view).getChildAt(0);
				drawBitmap(entries.get(position).photos.standardResolution.url);
				couldSend = true;
				invalidateOptionsMenu();
				return true;
			}
		});
	}


	//	FIXME: STUB
	@Background
	protected void drawBitmap(String url) {
		synchronized (this) {

			SurfaceHolder holder = surfaceView.getHolder();
			Canvas canvas = holder.lockCanvas();

			if (canvas == null) {
				Log.e(DT, "Cannot draw onto the canvas as it's null");
			} else {
				canvas.drawRGB(255, 255, 255);
				targetImageSize = new ImageSize(surfaceView.getHeight(), surfaceView.getWidth());

				collageBitmap = ImageLoader.getInstance().loadImageSync(url);

				canvas.drawBitmap(collageBitmap, 0, 0, null);
				holder.unlockCanvasAndPost(canvas);
			}
		}

	}
}
