package ru.arcadoss.collagemaker.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SpinnerAdapter;
import com.nostra13.universalimageloader.core.ImageLoader;
import ru.arcadoss.collagemaker.R;
import ru.arcadoss.collagemaker.Utils;
import ru.arcadoss.collagemaker.json.Entry;
import ru.arcadoss.collagemaker.ui.CollageCreation;

import java.util.ArrayList;
import java.util.List;

/**
* Created by arcturus at 04.07.14
*/
public class GalleryAdapter extends BaseAdapter implements SpinnerAdapter {
	private static final String DT = "arcturus:GalleryAdapter";
	private final LayoutInflater inflater;
	private final List<String> urls;

	public GalleryAdapter(LayoutInflater inflater, List<Entry> entryList) {
		this.inflater = inflater;
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
		FrameLayout out;

		if (convertView instanceof ImageView) {
			out = (FrameLayout) convertView;
		} else {
			out = (FrameLayout) inflater.inflate(R.layout.gallery_image, null);
		}

		ImageView img = (ImageView) out.getChildAt(0);
		String url = getItem(position);
		ImageLoader.getInstance().displayImage(url, img);
		img.setTag(url);

		return out;
	}
}
