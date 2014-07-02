package ru.arcadoss.collagemaker;

import android.app.Application;
import android.os.Build;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.impl.ext.LruDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;
import org.androidannotations.annotations.EApplication;

import java.io.File;

/**
 * Created by arcturus at 01.07.14
 */
@EApplication
public class CollageApplication extends Application {
	@Override
	public void onCreate() {
		super.onCreate();

		imageLoaderInit();
	}

	private void imageLoaderInit() {
		ImageLoader loader = ImageLoader.getInstance();
		File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());

		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.showImageOnLoading(android.R.color.transparent)
				.showImageOnFail(R.drawable.grumpy_cat)
				.cacheInMemory(true)
				.cacheOnDisk(true)
				.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2)
				.displayer(new FadeInBitmapDisplayer(300))
				.build();

		ImageLoaderConfiguration.Builder configBuilder = new ImageLoaderConfiguration.Builder(getApplicationContext())
				.threadPoolSize(5)
				.threadPriority(Thread.MIN_PRIORITY + 2)
				.diskCache(new LruDiscCache(cacheDir, new HashCodeFileNameGenerator(), 30 * 1024 * 1024))
				.denyCacheImageMultipleSizesInMemory()
				.defaultDisplayImageOptions(options)
				;

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			configBuilder.memoryCacheSize(500 * 1024);
		}

		ImageLoaderConfiguration config = configBuilder.build();

		loader.init(config);
	}
}
