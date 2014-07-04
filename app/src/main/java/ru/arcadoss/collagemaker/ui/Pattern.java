package ru.arcadoss.collagemaker.ui;

import android.graphics.Bitmap;
import android.view.View;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.Observable;

/**
 * Created by arcturus at 04.07.14
 */

public abstract class Pattern extends Observable {
	boolean addPiece(String url) {
//		ImageLoader.getInstance().loadImage(url, new ImageLoadingListener() {
//			@Override
//			public void onLoadingStarted(String imageUri, View view) {
//
//			}
//
//			@Override
//			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//
//			}
//
//			@Override
//			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//
//			}
//
//			@Override
//			public void onLoadingCancelled(String imageUri, View view) {
//
//			}
//		});
		return false;
	}

	abstract boolean delPiece(String url);

	abstract int getPieceCount();

	abstract int getMaxPieceCount();

	abstract String getNameRes();

	abstract int getPicRes();
}
