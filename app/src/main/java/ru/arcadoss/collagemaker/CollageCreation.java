package ru.arcadoss.collagemaker;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.nostra13.universalimageloader.core.ImageLoader;
import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;
import org.androidannotations.annotations.*;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import ru.arcadoss.collagemaker.json.EntriesListWrapper;
import ru.arcadoss.collagemaker.json.Entry;
import ru.arcadoss.collagemaker.json.Envelope;
import ru.arcadoss.collagemaker.json.User;
import ru.arcadoss.collagemaker.web.*;

import java.util.ArrayList;
import java.util.List;


@EActivity(R.layout.activity_collage_creation)
public class CollageCreation extends ActionBarActivity {
	private static final String DT = "arcturus:CollageCreation";
	@Extra User selectedUser;
	List<Entry> entries;
	@ViewById Gallery gallery;
	GalleryAdapter adapter;

	private final Callback<Envelope<EntriesListWrapper>> getPhotosCallback = new Callback<Envelope<EntriesListWrapper>>() {
		@Override
		public void success(Envelope<EntriesListWrapper> envelope, Response response) {
			entries = envelope.data.getSorted();
			adapter = new GalleryAdapter(CollageCreation.this, entries);
			gallery.setAdapter(adapter);
		}

		@Override
		public void failure(RetrofitError error) {
			Log.w(DT, error.toString());
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
