package ru.arcadoss.collagemaker;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import org.androidannotations.annotations.*;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.ConversionException;
import ru.arcadoss.collagemaker.json.EntriesListWrapper;
import ru.arcadoss.collagemaker.json.Entry;
import ru.arcadoss.collagemaker.json.Envelope;
import ru.arcadoss.collagemaker.json.User;
import ru.arcadoss.collagemaker.web.Instagram;
import ru.arcadoss.collagemaker.web.InstagramAdapter;

import java.util.ArrayList;
import java.util.List;


@WindowFeature({Window.FEATURE_ACTION_BAR})
@EActivity(R.layout.activity_collage_creation)
public class CollageCreation extends ActionBarActivity {
	private static final String DT = "arcturus:CollageCreation";
	@Extra User selectedUser;
	List<Entry> entries;
	@ViewById Gallery gallery;
	GalleryAdapter adapter;

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
			adapter = new GalleryAdapter(CollageCreation.this, entries);
			gallery.setAdapter(adapter);
			if (adapter.getCount() == 0) {
				Crouton.showText(CollageCreation.this, getString(R.string.user_without_photos), Style.INFO);
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

	@AfterViews
	void debug() {
		Instagram service = InstagramAdapter.getService();
		service.getTopPhotos(selectedUser.id, getPhotosCallback);
	}

	class GalleryAdapter extends BaseAdapter implements SpinnerAdapter{
		private Context context;
		private List<String> urls;

		GalleryAdapter(Context context, List<Entry> entryList) {
			this.context = context;
			urls = new ArrayList<String>(entryList.size());
			Log.d(DT, String.format("GalleryAdapter content: [%s]", Utils.joinStrings(urls, ",")));
			for (Entry entry : entryList) {
				urls.add(entry.photos.thumbnail.url);
			}
		}

		@Override
		public int getCount() {
			return urls.size();
		}

		@Override
		public String getItem(int position) {
			return urls.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ImageView out;

			if (convertView instanceof ImageView) {
				out = (ImageView) convertView;
			} else {
				out = new ImageView(context);
			}

			ImageLoader.getInstance().displayImage(getItem(position), out);

			return out;
		}
	}
}
